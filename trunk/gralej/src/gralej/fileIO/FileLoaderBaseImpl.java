package gralej.fileIO;

import gralej.controller.INewStreamListener;
import gralej.controller.StreamInfo;
import gralej.server.IGraleServer;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Base implementation of the file loader 
 * (basically callback/oberver management
 * copied from {@link IGraleServer}).
 * Should be synchronized...
 * @author Niels
 * @version $Id$
 *
 */
public abstract class FileLoaderBaseImpl {
	
	private Set<INewStreamListener> listeners;
	
	public FileLoaderBaseImpl() {
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
	 * @param streamMeta the type of this new stream.
	 */
	protected void notifyListeners(InputStream s, StreamInfo streamMeta) {
		for ( INewStreamListener l : listeners ) {
			l.newStream(s, streamMeta);
		}
	}


}
