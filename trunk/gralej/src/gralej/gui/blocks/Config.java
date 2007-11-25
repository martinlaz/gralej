package gralej.gui.blocks;

import java.util.Properties;
import java.io.InputStream;

class Config {
    static Properties _props;
    
    static //void loadProps()
    {
        final String filename = "blocks.properties";
        InputStream is = Config.class.getResourceAsStream(filename);
        if (is == null)
            throw new RuntimeException(
                "Failed to load resource: " + filename);
        try {
            _props = new Properties();
            _props.load(is);
        }
        catch (Exception e) {
            throw new RuntimeException("Loading of properties failed");
        }
    }
    
    private static String getProp(String prop) {
        return getProp(prop, null);
    }
    
    private static String getProp(String prop, String def) {
        String value = _props.getProperty(prop, def);
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
        return getProp(name, defaultValue);
    }
    
    static String get(String name) {
        String value = getProp(name);
        if (value == null)
            throw new RuntimeException("Unknown property: " + name);
        if (value.startsWith("$") && value.length() > 1) {
            if (value.charAt(1) != '$')
                // dereference
                value = get(value.substring(1));
        }
        return value;
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
