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

import java.io.InputStream;
import java.util.List;

/**
 * @author Niels
 * @version $Id$
 */
public interface IGraleParser {

    public List<IDataPackage> parseAll(InputStream s, StreamInfo meta)
            throws ParseException;

    public void parse(InputStream s, StreamInfo meta,
            IParseResultReceiver receiver);

}
