package server;

import controler.INewStreamListener;

/**
 * An interface providing the generic functions of
 * a Grale server that interacts with other
 * applications (preferably Trale) 
 * @author Niels
 * @version $Id$
 */
public interface IGraleServer {
	
	/**
	 * Registeres a new listener to this server
	 * that will be informed when a new stream comes in.
	 * @param l the listener to register.
	 */
	public void registerNewStreamListener(INewStreamListener l);
	
	/**
	 * Removes a listener from this server. If the listener
	 * has not been registered, the request will be ignored.
	 * @param l the listener to remove.
	 */
	public void removeNewStreamListener(INewStreamListener l);
	
	/**
	 * This starts the actual server functionality. Make sure
	 * to register a listener before, otherwise new connections
	 * will go to nirvana.
	 */
	public void startListening();

}
