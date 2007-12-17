package gralej.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;

import gralej.controller.*;
import gralej.gui.icons.IconTheme;
import gralej.gui.icons.IconThemeFactory;
import gralej.gui.prefsdialog.GenDialog;
import gralej.parsers.OutputFormatter;
import gralej.prefs.GralePreferences;
import javax.swing.ImageIcon;

/**
 * 
 * @author Armin
 * @version 
 */
public class MainGUI implements ActionListener, ItemListener {

    // display mode
    static int FRAMES = 0;
    static int WINDOWS = 1;
    // private int mode = WINDOWS;
    // private int mode = FRAMES;

    private final IconTheme theme;

    private Controller c; // the gralej.controller
    
    private GralePreferences gp;

    private JMenuItem m_Exit, m_Close, m_CloseAll, m_Open, m_About, m_Pref,
            m_Cascade, m_Tile, m_TestFile, m_WebTrale, m_Save, m_SaveAll;

    private JButton b_Open, b_Close, b_CloseAll, b_Save, b_SaveAll;

    private JMenuBar createMenuBar() {
        // menu
        JMenuBar menubar = new JMenuBar();
        // menu "File"
        JMenu filemenu = new JMenu("File");
        filemenu.add(new JSeparator());

        m_Open = new JMenuItem("Open");
        m_Open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                InputEvent.CTRL_DOWN_MASK));
        m_Open.addActionListener(this);
        filemenu.add(m_Open);

        m_TestFile = new JMenuItem("Open Sample");
        m_TestFile.addActionListener(this);
        filemenu.add(m_TestFile);

        m_Close = new JMenuItem("Close");
        m_Close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                InputEvent.CTRL_DOWN_MASK));
        m_Close.addActionListener(this);
        filemenu.add(m_Close);

        m_CloseAll = new JMenuItem("Close All");
        m_CloseAll.addActionListener(this);
        filemenu.add(m_CloseAll);

        m_Save = new JMenuItem("Save");
        m_Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_DOWN_MASK));
        m_Save.addActionListener(this);
        filemenu.add(m_Save);

        m_SaveAll = new JMenuItem("Save All");
        m_SaveAll.addActionListener(this);
        filemenu.add(m_SaveAll);

        filemenu.add(new JSeparator());

        JMenu connectSubmenu = new JMenu("Connections");

        m_WebTrale = new JMenuItem("Open WebTrale client");
        m_WebTrale.addActionListener(this);
        connectSubmenu.add(m_WebTrale);

        filemenu.add(connectSubmenu);

        m_Exit = new JMenuItem("Exit");
        m_Exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                InputEvent.CTRL_DOWN_MASK));
        m_Exit.addActionListener(this);
        filemenu.add(m_Exit);
        menubar.add(filemenu);

        // menu "View"
        JMenu viewmenu = new JMenu("View");

        m_Cascade = new JMenuItem("Cascade Windows/Frames");
        m_Cascade.addActionListener(this);
        viewmenu.add(m_Cascade);

        m_Tile = new JMenuItem("Tile Windows/Frames");
        m_Tile.addActionListener(this);
        viewmenu.add(m_Tile);

        viewmenu.addSeparator();

        m_Pref = new JMenuItem("Preferences");
        m_Pref.setAccelerator(KeyStroke.getKeyStroke("F2"));
        m_Pref.addActionListener(this);
        viewmenu.add(m_Pref);

        m_About = new JMenuItem("About GRALE");
        m_About.addActionListener(this);
        viewmenu.add(m_About);

        menubar.add(viewmenu);
        return menubar;
    }

    private JToolBar createToolBar() {
        JToolBar toolbar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);

        b_Open = new JButton(theme.getIcon("fileopen"));
        b_Open.addActionListener(this);
        b_Open.setToolTipText("Open");
        toolbar.add(b_Open);

        b_Save = new JButton(theme.getIcon("filefloppy"));
        b_Save.addActionListener(this);
        b_Save.setToolTipText("Save");
        toolbar.add(b_Save);

        b_Close = new JButton(theme.getIcon("closewindow"));
        b_Close.addActionListener(this);
        b_Close.setToolTipText("Close");
        toolbar.add(b_Close);

        b_CloseAll = new JButton(theme.getIcon("closeviews"));
        b_CloseAll.addActionListener(this);
        b_CloseAll.setToolTipText("Close All");
        toolbar.add(b_CloseAll);

        /*
         * b_SaveAll = new JButton(theme.getIcon("filefloppy"));
         * b_SaveAll.addActionListener(this); toolbar.add(b_SaveAll);
         */

        return toolbar;
    }

    // User actions broadcast Events. Depending on the source, they're
    // ActionEvents (menu item) or ItemEvents (checkbox)
    public void actionPerformed(ActionEvent e) {
        JComponent source = (JComponent) (e.getSource());
        if (source == m_Exit) {
            System.exit(0);
            
// OPEN
            
        } else if (source == m_Open || source == b_Open) {
            JFileChooser fc = new JFileChooser(gp.get("input.lastdir"));
            fc.setMultiSelectionEnabled(true);
            int returnVal = fc.showOpenDialog(source);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] files = fc.getSelectedFiles();
                for (File f : files) {
                    /* TODO if more than one file contains more than one data
                     * item, they're opened not in order by file (threading)
                     * 
                     */
                    c.open(f);
                }
                try {
                    gp.put("input.lastdir", files[0].getCanonicalPath());
                } catch (IOException e1) {
                    System.err
                            .println("Getting the directory of the (first) chosen file failed.");
                    e1.printStackTrace();
                }
            } else {
                // file could not be opened. doing nothing might be appropriate
            }
            
