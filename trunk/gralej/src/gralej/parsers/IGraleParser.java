package gralej.parsers;

import java.io.InputStream;
import java.util.List;

/**
 * @author Niels
 * @version $Id$
 */
public interface IGraleParser {
	
	public List<IParsedAVM> getParses(InputStream s);
	
	public void parse(InputStream s, IParseResultReceiver receiver);

}
