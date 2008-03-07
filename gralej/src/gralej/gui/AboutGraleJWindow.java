package gralej.gui;

import gralej.util.Log;

import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * About window, singleton instance
 */
class AboutGraleJWindow extends JDialog implements HyperlinkListener {

	private static final long serialVersionUID = 2993718642470952363L;


	public AboutGraleJWindow(JFrame parent) {
        super(parent, true);
        setTitle("About GraleJ");
        setLocationRelativeTo(parent);
        
        // get icon theme
        /*GralePreferences prefs = GralePreferences.getInstance();
        IconTheme icontheme = IconThemeFactory.getIconTheme(prefs
                .get("gui.l+f.icontheme"));

        JButton OKbtn = new JButton(
        		"OK", icontheme.getIcon("ok"));*/

        JEditorPane editorPane = new JEditorPane();
        JScrollPane scrollPane = new JScrollPane(editorPane);
        
        
        editorPane.setEditable(false);
        java.net.URL aboutfile = getClass().getResource(
                "/gralej/resource/about.html");
        if (aboutfile != null) {
            try {
                editorPane.setPage(aboutfile);
            } catch (IOException e) {
                Log.warning(
                        "Attempted to read a bad URL: " + aboutfile);
            }
        } else {
            Log.warning("Couldn't find about.html.");
        }

        editorPane.setPreferredSize(new Dimension(350, 350));
        editorPane.setMinimumSize(new Dimension(10, 10));
        editorPane.addHyperlinkListener(this);
        add(scrollPane);
        //add(OKbtn);
        pack();
    }

    void showWindow() {
        setVisible(true);
    }

    
    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                Desktop.getDesktop().browse(event.getURL().toURI());
            }
            catch (UnsupportedOperationException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Browsing is not supported on the current platform.\n" +
                            "The URL of the clicked link is:\n" + event.getURL(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                        );
            }
            catch (Exception e) {
            	// throw any exception to console and at the user.
                e.printStackTrace();
                Log.error(e.getMessage());
            }
        }
    }
}

