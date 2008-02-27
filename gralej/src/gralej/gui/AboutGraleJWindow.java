package gralej.gui;

import gralej.error.ErrorHandler;

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * About window, singleton instance
 */
class AboutGraleJWindow extends JDialog implements HyperlinkListener {

    public AboutGraleJWindow(JFrame parent) {
        super(parent, true);
        setTitle("About GraleJ");
        setLocationRelativeTo(parent);

        JEditorPane editorPane = new JEditorPane();

        editorPane.setEditable(false);
        java.net.URL aboutfile = getClass().getResource(
                "/gralej/resource/about.html");
        if (aboutfile != null) {
            try {
                editorPane.setPage(aboutfile);
            } catch (IOException e) {
                ErrorHandler.getInstance().report(
                        "Attempted to read a bad URL: " + aboutfile,
                        ErrorHandler.WARNING);
            }
        } else {
            ErrorHandler.getInstance().report("Couldn't find about.html.",
                    ErrorHandler.WARNING);
        }

        editorPane.setPreferredSize(new Dimension(250, 145));
        editorPane.setMinimumSize(new Dimension(10, 10));
        editorPane.addHyperlinkListener(this);
        add(editorPane);
        pack();
    }

    void showWindow() {
        setVisible(true);
    }

    
    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                Desktop.getDesktop().browse(event.getURL().toURI());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (URISyntaxException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}

