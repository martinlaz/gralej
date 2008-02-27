package gralej.error;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import gralej.prefs.GralePreferences;

/**
 * The ErrorHandler offers a single instance (much like the GralePreferences).
 * It reports errors depending on their badness to STDOUT, a pop-up or to a
 * logfile.
 * 
 * 
 * 
 * 
 * @author Armin
 * 
 */

public class ErrorHandler {

    // ways of reporting
    public final static int AsPopUp = 0;
    public final static int ToSTDOUT = 1;
    public final static int ToFile = 2;
    public final static int Ignore = 3;

    public final static int NOTE = 0; // such as "Stream closed"
    public final static int WARNING = 1; // failed loads etc.
    public final static int ERROR = 2; // wrong formats
    public final static int CRITICAL = 3; // probably too late then
    public final static int DEBUG = 4; // debug messages

    private int[] mapping = new int[5];

    private String file;

    private static ErrorHandler instance = null;

    public static ErrorHandler getInstance() {
        instance = new ErrorHandler();
        return instance;
    }

    private ErrorHandler() {
        // read way from prefs
        GralePreferences gp = GralePreferences.getInstance();
        mapping[NOTE] = gp.getInt("error.note");
        mapping[WARNING] = gp.getInt("error.warning");
        mapping[ERROR] = gp.getInt("error.error");
        mapping[CRITICAL] = gp.getInt("error.critical");
        mapping[DEBUG] = gp.getInt("error.debug");

        file = gp.get("error.file");
    }

    /**
     * 
     * @param error message
     * @param badness
     */
    public void report(String error, int badness) {
        switch (mapping[badness]) {
        case AsPopUp:
            int message_type = JOptionPane.PLAIN_MESSAGE;
            String title = "";
            switch (badness) {
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
        case ToSTDOUT:
            System.err.println(error);
            break;
        // what about System.out.? another case destinction?
        case ToFile:
            PrintStream p;
            try {
                p = new PrintStream(new FileOutputStream(file));
                p.print(error);
                p.close();
            } catch (FileNotFoundException e) {
//                report("Logfile " + file + " cannot be accessed", ERROR);
                System.err.println("Logfile " + file + " cannot be accessed.");
            }
            break;
        }

    }

}
