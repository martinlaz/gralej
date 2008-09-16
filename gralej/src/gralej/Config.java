// $Id$

package gralej;

import gralej.util.ChangeEventSource;
import gralej.util.Log;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.prefs.Preferences;

public final class Config extends ChangeEventSource {
    private static final String DEFAULT_CONFIG = "/gralej/DefaultConfig.xml";
    
    private static Config _default;
    private static Config _current;
    
    private Properties _props = new Properties();
    
    public static class UnknownKeyException extends RuntimeException {
        private UnknownKeyException(String key) {
            super(key);
        }
    }
    
    private Config() {}
    
    public Config(Config that) {
        for (String key : that._props.stringPropertyNames()) {
            _props.put(key, that._props.get(key));
        }
    }
    
    public static Config defaultConfig() {
        if (_default == null) {
            _default = new Config();
            try {
                _default._props.loadFromXML(
                    Config.class.getResourceAsStream(DEFAULT_CONFIG));
            }
            catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return _default;
    }
    
    public static Config currentConfig() {
        if (_current == null) {
            _current = new Config(defaultConfig());
            // override values
            try {
                Preferences prefs = Preferences.userNodeForPackage(
                    Config.class);
                Properties props = _current._props;
                for (Object key : props.keySet()) {
                    String defaultValue = (String) props.get(key);
                    props.put(key, prefs.get((String) key, defaultValue));
                }
            }
            catch (SecurityException e) {
                // we're not using the logger here because
                // it uses us, and we're not available yet
                e.printStackTrace();
                System.err.println("^-- will use default config only");
            }
        }
        return _current;
    }
    
    public void reset() {
        Properties props = new Properties();
        props.putAll(defaultConfig()._props);
        _props = props;
        save();
    }
    
    public boolean updateFrom(Config other) {
        return updateFrom(other, true);
    }
    
    public boolean updateFrom(Config other, boolean saveIfChanged) {
        boolean changed = false;
        for (Object key : other._props.keySet()) {
            Object value = other._props.get(key);
            if (!_props.put(key, value).equals(value))
                changed = true;
        }
        if (changed && saveIfChanged)
            save();
        return changed;
    }
    
    public void save() {
        Preferences prefs;
        try {
            prefs = Preferences.userNodeForPackage(Config.class);
        }
        catch (SecurityException e) {
            Log.info(
                "can't access user's preferences;",
                "won't store modified config keys");
            return;
        }
        
        for (Object key : _props.keySet()) {
            Object value = _props.get(key);
            Object defaultValue = _default._props.get(key);
            if (!value.equals(defaultValue)) {
                prefs.put((String)key, (String)value);
            }
            else {
                prefs.remove((String)key);
            }
        }
    }
    
    public void exportTo(OutputStream os) throws IOException {
        String comment = "Exported from " + Globals.APP_NAME_VER
            + " on " + Calendar.getInstance().getTime();
        _props.storeToXML(os, comment);
        os.flush();
    }
    
    public void importFrom(InputStream is) throws IOException {
        _props.loadFromXML(is);
        Set<Object> keys = new HashSet<Object>(_props.keySet());
        keys.removeAll(defaultConfig()._props.keySet());
        if (!keys.isEmpty()) {
            Log.warning("The following keys will be ignored:");
            for (Object key : keys) {
                Log.warning("\t" + key);
            }
        }
    }
    
    public void fireStateChanged(Object sender) {
        super.fireStateChanged(sender);
    }
    
    public String get(String key) {
        if (!_props.containsKey(key))
            throw new UnknownKeyException(key);
        return (String)_props.get(key);
    }
    
    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }
    
    public double getDouble(String key) {
        return Double.parseDouble(get(key));
    }
    
    public boolean getBool(String key) {
        return "true".equals(get(key));
    }
    
    public Color getColor(String key) {
        return Color.decode(get(key));
    }
    
    public Font getFont(String key) {
        return Font.decode(get(key));
    }
    
    public void put(String key, String value) {
        if (!_props.containsKey(key))
            throw new UnknownKeyException(key);
        _props.put(key, value);
    }
    
    public void put(String key, int value) {
        put(key, Integer.toString(value));
    }
    
    public void put(String key, double value) {
        put(key, Double.toString(value));
    }
    
    public void put(String key, boolean value) {
        put(key, value ? "true" : "false");
    }
    
    public void put(String key, Color value) {
        String s = String.format(
            "0x%02x%02x%02x",
            value.getRed(), value.getGreen(), value.getBlue()
            );
        put(key, s);
    }
    
    public void put(String key, Font font) {
        String style;
        switch (font.getStyle()) {
            case Font.BOLD:
                style = "BOLD";
                break;
            case Font.ITALIC:
                style = "ITALIC";
                break;
            default:
                style = "PLAIN";
        }
        put(key, font.getName() + "-" + style + "-" + font.getSize());
    }
    
    // some conviniences
    public static String s(String key) {
        return currentConfig().get(key);
    }
    
    public static int i(String key) {
        return currentConfig().getInt(key);
    }
    
    public static double f(String key) {
        return currentConfig().getDouble(key);
    }
    
    public static boolean bool(String key) {
        return currentConfig().getBool(key);
    }
    
    public static Color color(String key) {
        return currentConfig().getColor(key);
    }
    
    public static Font font(String key) {
        return currentConfig().getFont(key);
    }
}
