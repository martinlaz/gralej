package gralej.testers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import gralej.controller.INewStreamListener;
import gralej.controller.StreamInfo;

/**
 * A dummy stream handler that puts anything to stderr
 * 
 * @author Niels
 * @version $Id$
 * 
 */
public class DummyStreamHandler implements INewStreamListener {

    public void newStream(InputStream s, StreamInfo meta) {

        BufferedReader in = new BufferedReader(new InputStreamReader(s));

        System.err.println("---- Got new stream of type " + meta.getType());
        String line;
        try {
            while ((line = in.readLine()) != null) {
                System.err.println("Got line: " + line);
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }

}
