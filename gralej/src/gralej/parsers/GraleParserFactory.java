/*
 *  $Id$
 *
 *  Author:
 *     Niels Ott
 *     
 *  This file is part of the Gralej system
 *     http://code.google.com/p/gralej/
 *
 *  Gralej is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gralej is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package gralej.parsers;

import gralej.controller.StreamInfo;

/**
 * @author Niels
 * @version $Id$
 */
public class GraleParserFactory {

    /**
     * Creates a new parser for a given protcol/data format type.
     * 
     * @param streamMeta
     *            the format type ID string, e.g. "grisu".
     * @return the new parser.
     * @throws UnsupportedProtocolException
     *             if the ID string doesn't make sense to this factory.
     */
    public static IGraleParser createParser(StreamInfo streamMeta)
            throws UnsupportedProtocolException {
        // TODO: return other parsers
        if (streamMeta.getType().compareTo("grisu") == 0) {
            return new GrisuFormatParser();
        }
        /*
         * else if ( type.compareTo("grisu") == 0) { return new some other
         * parser }
         */

        throw new UnsupportedProtocolException("Protocol not handled: "
                + streamMeta);
    }

}
