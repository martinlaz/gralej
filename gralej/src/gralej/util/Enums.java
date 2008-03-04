/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.util;

import java.lang.reflect.Field;

/**
 *
 * @author Martin
 */
public class Enums {
    public static <T extends Enum<T>> T valueOf(Class<T> enumType, int ordinal) {
        for (Field f : enumType.getFields()) {
            if (f.isEnumConstant()) {
                T e;
                try {
                    e = (T) f.get(null);
                }
                catch (IllegalAccessException ex) {
                    continue;
                }
                if (e.ordinal() == ordinal)
                    return e;
            }
        }
        throw new IllegalArgumentException(
            "Cannot find an enum with ordinal " + ordinal + " in " + enumType);
    }
}
