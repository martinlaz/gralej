package gralej;

import java.awt.Cursor;

/**
 *
 * @author Martin
 */
public class Globals {
    public static final int VERSION_MAJOR = 0;
    public static final int VERSION_MINOR = 5;
    public static final int VERSION_REVISION = 0;
    
    public static final String VERSION_STRING =
            VERSION_MAJOR + "." + VERSION_MINOR + "." + VERSION_REVISION;
    
    public static final String APP_NAME = "Gralej";
    public static final String APP_NAME_VER = 
            APP_NAME + " (" + VERSION_STRING + ")";
    
    public static final Cursor HAND_CURSOR =
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
}
