package gralej.blocks;

import gralej.prefs.GralePreferences;
import gralej.prefs.NoDefaultPrefSettingException;
import java.awt.Color;
import java.awt.Font;

class Config {
    
    static void set(String key, String value) {
        GralePreferences.getInstance().put(key, value);
    }
    
    static void set(String key, Boolean value) {
        GralePreferences.getInstance().putBoolean(key, value);
    }
    
    static void set(String key, int value) {
        GralePreferences.getInstance().putInt(key, value);
    }
    
    static void set(String key, Font value) {
        GralePreferences.getInstance().putFont(key, value);
    }
    
    static void set(String key, Color value) {
        GralePreferences.getInstance().putColor(key, value);
    }

    static String get(String name, String defaultValue) {
        //return getProp(name, defaultValue);
        try {
            return get(name);
        }
        catch (NoDefaultPrefSettingException e) {
            return defaultValue;
        }
    }

    static String get(String name) {
        return GralePreferences.getInstance().get(name);
    }
    
    static Color getColor(String key, String defaultColorSpec) {
        int rgba = (int) Long.parseLong(get(key, defaultColorSpec), 16);
        return new Color(rgba >> 8);
    }
    
    static Color getColor(String key) {
        int rgba = (int) Long.parseLong(get(key), 16);
        return new Color(rgba >> 8);
    }
    
    static Font getFont(String key) {
        return GralePreferences.getInstance().getFont(key);
    }
    
    static Font getFont(String key, String defaultFontSpec) {
        return GralePreferences.getInstance().getFont(key, defaultFontSpec);
    }

    static int _getInt(String name, int radix) {
        String value = get(name);
        return Integer.parseInt(value, radix);
    }

    static int _getInt(String name, int defaultValue, int radix) {
        String value = get(name, "");
        if (value.length() == 0)
            return defaultValue;
        return Integer.parseInt(value, radix);
    }

    static int getInt(String name, int defaultValue) {
        return _getInt(name, defaultValue, 10);
    }

    static int getInt(String name) {
        return _getInt(name, 10);
    }

    static int getHexInt(String name, int defaultValue) {
        return _getInt(name, defaultValue, 16);
    }

    static int getHexInt(String name) {
        return _getInt(name, 16);
    }
}
