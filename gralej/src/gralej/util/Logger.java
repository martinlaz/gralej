package gralej.util;

import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import gralej.prefs.GralePreferences;
import gralej.util.Arrays;
import gralej.util.Enums;
import java.io.IOException;

/**
 * The Logger offers a single instance (much like the GralePreferences).
 * It reports errors depending on their badness to STDOUT, a pop-up or to a
 * logfile.
 * 
 * 
 * 
 * 
 * @author Armin
 * 
 */

public class Logger {

    // ways of reporting
    public final static int AsPopUp = 0;
    public final static int ToSTDERR = 1;
    public final static int ToFile = 2;
    public final static int Ignore = 3;

    public final static int NOTE = 0; // such as "Stream closed"
    public final static int WARNING = 1; // failed loads etc.
    public final static int ERROR = 2; // wrong formats
    public final static int CRITICAL = 3; // probably too late then
    public final static int DEBUG = 4; // debug messages

    private int[] mapping = new int[5];
    private Enums errorType =
            new Enums("INFO", "WARNING", "ERROR", "CRITICAL", "DEBUG");

    private String file;

    private static Logger instance = null;

    public static Logger getInstance() {
        if (instance == null)
            instance = new Logger();
        return instance;
    }

    private Logger() {
        // read way from prefs
        GralePreferences gp = GralePreferences.getInstance();
        Enums method = new Enums("popup", "stderr", "file", "ignore");
        mapping[NOTE]       = method.decode(gp.get("error.note"));
        mapping[WARNING]    = method.decode(gp.get("error.warning"));
        mapping[ERROR]      = method.decode(gp.get("error.error"));
        mapping[CRITICAL]   = method.decode(gp.get("error.critical"));
        mapping[DEBUG]      = method.decode(gp.get("error.debug"));

        file = gp.get("error.file");
    }

    /**
     * 
     * @param error message
     * @param severity
     */
    public void report(String error, int severity) {
        switch (mapping[severity]) {
        case AsPopUp:
            int message_type = JOptionPane.PLAIN_MESSAGE;
            String title = "";
            switch (severity) {
            case NOTE:
                message_type = JOptionPane.INFORMATION_MESSAGE;
                title = "Notification";
                break;
            case WARNING:
                message_type = JOptionPane.WARNING_MESSAGE;
                title = "Warning";
                break;
            case ERROR:
                message_type = JOptionPane.ERROR_MESSAGE;
                title = "Error";
                break;
            case CRITICAL:
                message_type = JOptionPane.ERROR_MESSAGE;
                title = "Critical error";
                break;
            case DEBUG:
                message_type = JOptionPane.PLAIN_MESSAGE;
                title = "Debugging info";
                break;
            }
            JOptionPane.showMessageDialog(null, error, title, message_type);
            break;
        case ToSTDERR:
            System.err.println(errorType.toString(severity) + ": " + error);
            break;
        // what about System.out.? another case destinction?
        case ToFile:
            PrintStream p;
            try {
                p = new PrintStream(new FileOutputStream(file));
                p.print(errorType.toString(severity) + ": " + error);
                p.close();
            } catch (IOException e) {
                System.err.println("WARNING: logfile " + file + " cannot be accessed.");
                System.err.println(errorType.toString(severity) + ": " + error);
            }
            break;
        }
    }
    
    public static void critical(Object... msgs) {
        getInstance().report(Arrays.concat(" ", msgs), CRITICAL);
    }
    
    public static void error(Object... msgs) {
        getInstance().report(Arrays.concat(" ", msgs), ERROR);
    }
    
    public static void warning(Object... msgs) {
        getInstance().report(Arrays.concat(" ", msgs), WARNING);
    }
    
    public static void info(Object... msgs) {
        getInstance().report(Arrays.concat(" ", msgs), NOTE);
    }
    
    public static void debug(Object... msgs) {
        getInstance().report(Arrays.concat(" ", msgs), DEBUG);
    }
}
