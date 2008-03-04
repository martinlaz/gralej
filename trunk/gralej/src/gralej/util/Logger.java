package gralej.util;

import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import gralej.prefs.GralePreferences;
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

    private static Logger instance = null;

    public static Logger getInstance() {
        if (instance == null)
            instance = new Logger();
        return instance;
    }

    private Logger() {
        // read way from prefs
        GralePreferences gp = GralePreferences.getInstance();
        mapping[Severity.INFO.ordinal()] = Enum.valueOf(Method.class, gp.get("message.info").toUpperCase());
        mapping[Severity.WARNING.ordinal()] = Enum.valueOf(Method.class, gp.get("message.warning").toUpperCase());
        mapping[Severity.ERROR.ordinal()] = Enum.valueOf(Method.class, gp.get("message.error").toUpperCase());
        mapping[Severity.CRITICAL.ordinal()] = Enum.valueOf(Method.class, gp.get("message.critical").toUpperCase());
        mapping[Severity.DEBUG.ordinal()] = Enum.valueOf(Method.class, gp.get("message.debug").toUpperCase());
        
        String filename = gp.get("logger.file");

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
    public void log(String error, Severity severity) {
        switch (mapping[severity.ordinal()]) {
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
        getInstance().log(Arrays.concat(" ", msgs), Severity.CRITICAL);
    }
    
    public static void error(Object... msgs) {
        getInstance().log(Arrays.concat(" ", msgs), Severity.ERROR);
    }
    
    public static void warning(Object... msgs) {
        getInstance().log(Arrays.concat(" ", msgs), Severity.WARNING);
    }
    
    public static void info(Object... msgs) {
        getInstance().log(Arrays.concat(" ", msgs), Severity.INFO);
    }
    
    public static void debug(Object... msgs) {
        getInstance().log(Arrays.concat(" ", msgs), Severity.DEBUG);
    }
}
