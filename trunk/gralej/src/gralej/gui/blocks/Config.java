package gralej.gui.blocks;

import gralej.prefs.GralePreferences;
import gralej.prefs.NoDefaultPrefSettingException;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;

class Config {
    private static Properties _props;

    static void loadProps()
    {
        if (_props != null)
            return;
        InputStream is;
        final String resourceName = "blocks.properties";
        final String userPropsFilename = "gralej." + resourceName;
        String filename = System.getProperty(userPropsFilename);
        if (filename == null) {
            if (new File(userPropsFilename).exists())
                filename = userPropsFilename;
        }
        if (filename != null) {
            try {
                is = new FileInputStream(filename);
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        else {
            is = Config.class.getResourceAsStream(resourceName);
            if (is == null)
                throw new RuntimeException("Failed to load resource: " + resourceName);
        }
        try {
            _props = new Properties();
            _props.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Loading of properties failed");
        }
        finally {
            try { is.close(); } catch (IOException ex) {}
        }
    }

    private static String getProp(String prop) {
        return getProp(prop, null);
    }

    private static String getProp(String prop, String def) {
        loadProps();
        String value = System.getProperty("gralej." + prop);
        if (value == null)
            value = _props.getProperty(prop, def);
        if (value != null) {
            if (value.startsWith("$") && value.length() > 1) {
                if (value.charAt(1) != '$')
                    // dereference
                    value = getProp(value.substring(1));
            }
        }
        return value;
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
        /*
        String value = getProp(name);
        if (value == null)
            throw new RuntimeException("Unknown property: " + name);
        if (value.startsWith("$") && value.length() > 1) {
            if (value.charAt(1) != '$')
                // dereference
                value = get(value.substring(1));
        }
        return value;
         */
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
