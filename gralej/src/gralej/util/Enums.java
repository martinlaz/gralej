/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.util;

/**
 *
 * @author Martin
 */
public class Enums {
    public static <T extends Enum<T>> T valueOf(Class<T> enumType, int ordinal) {
        for (T e : enumType.getEnumConstants()) {
            if (e.ordinal() == ordinal)
                return e;
        }
        throw new IllegalArgumentException(
            "Cannot find an enum with ordinal " + ordinal + " in " + enumType);
    }
}
