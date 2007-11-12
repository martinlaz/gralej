package controler;

import java.io.InputStream;

/**
 * An interface providing a listener to the
 * server or file input mechanism. 
 * Those invoke the handle on a new stream.
 * The purpose of this interface is to
 * decouple the concrete Controler
 * from the I/O mechanisms.
 * @author Niels
 * @version $Id$
 */
public interface INewStream {
	
	/**
	 * message handler to be invoked when
	 * a new data stream comes in.
	 * @param s the stream
	 * @param type a string indicating the data format or 
	 * protocol of the stream.
	 */
	public void newStream(InputStream s, String type); 


}
