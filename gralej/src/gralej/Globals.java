/*
 *  $Id$
 *
 *  Author:
 *     Martin Lazarov [mlazarov at sfs.uni-tuebingen.de]
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

package gralej;

import java.awt.Cursor;

public class Globals {
    public static final int VERSION_MAJOR = 0;
    public static final int VERSION_MINOR = 5;
    public static final int VERSION_REVISION = 3;
    
    public static final String VERSION_STRING =
            VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_REVISION;
    
    public static final String APP_NAME = "Gralej";
    public static final String APP_NAME_VER = 
            APP_NAME + " (" + VERSION_STRING + ")";
    
    public static final Cursor HAND_CURSOR =
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
}
