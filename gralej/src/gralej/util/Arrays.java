/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.util;

/**
 *
 * @author Martin
 */
public class Arrays {
    public static String concat(String delimiter, Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objects.length; ++i) {
            if (i > 0)
                sb.append(delimiter);
            sb.append(objects[i].toString());
        }
        return sb.toString();
    }
    
    public static String concat(Object... objects) {
        return concat("", objects);
    }
}
