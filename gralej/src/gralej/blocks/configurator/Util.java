/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
