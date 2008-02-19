package gralej.gui;

import gralej.controller.Controller;
import gralej.controller.StreamInfo;
import gralej.gui.icons.IconTheme;
import gralej.gui.icons.IconThemeFactory;
import gralej.parsers.OutputFormatter;
import gralej.prefs.GralePreferences;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

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
    
    private final JFrame frame;

    private Controller c; // the gralej.controller
    
    private GralePreferences gp;

    private JMenu saveallmenu;
    
    private JMenuItem m_Exit, m_Close, m_CloseAll, m_Open, m_About, m_Pref,
            m_Cascade, m_Tile, m_TestFile, m_WebTrale, m_Save, m_SaveAll,
            m_SaveAllXML, m_Server;

    private JToolBar toolbar;
    private JButton b_Open, b_Close, b_CloseAll, b_Save;
    private JCheckBoxMenuItem m_AutoOpenWindows, m_ShowToolBar, m_ShowStatusBar;

    StatusBar statusbar;
    
    private JMenuBar createMenuBar() {
        // menu
        JMenuBar menubar = new JMenuBar();
        // menu "File"
        JMenu filemenu = new JMenu("File");
        filemenu.setMnemonic('F');
        
        m_Open = new JMenuItem("Open");
        m_Open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                InputEvent.CTRL_DOWN_MASK));
        m_Open.addActionListener(this);
        filemenu.add(m_Open);

        m_TestFile = new JMenuItem("Open Sample");
        m_TestFile.addActionListener(this);
