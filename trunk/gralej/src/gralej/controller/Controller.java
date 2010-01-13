package gralej.controller;

import gralej.Config;
import gralej.client.WebTraleClient;
import gralej.util.Log;
import gralej.fileIO.FileLoader;
import gralej.gui.MainGUI;
import gralej.parsers.GraleParserFactory;
import gralej.parsers.IGraleParser;
import gralej.parsers.IParseResultReceiver;
import gralej.parsers.IDataPackage;
import gralej.parsers.UnsupportedProtocolException;
import gralej.server.IGraleServer;
import gralej.server.SocketServer;

import java.io.File;
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
        open(FileLoader.file2url(file));
    }
    
    public void open(URL url) {

        // instantiate a new file handler
        Log.info(
                "Opening file/url " + url);
        FileLoader fl = new FileLoader(url, true);
        fl.registerNewStreamListener(this);
        Log.debug("Starting to open");
        try {
            fl.loadFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.debug("Opening should run in its thread now");

    }

    public void newStream(InputStream s, StreamInfo streamMeta) {
        Log.debug("New stream of type " + streamMeta);
        cm.newStream(streamMeta);

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

    public void streamClosed(InputStream is, StreamInfo meta, Exception ex) {
        if (is == System.in) {  // when started with the option --stdin
            if (MainGUI.getLastInstance() != null)
                MainGUI.getLastInstance().quit(true);
            else
                System.exit(0);
        }
        cm.streamClosed(meta);
        Log.debug("Stream closed: " + meta);
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
        if (Config.bool("mode.grale")) {
            final int portlo = Config.i("server.portrange.lo");
            final int porthi = Config.i("server.portrange.hi");
            boolean ok = false;
            for (int port = portlo; port <= porthi; ++port) {
                try {
                    server = new SocketServer(port);
                    server.startListening();
                    System.out.println("@|@| 127.0.0.1 " + port);
                    ok = true;
                    break;
                }
                catch (IOException ex) {
                }
            }
            if (!ok) {
                Log.error("Failed to find a port to bind in the range between",
                        portlo, "and", porthi, "-- the server won't start");
            }
        }
        else {
            int port = Config.i("server.port");
            if ( server == null ) {
                    server = new SocketServer(port);
            }	
            try {
                server.startListening();
                Log.debug("Server up and listening on port", port);
            } catch (IOException e) {
                    e.printStackTrace();
                    Log.error("Cannot bind server to network port",
                            port + ", perhaps another Gralej is running?");
            }
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

    public boolean isServerRunning() {
        return server != null && server.isListening();
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
