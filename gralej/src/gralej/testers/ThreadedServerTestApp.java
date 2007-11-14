package gralej.testers;

import gralej.controller.INewStreamListener;
import gralej.server.IGraleServer;
import gralej.server.SocketServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A small application testing the servers in a threaded
 * way, but this seems unnecessary
 * @author Niels
 * @version $Id$
 */
public class ThreadedServerTestApp {
	
	public class StreamHandler extends Thread {
		
		private String type;
		private InputStream is;
		
		public StreamHandler(InputStream s, String type) {
			super();
			this.type = type;
			this.is = s;
		}
		
		public void run() {
			System.err.println("---- Hello, I'm a new StreamHandler thread" +
					" operating on a stream of type " + type);
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(is));
			
			String line ;
			try {
				while ( ( line = in.readLine() ) != null) {
					System.err.println("---- StreamHandler got line: " + line);
				}
			} catch (IOException e) {
				System.err.println(e);
			}
			
		}
		
	}
	
	

	public class StreamListener implements INewStreamListener {

		public void newStream(InputStream s, String type) {
			
			System.err.println("-- Got new stream of type " + type 
					+ ". creating new stream handler...");
			new StreamHandler(s, type).start();
			System.err.println("-- StreamListener ready for new calls.");

		}

	}
	
	public void runTest() throws IOException {
		StreamListener handler = new StreamListener();
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
		
		new ThreadedServerTestApp().runTest();

	}

}
