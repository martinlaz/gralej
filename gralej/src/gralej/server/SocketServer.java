package gralej.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A multi-client (=threaded) grale server binding to a TCP/IP socket.
 * FIXME: Handle access control!
 * @author Niels
 * @version $Id:SocketServer.java 18 2007-11-13 16:26:47Z niels@drni.de $
 */
public class SocketServer extends ServerBaseImpl {

	private int port;
	private InetAddress bindIP;
	private ServerSocket socket;

	/**
	 * A helper class that waits for incoming
	 * connections as a separate thread.
	 * This class will invoke a
	 * {@link ConnectionHandler} for each incoming connection.
	 */
	private class ConnectionWaiter extends Thread {
		
		/**
		 * Run this ConnectionWaiter: This waits for incoming connections 
		 * and invokes new handler threads if required. Must not be run 
		 * elsehwere but in {@link SocketServer#startListening()}
		 */
		public void run() {
			
			// server main loop
			while ( true ) {
				try {
					System.err.println("- SocketServer waiting for (more) connections.");
					Socket clientSocket = socket.accept();
					new ConnectionHandler(clientSocket).start();
				} catch (IOException e) {
					// FIXME: handle failing properly here, infinite loop ahead!
					e.printStackTrace();
				}
				
			}
		}
	}

	/**
	 * A class resp. thread that handles an incoming
	 * connection and informs the listeners of this
	 * {@link SocketServer}.
	 */
	private class ConnectionHandler extends Thread {
		
		private Socket clientSocket;
		
		public ConnectionHandler(Socket clientSocket) {
			super();
			this.clientSocket = clientSocket;
		}
		
		/**
		 * Runs this ConnectionHandler: This will
		 * detect the input protocol and hand
		 * over to the listeners of the {@link SocketServer}
		 * afterwards.
		 */
		public void run() {
			
			System.err.println("- SocketServer accepted new connection!");
			
			DataInputStream is;
			try {
				is = new DataInputStream(
						clientSocket.getInputStream());
				// TODO: do protocol type detection here
				String type = "unknown";
				notifyListeners(is, type);
			} catch (IOException e) {
				// FIXME: do proper handling of exception here
				e.printStackTrace();
			}
			
		}

	}
	
	/**
	 * Instantiates a new socket server listening to
	 * 127.0.0.1 (aka localhost) only.
	 * @param port the port to bind to on localhost.
	 */
	public SocketServer(int port) {
		try {
			bindIP = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			// this should never ever happen
			throw new RuntimeException(e);
		}
		this.port = port;
	}
	
	/**
	 * A new socket server listening to
	 * a given IP address, allowing binding to
	 * public specific network interfaces.
	 * @param port the port to bind to.
	 * @param bind address of the interface to bind to.
	 */
	public SocketServer(int port, InetAddress bind) {
		this.port = port;
		this.bindIP = bind;
	}
	
	/**
	 * @see IGraleServer#startListening()
	 */
	public void startListening() throws IOException {

		// open the port, this may go wrong
		socket = new ServerSocket(port, 0, bindIP);
		
		// run the server main loop thread
		new ConnectionWaiter().start();

	}

}
