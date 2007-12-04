package gralej.parsers;

import gralej.controller.StreamInfo;

import java.io.InputStream;
import java.util.List;

/**
 * @author Niels
 * @version $Id$
 */
public interface IGraleParser {
	
	public List<IDataPackage> getParses(InputStream s, StreamInfo meta) throws ParseException;
	
	public void parse(InputStream s, StreamInfo meta, IParseResultReceiver receiver);

}
