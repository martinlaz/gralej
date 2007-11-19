package gralej.parsers;

import gralej.controller.StreamInfo;

import java.io.InputStream;
import java.util.List;

/**
 * @author Niels
 * @version $Id$
 */
public interface IGraleParser {
	
	public List<IParsedAVM> getParses(InputStream s, StreamInfo meta);
	
	public void parse(InputStream s, StreamInfo meta, IParseResultReceiver receiver);

}
