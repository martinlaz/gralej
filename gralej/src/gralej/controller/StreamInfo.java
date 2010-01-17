package gralej.controller;

/**
 * Class holding meta information about some input stream
 * 
 * @author Niels
 * @version $Id$
 */
public class StreamInfo {
    
    public final static StreamInfo GRISU = new StreamInfo("grisu");
    public final static StreamInfo GRALEJ_SIMPLE = new StreamInfo("gralej-simple");

    private String type;
    private String name;
    private String charsetName;

    public StreamInfo(String type, String name) {
        this.type = new String(type);
        this.name = new String(name);
        charsetName = null;
    }

    public StreamInfo(String type) {
        this.type = new String(type);
        this.name = null;
        charsetName = null;
    }

    /**
     * Attenzione! this may return null.
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = new String(name);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = new String(type);
    }

    public String toString() {

        String cs = "";
        if (charsetName != null) {
            cs = "@" + cs;
        }

        if (name == null) {
            return type + cs + "_(no name)";
        }

        return type + cs + "_(" + name + ")";

    }

    /**
     * @return the charset name for this stream, or null
     */
    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

}
