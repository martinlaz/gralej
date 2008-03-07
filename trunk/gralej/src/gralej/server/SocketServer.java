package gralej.server;

import gralej.controller.StreamInfo;

import gralej.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * A multi-client (=threaded) grale server binding to a TCP/IP socket.
 * 
 * @author Niels
 * @version $Id:SocketServer.java 18 2007-11-13 16:26:47Z niels@drni.de $
 */
public class SocketServer extends ServerBaseImpl {

    private int port;
    private InetAddress bindIP;
    private ServerSocket socket;
    private ConnectionWaiter waiter;
    private Vector<ConnectionHandler> handlerList;
    

    /**
     * A helper class that waits for incoming connections as a separate thread.
     * This class will invoke a {@link ConnectionHandler} for each incoming
     * connection.
     */
    private class ConnectionWaiter extends Thread {

    	private boolean shutdown_state = false;
    	
        public ConnectionWaiter() {
            super();
            // informative thread name for debugging
            this.setName("ConnectionWaiter (bind: "
                    + socket.getInetAddress().getHostAddress() + ":"
                    + socket.getLocalPort() + ")");
        }

        /**
         * Run this ConnectionWaiter: This waits for incoming connections and
         * invokes new handler threads if required. Must not be run elsehwere
         * but in {@link SocketServer#startListening()}
         */
        public void run() {

            try {
                // server main loop
                while (! shutdown_state) {
                    Socket clientSocket = socket.accept();
                    new ConnectionHandler(clientSocket).start();
                }

            } catch (IOException e) {
            	e.printStackTrace();
            	if (! shutdown_state) {
            		Log.error("An exception "
            				+ "occured while waiting for incoming connections. "
            				+ "Server thread terminates now, restart the server "
            				+ "to regain networking functionality. ");
            	} else {
            		Log.warning(this.getName() + 
            				": Caught exception during server shutdown, " +
            				"this may be normal.");
            	}

            }
            
            // if this was a shutdown, then it's finished now
            shutdown_state = false;
        }
        
        synchronized void shutdownWaiter() throws IOException {
    		shutdown_state = true;
    		socket.close();
        }
    }

    /**
     * A class resp. thread that handles an incoming connection and informs the
     * listeners of this {@link SocketServer}.
     */
    private class ConnectionHandler extends Thread {

        private Socket clientSocket;
        private boolean shutdown_state = false;

        public ConnectionHandler(Socket clientSocket) {
            super();
            this.clientSocket = clientSocket;
            // informative thread name for debugging
            this.setName("ConnectionHandler (remote: "
                    + clientSocket.getInetAddress().getHostAddress() + ":"
                    + clientSocket.getPort() + ")");
        }

        /**
         * Runs this ConnectionHandler: This will detect the input protocol and
         * hand over to the listeners of the {@link SocketServer} afterwards.
         */
        public void run() {

            // System.err.println("- SocketServer accepted new connection!");

        	registerConnHandler(this);
        	
            try {
                BufferedInputStream s = new BufferedInputStream(clientSocket
                        .getInputStream());
                StreamInfo info = new StreamInfo(StreamProtocolMagic
                        .stream2type(s));
                notifyListeners(s, info);
            } catch (IOException e) {
                // the remote host closed the connection before something
                // useful has happened, we can ignore this.
            	e.printStackTrace();
            	if ( ! shutdown_state) {
            		Log.warning(this.getName()
            				+ ": Remote closed connection "
            				+ "before sending something useful. Closing handler.");
            	} else {
            		Log.warning(this.getName() + ": " +
            				"Caught exception during connection shutdown, " +
            				"this may be normal.");
            	}
            	
            }
            
            removeConnHandler(this);

        }
        
        synchronized private void killConnection() throws IOException {
        	shutdown_state = true;
        	clientSocket.close();
        }

    }

    /**
     * Instantiates a new socket server listening to 127.0.0.1 (aka localhost)
     * only.
     * 
     * @param port
     *            the port to bind to on localhost.
     */
    public SocketServer(int port) {
        try {
            bindIP = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            // this should never ever happen
            throw new RuntimeException(e);
        }
        this.port = port;
        handlerList = new Vector<ConnectionHandler>();
    }
    
    private void registerConnHandler(ConnectionHandler c) {
    	// vectors are synchronized, this should be thread-safe
    	handlerList.add(c);
    }
    
    private void removeConnHandler(ConnectionHandler c) {
    	// vectors are synchronized, this should be thread-safe
    	handlerList.removeElement(c);
    }

    /**
     * A new socket server listening to a given IP address, allowing binding to
     * public specific network interfaces.
     * 
     * @param port
     *            the port to bind to.
     * @param bind
     *            address of the interface to bind to.
     * @throws NotImplementedInServerException
     */
    public SocketServer(int port, InetAddress bind)
            throws NotImplementedInServerException {
        this.port = port;
        this.bindIP = bind;
        // TODO: binding to public interfaces without access control is disabled
        throw new NotImplementedInServerException();
    }

    /**
     * @see IGraleServer#startListening()
     */
    public void startListening() throws IOException {
    	
    	if ( waiter != null ) {
    		return;
    	}

        // open the port, this may go wrong
        socket = new ServerSocket(port, 0, bindIP);

        // run the server main loop thread
        waiter = new  ConnectionWaiter();
        waiter.start();

    }

	public boolean isListening() {
		return ( waiter != null && socket.isBound() );
	}

	public void stopListening() throws IOException {
		waiter.shutdownWaiter();
		// hopefully the garbage collector will do its job now...
		waiter = null;
	}

	public void killActiveConnections() throws IOException {
		// copy vector because it will be modified by terminating
		// connections

		// clone manually to ensure the right outcome
		Vector<ConnectionHandler> handlers = new Vector<ConnectionHandler>();
		for ( ConnectionHandler c : handlerList ) {
			handlers.add(c);
		}
		
		for ( ConnectionHandler c : handlers) {
			c.killConnection();
		}
		
	}
    
    

}
