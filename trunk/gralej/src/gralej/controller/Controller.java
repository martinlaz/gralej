package gralej.controller;

import gralej.fileIO.FileLoader;
import gralej.parsers.GraleParserFactory;
import gralej.parsers.IGraleParser;
import gralej.parsers.IParseResultReceiver;
import gralej.parsers.IParsedAVM;
import gralej.parsers.UnsupportedProtocolException;
import gralej.server.IGraleServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
	
	public void newStream(InputStream s, String type) {
		System.err.println("-- Controller got new stream of type " + type);				
		
		IGraleParser parser;
		try {
			// ask parser factory for parser
			parser = GraleParserFactory.createParser(type);
			// plug stream into parser, and wait for results
			parser.parse(s, this);
		} catch (UnsupportedProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

	public synchronized void newParse(IParsedAVM parse) {
		System.err.println("-- Controller got new parse");				

		cm.open(parse.display(), parse.getName());
		
		
	}

	
	public void close () {
		// notify content model of change. to this change in the cm, the gui listens
		cm.close();		
		// this command is passed to the CM, 
		// however the preferences call is sent via getModel().
		// TODO settle on one way
	}
	
	public ContentModel getModel () {
		return cm;
	}

	public Controller(IGraleServer server) {
		server.registerNewStreamListener(this);
		
		cm = new ContentModel();
		
	}

}
