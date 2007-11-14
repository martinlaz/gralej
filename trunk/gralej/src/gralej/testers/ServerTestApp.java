package gralej.testers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import gralej.controller.INewStreamListener;
import gralej.server.IGraleServer;
import gralej.server.SocketServer;

/**
 * A small application testing the servers
 * @author Niels
 * @version $Id$
 */
public class ServerTestApp {
	
	

	public class StreamHandler implements INewStreamListener {

		public void newStream(InputStream s, String type) {
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(s));
			
			System.err.println("-- Got new stream of type " + type);
			String line ;
			try {
				while ( ( line = in.readLine() ) != null) {
					System.err.println("Got line: " + line);
				}
			} catch (IOException e) {
				System.err.println(e);
			}

		}

	}
	
	public void runTest() throws IOException {
		StreamHandler handler = new StreamHandler();
		System.err.println("-- Creating server");
		IGraleServer server = new SocketServer(1080);
		server.registerNewStreamListener(handler);
		System.err.println("-- Starting to listen");
		server.startListening();
		System.err.println("-- Server should run in its thread now");		
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		new ServerTestApp().runTest();

	}

}
