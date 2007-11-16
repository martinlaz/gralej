package gralej.controller;

import java.io.*;

import gralej.*;
import gralej.fileIO.*;
import gralej.gui.*;
import gralej.parsers.*;
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
	
	
	public void open (File file) {
		cm.open(file);
		
	}
	
	public void close () {
		cm.close();		
	}
	
	public ContentModel getModel () {
		return cm;
	}

	public Controller() {
		cm = new ContentModel();
	}

}
