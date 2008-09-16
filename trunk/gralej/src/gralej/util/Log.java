// $Id$

package gralej.util;

import gralej.prefs.GPrefsChangeListener;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import gralej.prefs.GralePreferences;
import java.io.IOException;

/**
 * The Log offers a single instance (much like the GralePreferences).
 * It reports errors depending on their badness to STDOUT, a pop-up or to a
 * logfile.
 * 
 * 
 * 
 * 
 * @author Armin
 * 
 */

public class Log {

    // ways of reporting
    public static enum Method {
        POPUP, STDERR, FILE, IGNORE
    }

    public static enum Severity {
        INFO,       // such as "Stream closed"
        WARNING,    // failed loads etc.
        ERROR,      // wrong formats
        CRITICAL,   // probably too late then
        DEBUG       // debug messages
           // debug messages
    }

    // Severity -> Method
    private Method[] mapping = new Method[5];
    private PrintStream out;

    private static Log instance = null;

    public static Log getInstance() {
        if (instance == null)
            instance = new Log();
        return instance;
    }
    
    static {
        final String keyPrefix = "log.";
        GralePreferences.getInstance().addListener(new GPrefsChangeListener() {
                public void preferencesChange() {
                    instance = null;
                }
            },
            keyPrefix);
    }

    private Log() {
        // read way from prefs
        GralePreferences gp = GralePreferences.getInstance();
        mapping[Severity.INFO.ordinal()] = Method.valueOf(gp.get("log.message.info").toUpperCase());
        mapping[Severity.WARNING.ordinal()] = Method.valueOf(gp.get("log.message.warning").toUpperCase());
        mapping[Severity.ERROR.ordinal()] = Method.valueOf(gp.get("log.message.error").toUpperCase());
        mapping[Severity.CRITICAL.ordinal()] = Method.valueOf(gp.get("log.message.critical").toUpperCase());
        mapping[Severity.DEBUG.ordinal()] = Method.valueOf(gp.get("log.message.debug").toUpperCase());
        
        String filename = gp.get("log.file");

        for (Method m : mapping) {
            if (m == Method.FILE) {
                try {
                    out = new PrintStream(new FileOutputStream(filename), true);
                } catch (IOException e) {
                    System.err.println("WARNING: logfile " + filename + " cannot be opened. " +
                            "Will log to STDERR instead.");
                    out = System.err;
                }
                break;
            }
        }
    }
    
    
    /**
     * @param error message
     * @param severity
     */
    public void log(Severity severity, Object... msgs) {
        Method method = mapping[severity.ordinal()];
        if (method == Method.IGNORE)
            return;
        
        String error = Arrays.concat(" ", msgs);
        
        switch (method) {
        case POPUP:
            int message_type = JOptionPane.PLAIN_MESSAGE;
            String title = "";
            switch (severity) {
            case INFO:
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
        case STDERR:
            System.err.println(severity.name() + ": " + error);
            break;
        case FILE:
            out.println(severity.name() + ": " + error);
            break;
        }
    }
    
    public static void critical(Object... msgs) {
        getInstance().log(Severity.CRITICAL, msgs);
    }
    
    public static void error(Object... msgs) {
        getInstance().log(Severity.ERROR, msgs);
    }
    
    public static void warning(Object... msgs) {
        getInstance().log(Severity.WARNING, msgs);
    }
    
    public static void info(Object... msgs) {
        getInstance().log(Severity.INFO, msgs);
    }
    
    public static void debug(Object... msgs) {
        getInstance().log(Severity.DEBUG, msgs);
    }
}
