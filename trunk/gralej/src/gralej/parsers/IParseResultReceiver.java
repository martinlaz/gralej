package gralej.parsers;

import gralej.controller.StreamInfo;

/**
 * @author Niels
 * @version $Id$
 */
public interface IParseResultReceiver {

    void newDataPackage(IDataPackage data);

    void streamClosed(StreamInfo meta, Exception exception);

}
