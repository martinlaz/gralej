package gralej.testers;

import gralej.controller.INewStreamListener;
import gralej.server.IGraleServer;
import gralej.server.SocketServer;

import java.io.IOException;

/**
 * A small application testing the servers
 * 
 * @author Niels
 * @version $Id$
 */
public class ServerTestApp {

    public void runTest() throws IOException {
        INewStreamListener handler = new DummyStreamHandler();
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

        new ServerTestApp().runTest();

    }

}
