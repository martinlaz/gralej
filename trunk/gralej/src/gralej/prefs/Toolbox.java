package gralej.prefs;

import java.awt.Color;
import java.awt.Font;

/**
 * Some static helper methods for preferences
 * 
 * @author Niels
 * @version $Id$
 */
public class Toolbox {

    /**
     * @throws PrefValueBadFormatException
     */
    public static Color parseRGBA(String rgba)
            throws PrefValueBadFormatException {

        Color res;

        try {
            String r = "" + rgba.charAt(0) + rgba.charAt(1);
            String g = "" + rgba.charAt(2) + rgba.charAt(3);
            String b = "" + rgba.charAt(4) + rgba.charAt(5);
            String a = "" + rgba.charAt(6) + rgba.charAt(7);

            res = new Color(Integer.parseInt(r, 16), Integer.parseInt(g, 16),
                    Integer.parseInt(b, 16), Integer.parseInt(a, 16));
        } catch (Exception e) {
            throw new PrefValueBadFormatException(rgba);
        }

        return res;
    }

    public static String color2RGBA(Color color) {

        // convert values to hex
        String r = Integer.toHexString(color.getRed());
        String g = Integer.toHexString(color.getGreen());
        String b = Integer.toHexString(color.getBlue());
        String a = Integer.toHexString(color.getAlpha());

        // pad up zeroes
        if (r.length() < 2)
            r = "0" + r;
        if (g.length() < 2)
            g = "0" + g;
        if (b.length() < 2)
            b = "0" + b;
        if (a.length() < 2)
            a = "0" + a;

        // return rgba string
        return r + g + b + a;
    }

    public static String font2str(Font font) {

        return "" + font.getName() + "," + font.getStyle() + ","
                + font.getSize();

    }

    /**
     * @throws PrefValueBadFormatException
     */
    public static Font str2font(String s) throws PrefValueBadFormatException {

        Font res;
        String[] fields = s.split("\\s*,\\s*");

        try {
            res = new Font(fields[0], Integer.parseInt(fields[1]), Integer
                    .parseInt(fields[2]));
        } catch (Exception e) {
            throw new PrefValueBadFormatException(s, e);
        }

        return res;

    }
    
    public static String unEscape(String s) {
    	return s.replace("\\$", "$");
    }
    
    public static String doEscape(String s) {
    	return s.replace("$", "\\$");
    }
    
    

}
