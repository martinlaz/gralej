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

package gralej.blocks.configurator;

import java.awt.Cursor;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JLabel;

/**
 *
 * @author Martin
 */
class Util {
    static final Cursor HAND_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    
    static List<Field> getColorLabels(Object obj) {
        List<Field> labs = new LinkedList<Field>();
        for (Field f : obj.getClass().getDeclaredFields()) {
            if (f.getType() == JLabel.class && f.getName().startsWith("_col")) {
                labs.add(f);
            }
        }
        return labs;
    }
}
