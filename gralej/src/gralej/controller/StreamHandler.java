package gralej.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import gralej.controller.INewStreamListener;

/**
 * A dummy stream handler that just returns
 * @author Armin
 * @version $Id$
 *
 */
public class StreamHandler implements INewStreamListener {
	
	private Controller c; // a single controller registers

	public void newStream(InputStream s, String type) {

		c.notifyOfStream(s, type);
		
	}
	
	public StreamHandler (Controller c) {
		this.c = c;
	}

}
