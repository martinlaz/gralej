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
    
    
    
    static String get(String name, String defaultValue) {
        return _props.getProperty(name, defaultValue);
    }
    
    static String get(String name) {
        String value = _props.getProperty(name);
        if (value == null)
            throw new RuntimeException("Unknown property: " + name);
        return value;
    }
    
    static int getInt(String name) {
        String value = get(name);
        return Integer.parseInt(value);
    }
    
    static int getInt(String name, int defaultValue) {
        String value = get(name, "");
        if (value.length() == 0)
            return defaultValue;
        return Integer.parseInt(value);
    }
}