// OPEN SAMPLE FILES
            
        } else if (source == m_TestFile) {
            final String resName = "/gralej/resource/sample.GRALE";
            InputStream is = getClass().getResourceAsStream(resName);
            if (is == null) // should never happen
                throw new RuntimeException("Internal program error");
            c.newStream(is, new StreamInfo("grisu", resName));
            
         // CONNECT TO WEB SERVER
                     
        } else if (source == m_WebTrale) {
            URL url;
            try {
                url = new URL(JOptionPane
                        .showInputDialog(null, "Choose server", gp.get("input.lastserver")));
                c.startWebTraleClient(url);
                gp.put("input.lastserver", url.toString());
            } catch (HeadlessException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (MalformedURLException e1) {
                System.err.println("Malformed URL");
            }
            
         // CLOSE
                     
        } else if (source == m_Close || source == b_Close) {
            c.getModel().close();
        } else if (source == m_CloseAll || source == b_CloseAll) {
            c.getModel().closeAll();
            
         // SAVE
                     
        } else if (source == m_Save || source == b_Save) {
            File f = saveDialog(OutputFormatter.TRALEFormat);
            if (f != null) {
                c.getModel().save(f, OutputFormatter.TRALEFormat);
            }
        } else if (source == m_SaveAll || source == b_SaveAll) {
            File f = saveDialog(OutputFormatter.TRALEFormat);
            if (f != null) {
                c.getModel().saveAll(f, OutputFormatter.TRALEFormat);
            } else {
                // file could not be opened. doing nothing might be appropriate
            }
            
         // VIEW MENU ITEMS
                     
        } else if (source == m_About) {
            JOptionPane.showMessageDialog(null, "GraleJ (2007).");
            // TODO write useful text here
        } else if (source == m_Cascade) {
            c.getModel().cascade();
        } else if (source == m_Tile) {
            c.getModel().tile();
        } else if (source == m_Pref) {
            GenDialog frame = new GenDialog(null);
            frame.setVisible(true);
        	new GenDialog(null).setVisible(true);
        }
    }

    public File saveDialog(int format) {
        JFileChooser fc = new JFileChooser(gp.get("input.lastdir"));
        fc.setMultiSelectionEnabled(false);
        // fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(c.getModel().getOutputFormatter()
                .getFilter(format));
        int returnVal = fc.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                gp.put("input.lastdir", f.getCanonicalPath());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if (!f.exists() || JOptionPane.showConfirmDialog(null,
                    "File exists. Overwrite?", "Overwrite?",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                return f;
            }
        }
        return null;
    }
    
    public void itemStateChanged(ItemEvent e) {
        // JMenuItem source = (JMenuItem)(e.getSource());
    }

    /**
     * 
     */
    public MainGUI(Controller c) {
        this.c = c;
        gp = GralePreferences.getInstance();

        // first: style
        // TODO parameterize
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        theme = IconThemeFactory.getIconTheme(gp.get("gui.l+f.icontheme"));

        JFrame frame = new JFrame("GraleJ");
        frame.setIconImage(theme.getIcon("grale").getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // instantiate menus
        frame.setJMenuBar(this.createMenuBar());

        // instantiate toolbar
        frame.getContentPane().add(this.createToolBar(), BorderLayout.NORTH);
        notifyOfSelection(false);
        notifyOfEmptyList(true);

        // list observer
        ContentObserver list = new ListContentObserver(c.getModel(), this);

        /*
         * if (mode == FRAMES) {
         * content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
         * content.setOneTouchExpandable(true); 
         * content.setDividerLocation(150);
         * content.setPreferredSize(new Dimension(500, 250));
         * frame.getContentPane().add(content, BorderLayout.CENTER);
         * content.add(list.getDisplay());
         * ContentObserver frames = new FramesContentObserver(c.getModel());
         * content.add(frames.getDisplay()); 
         * 
         * } else if (mode == WINDOWS) {
         */
        new WindowsContentObserver(c.getModel(), theme, this);
        frame.getContentPane().add(list.getDisplay(), BorderLayout.CENTER);
        // }

        // TODO implement contents of the bottom status line
        JPanel statusLine = new JPanel();
        statusLine.add(new JLabel("status line"));
        // frame.getContentPane().add(statusLine, BorderLayout.PAGE_END);

        frame.pack();
        frame.setSize(gp.getInt("gui.windows.main.size.width"), 
                      gp.getInt("gui.windows.main.size.height"));

        frame.setVisible(true);

    }

    /**
     * Some menu items and buttons depend on a file being selected.
     * This method is called by the list whenever the selection changes.
     * 
     * @param b: whether a list item is selected
     */
    public void notifyOfSelection(boolean b) {
        m_Close.setEnabled(b);
        m_Save.setEnabled(b);
        b_Close.setEnabled(b);
        b_Save.setEnabled(b);
    }

    /**
     * Other menu items depend on the non-emptiness of the list.
     * This method shows or hides them, and it's the list's
     * responsibility to notify the GUI accordingly.
     * 
     * @param isEmpty
     */
    public void notifyOfEmptyList(boolean isEmpty) {
        m_SaveAll.setEnabled(!isEmpty);
        m_CloseAll.setEnabled(!isEmpty);
        // b_SaveAll.setEnabled(!isEmpty);
    }

}
