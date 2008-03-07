package gralej.controller;

import gralej.client.WebTraleClient;
import gralej.util.Log;
import gralej.fileIO.FileLoader;
import gralej.parsers.GraleParserFactory;
import gralej.parsers.IGraleParser;
import gralej.parsers.IParseResultReceiver;
import gralej.parsers.IDataPackage;
import gralej.parsers.UnsupportedProtocolException;
import gralej.prefs.GralePreferences;
import gralej.server.IGraleServer;
import gralej.server.SocketServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * The controller is the central element of the program's design.
 * 
 * It listens to input interfaces (server and file) and to the parser interface
 * it takes input and passes it on to the parser it takes parses and passes them
 * on to the content model (which the gui listens to)
 * 
 * 
 * @author Armin
 * @version $Id$
 */

public class Controller implements INewStreamListener, IParseResultReceiver {

    private ContentModel cm;
    
    private IGraleServer server;

    public void open(File file) {

        // instantiate a new file handler
        Log.info(
                "Opening File " + file.getAbsolutePath());
        FileLoader fl = new FileLoader(file, true);
        fl.registerNewStreamListener(this);
        Log.debug("Starting to open");
        try {
            fl.loadFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.debug("Opening should run in its thread now");

    }

    public void newStream(InputStream s, StreamInfo streamMeta) {
        Log.info("New stream of type " + streamMeta);

        try {
            // ask parser factory for parser
            IGraleParser parser = GraleParserFactory.createParser(streamMeta);
            // plug stream into parser, and wait for results
            parser.parse(s, streamMeta, this);
        } catch (UnsupportedProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void streamClosed(StreamInfo meta, Exception ex) {
        Log.info("Stream closed: " + meta);
        if (ex != null)
            Log.error("Exception:", ex);
    }

    public void newDataPackage(IDataPackage parse) {
        Log.debug("Controller got new parse");

        class ParseShowingRunnable implements Runnable {
            IDataPackage parse;

            ParseShowingRunnable(IDataPackage parse) {
                this.parse = parse;
            }

            public void run() {
                cm.open(parse);
            }

        }

        try {
            javax.swing.SwingUtilities.invokeAndWait(
                    new ParseShowingRunnable(parse));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public ContentModel getModel() {
        return cm;
    }

    public Controller() {
        cm = new ContentModel();
    }
    
    public void startServer () {
        GralePreferences gp = GralePreferences.getInstance();
        int port = gp.getInt("server.port");
        if ( server == null ) {
        	server = new SocketServer(port);
        }	
        try {
            server.startListening();
            Log.info("Server up and listening");
        } catch (IOException e) {
        	e.printStackTrace();
        	Log.error(
        			"Cannot bind server to network port",
        			port
        			+ ", perhaps another GraleJ is running?"
                                );
        }

        server.registerNewStreamListener(this);
        cm.notifyOfServerConnection(true);

    }
    
    public void stopServer () {
        // server.shutdown(); // TODO uncomment once implemented
        //server = null;
        try {
			server.killActiveConnections();
			server.stopListening();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		server.removeNewStreamListener(this);
        cm.notifyOfServerConnection(false);
    }

    public void startWebTraleClient(URL url) {
        final WebTraleClient wtc = WebTraleClient.inFrame(url);
        new Thread(new Runnable() {
            public void run() {
                newStream(wtc.getInputStream(), 
                        new StreamInfo("grisu", "WebTrale"));
            }
        }).start();
    }

}
