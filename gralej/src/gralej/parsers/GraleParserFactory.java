package gralej.parsers;

/**
 * @author Niels
 * @version $Id$
 */
public class GraleParserFactory {
	
	/**
	 * Creates a new parser for a given protcol/data format type.
	 * @param type the format type ID string, e.g. "grisu".
	 * @return the new parser.
	 * @throws UnsupportedProtocolException if the ID string doesn't make 
	 * sense to this factory.
	 */
	public static IGraleParser createParser(String type) throws UnsupportedProtocolException {
		// TODO: return other parsers
		if ( type.compareTo("grisu") == 0) {
			return new GrisuFormatParser();
		}
		/*else if ( type.compareTo("grisu") == 0)  {
			return new some other parser
		}*/
		
		throw new UnsupportedProtocolException("Protocol not handled: "
				+ type);
	}

}
