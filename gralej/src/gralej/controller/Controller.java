package gralej.controller;

import gralej.fileIO.FileLoader;
import gralej.parsers.GraleParserFactory;
import gralej.parsers.IGraleParser;
import gralej.parsers.IParseResultReceiver;
import gralej.parsers.IDataPackage;
import gralej.parsers.UnsupportedProtocolException;
import gralej.server.IGraleServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * The controller is the central element of the program's design.
 * 
 * It listens to input interfaces (server and file) and to the parser interface
 * it takes input and passes it on to the parser
 * it takes parses and passes them on to the content model (which the gui listens to)
 * 
 * 
 * @author Armin
 * @version $Id$
 */

public class Controller implements INewStreamListener, IParseResultReceiver {
	
	private ContentModel cm; // 
		
	public void open (File file) {
		
		// instantiate a new file handler
		System.err.println("-- Opening File " + file.getAbsolutePath());
		FileLoader fl = new FileLoader(file, true);
		fl.registerNewStreamListener(this);
		System.err.println("-- Starting to open");
		try {
			fl.loadFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.err.println("-- Opening should run in its thread now");		
		
	}
	
	public void newStream(InputStream s, StreamInfo streamMeta) {
		System.err.println("-- Controller got new stream of type " + streamMeta);				
		
		IGraleParser parser;
		try {
			// ask parser factory for parser
			parser = GraleParserFactory.createParser(streamMeta);
			// plug stream into parser, and wait for results
			parser.parse(s, streamMeta, this);
		} catch (UnsupportedProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
        
        public void streamClosed(StreamInfo meta, Exception ex) {
            
        }
	
	

	public void newDataPackage(IDataPackage parse) {
		System.err.println("-- Controller got new parse");				
		
		class ParseShowingRunnable implements Runnable {
			IDataPackage parse;
			
			ParseShowingRunnable (IDataPackage parse) {
				this.parse = parse;
			}
			
			public void run () {
        		cm.open(parse.createView(), parse.getTitle());				
			}
			
		}

        try {
			javax.swing.SwingUtilities.invokeAndWait(new ParseShowingRunnable(parse));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
	public void close () {
		cm.close();		
	}
	
	public void closeAll() {
		cm.closeAll();
	}
	
	public ContentModel getModel () {
		return cm;
	}

	public Controller(IGraleServer server) {
		server.registerNewStreamListener(this);
		
		cm = new ContentModel();
		
	}

}
