package gralej.server;

import gralej.controller.INewStreamListener;

import java.io.IOException;

/**
 * An interface providing the generic functions of a Grale server that interacts
 * with other applications (preferably Trale) TODO: Does it make sense to have
 * more than one listener? after all only one of them can read from the stream.
 * 
 * @author Niels
 * @version $Id:IGraleServer.java 18 2007-11-13 16:26:47Z niels@drni.de $
 */
public interface IGraleServer {

    /**
     * Registeres a new listener to this server that will be informed when a new
     * stream comes in. If this listener is already present, the request should
     * be ignored.
     * 
     * @param l
     *            the listener to register.
     */
    public void registerNewStreamListener(INewStreamListener l);

    /**
     * Removes a listener from this server. If the listener has not been
     * registered, the request should be ignored.
     * 
     * @param l
     *            the listener to remove.
     */
    public void removeNewStreamListener(INewStreamListener l);

    /**
     * This starts the actual server functionality. Implementations may invoke a
     * thread in the background and return immediately. Make sure to register a
     * listener before, otherwise new connections will go to nirvana.
     * 
     * @throws IOException
     *             if the server cannot be started, e.g. if the port to bind to
     *             is already in use.
     */
    public void startListening() throws IOException;

}
