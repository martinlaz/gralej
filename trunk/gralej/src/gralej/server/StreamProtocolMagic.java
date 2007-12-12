package gralej.server;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * A class providing guessing methods to detect the protocol/format of a stream.
 * The stream must support the mark() method
 * 
 * @author Niels
 * @version $Id$
 * 
 */
public class StreamProtocolMagic {

    private static String doMagic(String inputSnippet) {

        if (inputSnippet.startsWith("!newdata")) {
            return "grisu";
        }

        // TODO: implement protocol here, including encoding detection.

        if (inputSnippet.matches(".*<\\?xml.+")) {
            return "xml-unknown";
        }

        return "unknown";
    }

    /**
     * Detects the type if the stream and returns a string identifying it.
     * Attention: Do not use
     * <code>StreamProtocolMagic.stream2type(new BufferedInputStream(somestream)</code>!
     * As the stream will be reset, one should pass on one and the same
     * BufferedInputStream afterwards, otherwise the bytes read for the magic
     * mechanism will be lost.
     * 
     * @param in
     *            the stream to detect the format from
     * @return the format ID or "unknown" if this fails
     * @throws IOException
     * @throws IOException
     * @throws IOException
     *             if reading from the stream fails or the stream cannot be
     *             reset.
     */
    public static String stream2type(BufferedInputStream in) throws IOException {

        // max buffer size to read in
        int bufsize = 1024;

        // mark bufsize bytes to be buffered
        in.mark(bufsize);

        // try to read 1024 bytes
        byte[] buf = new byte[bufsize];
        int len = in.read(buf, 0, bufsize);

        // if length is < 1, nothing ever was sent but connection
        // has been closed
        if (len < 1) {
            throw new IOException(
                    "Connection closed by remote without sending data.");
        }

        String code = new String(buf, 0, len);

        // reset the stream
        in.reset();

        // hand over to the guesser

        return doMagic(code);
    }

}
