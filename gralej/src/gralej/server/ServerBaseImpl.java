package gralej.server;


import gralej.controller.INewStreamListener;
import gralej.controller.StreamInfo;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * A Grale server base implementation for the observer
 * busines, do not forget to call <code>super()</code> when extending!
 * This should be synchronized.
 * @author no
 *
 */
public abstract class ServerBaseImpl implements IGraleServer {
	
	private Set<INewStreamListener> listeners;
	
	public ServerBaseImpl() {
		listeners = 
			Collections.synchronizedSet(new HashSet<INewStreamListener>());
	}

	/**
	 * @see IGraleServer#registerNewStreamListener(INewStreamListener)
	 */
	public void registerNewStreamListener(INewStreamListener l) {
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
	protected void notifyListeners(InputStream s, StreamInfo meta) {
		for ( INewStreamListener l : listeners ) {
			l.newStream(s, meta);
		}
	}

}
