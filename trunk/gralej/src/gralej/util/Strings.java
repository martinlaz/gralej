// $Id$
//
// Copyright (C) 2009, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.util;

/**
 *
 * @author Martin
 */
public final class Strings {
    public static class IllegalEscapeSequence extends RuntimeException {
        IllegalEscapeSequence(String s, Throwable cause) {
            super(s, cause);
        }
    }
    public static String unescapeCString(String s) {
        if (s.indexOf('\\') == -1)
            return s;
        StringBuilder sb = new StringBuilder(s.length());
        char[] chars = s.toCharArray();
        int cp; // unicode code point
        try {
            for (int i = 0; i < chars.length; ++i) {
                char c = chars[i];
                if (c != '\\') {
                    sb.append(c);
                    continue;
                }
                switch (c = chars[++i]) {
                    case 'n':
                        sb.append('\n');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case '\\':
                        sb.append('\\');
                        break;
                    case 'u':
                        cp = Integer.parseInt(new String(
                                new char[] {chars[++i], chars[++i], chars[++i], chars[++i]}
                                ), 16);
                        if (!Character.isValidCodePoint(cp))
                            throw new RuntimeException("Invalid code point: " + cp);
                        sb.append(Character.toChars(cp));
                        break;
                    default:
                        sb.append("\\").append(c);
                }
            }
        }
        catch (Exception ex) {
            throw new IllegalEscapeSequence(s, ex);
        }
        return sb.toString();
    }
}
