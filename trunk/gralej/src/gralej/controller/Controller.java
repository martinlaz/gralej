package gralej.controller;

import java.io.*;

import gralej.*;
import gralej.fileIO.FileLoader;
import gralej.parsers.*;
import gralej.testers.DummyStreamHandler;
import gralej.server.*;

/**
 * The controller is the central element of the program's design.
 * 
 * It listens to input interfaces (server and file) and to the parser interface
 * it takes input and passes it on to the parser
 * it takes parses and passes them on to the viewer? or does the viewer listen?
 * 
 * 
 * @author Armin
 *
 */

public class Controller {
	
	private ContentModel cm; // 
	
	private IGraleServer server;
	
	private INewStreamListener listener;
		
	private IParseResultReceiver receiver; // listens to parse results
	
	
	public void open (File file) {
		
		// old way: let opening be handled by the model
		// TODO postpone this until parse is received
		// TODO make it independent from a File (currently only used for naming the window)
		cm.open(file);

		
		// new way: instantiate a new file handler
		System.err.println("-- Opening File " + file.getAbsolutePath());
		FileLoader fl = new FileLoader(file, true);
		fl.registerNewStreamListener(listener);
		System.err.println("-- Starting to open");
		try {
			fl.loadFile();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.err.println("-- Opening should run in its thread now");		
		
	}
	
	public void notifyOfStream(InputStream s, String type) {
		System.err.println("-- Controller got new stream of type " + type);				
		
		IGraleParser parser;
		try {
			// ask parser factory for parser
			parser = GraleParserFactory.createParser(type);
			// plug stream into parser, and wait for results
			parser.parse(s, receiver);
		} catch (UnsupportedProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close () {
		// notify content model of change. to this change in the cm, the gui listens
		cm.close();		
	}
	
	public ContentModel getModel () {
		return cm;
	}

	public Controller(IGraleServer server) {
		this.server = server;
		listener = new StreamHandler(this);
		server.registerNewStreamListener(listener);
		
		cm = new ContentModel();
		
		// instantiate parse result receiver
		
	}

}
