package gralej.controller;

import java.io.InputStream;

/**
 * An interface providing a listener to the server or file input mechanism.
 * Those invoke the handle on a new stream. The purpose of this interface is to
 * decouple the concrete Controler from the I/O mechanisms.
 * 
 * @author Niels
 * @version $Id: INewStreamListener.java 12 2007-11-13 14:39:45Z niels@drni.de $
 */
public interface INewStreamListener {

    /**
     * message handler to be invoked when a new data stream comes in.
     * 
     * @param s
     *            the stream
     * @param streamMeta
     *            a string indicating the data format or protocol of the stream.
     */
    public void newStream(InputStream s, StreamInfo streamMeta);

}
