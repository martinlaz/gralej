package gralej.parsers;

import java.io.InputStream;
import java.util.List;

/**
 * A dummy class representing a parser for the 
 * plain old TRALE/Grisu interchange protocol
 * @author Niels
 * @version $Id$
 */
class GrisuFormatParser implements IGraleParser {

	public List<AVMObject> getParses(InputStream s) {
		// FIXME: do parsing and don't return null
		return null;
	}

	public void parse(InputStream s, IParseResultReceiver receiver) {
		// FIXME: do parsing and send message to receivers

	}

}