//        m_TestFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
//                InputEvent.CTRL_DOWN_MASK));
        filemenu.add(m_TestFile);

        m_Save = new JMenuItem("Save");
        m_Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_DOWN_MASK));
        m_Save.addActionListener(this);
        filemenu.add(m_Save);

        saveallmenu = new JMenu("Save All");
        
        m_SaveAll = new JMenuItem("TRALE format");
        m_SaveAll.addActionListener(this);
        saveallmenu.add(m_SaveAll);

        m_SaveAllXML = new JMenuItem("XML");
        m_SaveAllXML.addActionListener(this);
        saveallmenu.add(m_SaveAllXML);
        
        filemenu.add(saveallmenu);
        
        m_Close = new JMenuItem("Close");
        m_Close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                InputEvent.CTRL_DOWN_MASK));
        m_Close.addActionListener(this);
        filemenu.add(m_Close);

        m_CloseAll = new JMenuItem("Close All");
        m_CloseAll.addActionListener(this);
        filemenu.add(m_CloseAll);
        
        filemenu.addSeparator();

        m_Exit = new JMenuItem("Exit");
        m_Exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_DOWN_MASK));
        m_Exit.addActionListener(this);
        filemenu.add(m_Exit);
        menubar.add(filemenu);

        // menu "Connections"
        JMenu connectmenu = new JMenu("Connections");
        connectmenu.setMnemonic('C');
        
        m_Server = new JMenuItem();
        m_Server.addActionListener(this);
        connectmenu.add(m_Server);
        
        m_WebTrale = new JMenuItem("Open WebTrale Client");
        m_WebTrale.addActionListener(this);
        connectmenu.add(m_WebTrale);

        menubar.add(connectmenu);

        // menu "View"
        JMenu viewmenu = new JMenu("View");
        viewmenu.setMnemonic('V');
        
        m_Cascade = new JMenuItem("Cascade Windows");
        m_Cascade.addActionListener(this);
        viewmenu.add(m_Cascade);

        m_Tile = new JMenuItem("Tile Windows");
        m_Tile.addActionListener(this);
        viewmenu.add(m_Tile);

        viewmenu.addSeparator();
        
        m_AutoOpenWindows = new JCheckBoxMenuItem("Automatically Open Windows");
        m_AutoOpenWindows.addActionListener(this);
        m_AutoOpenWindows.setState(gp.getBoolean("behavior.openonload"));
        viewmenu.add(m_AutoOpenWindows);
        
        viewmenu.addSeparator();

        m_ShowToolBar = new JCheckBoxMenuItem("Show toolbar");
        m_ShowToolBar.addActionListener(this);
        m_ShowToolBar.setState(gp.getBoolean("behavior.showtoolbar"));
        viewmenu.add(m_ShowToolBar);

        m_ShowStatusBar = new JCheckBoxMenuItem("Show status bar");
        m_ShowStatusBar.addActionListener(this);
        m_ShowStatusBar.setState(gp.getBoolean("behavior.showstatusbar"));
        viewmenu.add(m_ShowStatusBar);

        viewmenu.addSeparator();

        m_Pref = new JMenuItem("Preferences");
        m_Pref.setAccelerator(KeyStroke.getKeyStroke("F2"));
        m_Pref.addActionListener(this);
        viewmenu.add(m_Pref);

        menubar.add(viewmenu);

        // menu "Help"
        JMenu helpmenu = new JMenu("Help");
        viewmenu.setMnemonic('H');
        
        m_About = new JMenuItem("About GraleJ");
        m_About.addActionListener(this);
        helpmenu.add(m_About);

        menubar.add(helpmenu);
        return menubar;
    }

    private void createToolBar() {
        toolbar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);

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
            while (true) {
                String surl = JOptionPane
                        .showInputDialog(null, "Choose server", gp.get("input.lastserver"));
                if (surl == null) 
                    break; // cancel
                try {
                    c.startWebTraleClient(new URL(surl));
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), ex.toString(), JOptionPane.ERROR_MESSAGE);
                    continue; // try again
                }
                try {
                    gp.put("input.lastserver", surl);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
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
        } else if (source == m_SaveAll) {
            File f = saveDialog(OutputFormatter.TRALEFormat);
            if (f != null) {
                c.getModel().saveAll(f, OutputFormatter.TRALEFormat);
            } else {
                // file could not be opened. doing nothing might be appropriate
            }
        } else if (source == m_SaveAllXML) {
            File f = saveDialog(OutputFormatter.XMLFormat);
            if (f != null) {
                c.getModel().saveAll(f, OutputFormatter.XMLFormat);
            } else {
                // file could not be opened. doing nothing might be appropriate
            }

            
         // VIEW MENU ITEMS
                     
        } else if (source == m_About) {
            AboutGraleJWindow w = AboutGraleJWindow.getInstance();
            w.showWindow();
        } else if (source == m_Cascade) {
            c.getModel().cascade();
        } else if (source == m_Tile) {
            c.getModel().tile();
        } else if (source == m_ShowToolBar) {
            gp.putBoolean("behavior.showtoolbar", m_ShowToolBar.getState());
            toolbar.setVisible(m_ShowToolBar.getState());
            
        } else if (source == m_ShowStatusBar) {
            gp.putBoolean("behavior.showstatusbar", m_ShowStatusBar.getState());
            statusbar.setVisible(m_ShowStatusBar.getState());
            
        } else if (source == m_Pref) {
            //GenDialog frame = new GenDialog(null);
            //frame.setVisible(true);
        
        } else if (source == m_Server) {
            // maybe not the best way to store the information
            if (m_Server.getText().equals("Start Server")) {
                c.startServer();
            } else {
                c.stopServer();
            }
        } else if (source == m_AutoOpenWindows) {
            gp.putBoolean("behavior.openonload", m_AutoOpenWindows.getState());
        }
    }
    
    static class AboutGraleJWindow extends JFrame implements HyperlinkListener {
        
        private static AboutGraleJWindow instance;
        
        static AboutGraleJWindow getInstance () {
            if (instance == null) instance = new AboutGraleJWindow();
            return instance;
        }
        
        private AboutGraleJWindow () {
            super("About GraleJ");

            // load info text from HTML file
            JEditorPane editorPane = new JEditorPane();

            editorPane.setEditable(false);
            java.net.URL aboutfile = getClass().getResource(
                    "/gralej/resource/about.html");
            if (aboutfile != null) {
                try {
                    editorPane.setPage(aboutfile);
                } catch (IOException e) {
                    System.err.println("Attempted to read a bad URL: " + aboutfile);
                }
            } else {
                System.err.println("Couldn't find about.html.");
            }

            editorPane.setPreferredSize(new Dimension(250, 145));
            editorPane.setMinimumSize(new Dimension(10, 10));
            editorPane.addHyperlinkListener(this);
            add(editorPane);
            pack();
            setLocationByPlatform(true);
        }
        
        void showWindow() {
            instance.setVisible(true);
            instance.pack();
        }

        
        public void hyperlinkUpdate(HyperlinkEvent event) {
            // TODO Auto-generated method stub
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Runtime.getRuntime().exec("cmd /c start "+event.getURL());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
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
            
            if (!f.getName().toLowerCase().endsWith(c.getModel().getOutputFormatter()
                    .getExtension(format))) { 
               f = new File(f.getPath() + "." + c.getModel().getOutputFormatter()
                       .getExtension(format));
            } 


            if (!f.exists() || JOptionPane.showConfirmDialog(null,
                    "File "+f.getName()+" exists. Overwrite?", "Overwrite?",
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
        /*
        // look and feel moved to Main.java by Niels
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        
        theme = IconThemeFactory.getIconTheme(gp.get("gui.l+f.icontheme"));

        frame = new JFrame("GraleJ");
        frame.setIconImage(theme.getIcon("grale").getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // instantiate menus
        frame.setJMenuBar(this.createMenuBar());

        // instantiate toolbar
        createToolBar();
        toolbar.setVisible(gp.getBoolean("behavior.showtoolbar"));
        frame.getContentPane().add(toolbar, BorderLayout.NORTH);

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

        statusbar = new StatusBar();
        frame.getContentPane().add(statusbar, BorderLayout.PAGE_END);

        notifyOfSelection(false);
        notifyOfListElements(0);

        frame.pack();
        frame.setSize(gp.getInt("gui.windows.main.size.width"), 
                      gp.getInt("gui.windows.main.size.height"));
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent we) {
                try {
                    gp.putInt("gui.windows.main.size.width", frame.getWidth());
                    gp.putInt("gui.windows.main.size.height", frame.getHeight());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        
        frame.setLocationByPlatform(true);

        frame.setVisible(true);
    }
    
    class StatusBar extends JPanel {
        // TODO implement contents of the bottom status line
        
        JLabel counter;
        JLabel connectionInfo;
        
        StatusBar () {
            super();
            counter = new JLabel("0");
            add(counter, BorderLayout.WEST);
            connectionInfo = new JLabel();
            add(connectionInfo, BorderLayout.EAST);
            setVisible(gp.getBoolean("behavior.showstatusbar"));
        }
        
        void setNumberOfItems (int i) {
            counter.setText(""+i+" data items; ");            
        }
        
        void setConnectionInfo (String info) {
            connectionInfo.setText(info);            
        }



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
    public void notifyOfListElements(int number) {
        boolean isEmpty = (number == 0);
        saveallmenu.setEnabled(!isEmpty);
        m_CloseAll.setEnabled(!isEmpty);
        // b_SaveAll.setEnabled(!isEmpty);
        
        statusbar.setNumberOfItems(number);
    }
    
    public void notifyOfServerConnection(boolean isConnected) {
        if (isConnected) {
            m_Server.setText("Stop Server");
            statusbar.setConnectionInfo("connected");
        } else {
            m_Server.setText("Start Server");
            statusbar.setConnectionInfo("not connected");
        }
    }
    
    public void raiseMainWindow () {
        frame.requestFocus();
    }

}
