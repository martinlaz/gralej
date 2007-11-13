package server;

import java.io.InputStream;
import java.util.HashSet;

import controler.INewStreamListener;

/**
 * A Grale server base implementation
 * @author no
 *
 */
public abstract class ServerBaseImpl implements IGraleServer {
	
	private HashSet<INewStreamListener> listeners; 

	/**
	 * @see IGraleServer#registerNewStreamListener(INewStreamListener)
	 */
	public void registerNewStreamListener(INewStreamListener l) {
		// instantiate listener list if necessary
		if ( listeners == null) {
			listeners = new HashSet<INewStreamListener>();
		}
		
		// we don't like non-instanciated listeners
		if ( l == null ) {
			throw new NullPointerException("Listeners must not be null");
		}
		
		listeners.add(l);
	}

	/**
	 * @see IGraleServer#removeNewStreamListener(INewStreamListener)
	 */
	public void removeNewStreamListener(INewStreamListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Ring up the listeners!
	 * @param s the new stream that we've found.
	 * @param type the type of this new stream.
	 */
	protected void notifyListeners(InputStream s, String type) {
		for ( INewStreamListener l : listeners ) {
			l.newStream(s, type);
		}
	}

}
