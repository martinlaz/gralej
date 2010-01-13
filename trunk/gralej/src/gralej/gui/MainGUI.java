package gralej.gui;

import gralej.Config;
import gralej.Globals;
import gralej.blocks.configurator.BlockConfiguratorDialog;
import gralej.controller.Controller;
import gralej.controller.StreamInfo;
import gralej.util.Log;
import gralej.gui.icons.IconTheme;
import gralej.gui.icons.IconThemeFactory;
import gralej.parsers.OutputFormatter;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
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
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * @author Armin
 * @version $Id$
 */
public class MainGUI implements ActionListener, ItemListener {
    private Config cfg;

    private final IconTheme theme;

    private static JFrame frame;

    private Controller c;

    private JMenu saveallmenu;

    private JMenuItem m_Quit, m_Close, m_CloseAll, m_Open, m_OpenURL, m_About, m_Options,
            m_Cascade, m_Tile, m_TestFile, m_WebTrale, m_Save, m_SaveAll,
            m_SaveAllXML, m_Server;

    private JToolBar toolbar;
    private JButton b_Open, b_Close, b_CloseAll, b_Save;
    private JButton b_Options;
    private JCheckBoxMenuItem m_AutoOpenWindows, m_AutoExpandTags,
            m_ShowToolBar, m_ShowStatusBar;

    StatusBar statusbar;
    private static MainGUI lastInstance;
    
    public static JFrame getLastFrame() {
        return frame;
    }

    public static MainGUI getLastInstance() {
        return lastInstance;
    }

    public StatusBar getStatusBar() {
        return statusbar;
    }

    private JMenuBar createMenuBar(boolean useServer) {

        JMenuBar menubar = new JMenuBar();
        // menu "File"
        JMenu filemenu = new JMenu("File");
        filemenu.setMnemonic(KeyEvent.VK_F);

        m_Open = new JMenuItem("Open");
        m_Open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                InputEvent.CTRL_DOWN_MASK));
        m_Open.addActionListener(this);
        filemenu.add(m_Open);

        m_OpenURL = new JMenuItem("Open URL...");
        m_OpenURL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doOpenURL();
            }
        });
        filemenu.add(m_OpenURL);

        m_TestFile = new JMenuItem("Open Sample");
        m_TestFile.addActionListener(this);
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

        m_Quit = new JMenuItem("Quit");
        // NB: CTRL-X is caught by the system to mean "cut"
        // (with focus on certain elements)
        m_Quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_DOWN_MASK));
        m_Quit.addActionListener(this);
        filemenu.add(m_Quit);
        menubar.add(filemenu);

        // menu "View"
        JMenu viewmenu = new JMenu("View");
        viewmenu.setMnemonic(KeyEvent.VK_V);

        m_Cascade = new JMenuItem("Cascade Windows");
        m_Cascade.addActionListener(this);
        viewmenu.add(m_Cascade);

        m_Tile = new JMenuItem("Tile Windows");
        m_Tile.addActionListener(this);
        viewmenu.add(m_Tile);

        viewmenu.addSeparator();

        m_AutoOpenWindows = new JCheckBoxMenuItem("Auto-Open Windows");
        m_AutoOpenWindows.addActionListener(this);
        viewmenu.add(m_AutoOpenWindows);
        
        m_AutoExpandTags = new JCheckBoxMenuItem("Auto-Expand Tags");
        m_AutoExpandTags.addActionListener(this);
        viewmenu.add(m_AutoExpandTags);

        viewmenu.addSeparator();

        m_ShowToolBar = new JCheckBoxMenuItem("Toolbar");
        m_ShowToolBar.addActionListener(this);
        m_ShowToolBar.setState(cfg.getBool("behavior.showtoolbar"));
        viewmenu.add(m_ShowToolBar);

        m_ShowStatusBar = new JCheckBoxMenuItem("Status Bar");
        m_ShowStatusBar.addActionListener(this);
        m_ShowStatusBar.setState(cfg.getBool("behavior.showstatusbar"));
        viewmenu.add(m_ShowStatusBar);

        menubar.add(viewmenu);
        
        // menu "Tools"
        JMenu toolsmenu = new JMenu("Tools");
        toolsmenu.setMnemonic(KeyEvent.VK_T);

        if (useServer) {
            m_Server = new JMenuItem();
            m_Server.addActionListener(this);
            toolsmenu.add(m_Server);

            m_WebTrale = new JMenuItem("Open WebTrale Client");
            m_WebTrale.addActionListener(this);
            toolsmenu.add(m_WebTrale);

            toolsmenu.addSeparator();
        }
        
        JMenuItem bcm = new JMenuItem("AVM Tree View Configurator");
        bcm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BlockConfiguratorDialog d =
                        new BlockConfiguratorDialog(frame, true, cfg);
                d.setVisible(true);
                if (d.isOkayed())
                    cfg.save();
            }
        });
        
        toolsmenu.add(bcm);
        
        m_Options = new JMenuItem("Options...");
        m_Options.setAccelerator(KeyStroke.getKeyStroke("F2"));
        m_Options.addActionListener(this);
        toolsmenu.add(m_Options);

        menubar.add(toolsmenu);

        // menu "Help"
        JMenu helpmenu = new JMenu("Help");
        helpmenu.setMnemonic(KeyEvent.VK_H);

        m_About = new JMenuItem("About Gralej");
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
        
        b_Options = new JButton(theme.getIcon("configure"));
        b_Options.addActionListener(this);
        b_Options.setToolTipText("Options");
        toolbar.add(b_Options);

    }
    
    public void quit() {
        quit(false);
    }
    
    public void quit(boolean force) {
        try {
            if (!force && cfg.bool("behavior.confirm.exit")) {
                int response = JOptionPane.showConfirmDialog(
                        frame,
                        "Do you really want to quit " + Globals.APP_NAME + "?",
                        "Confirm Quit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (response != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            frame.dispose();
            cfg.set("gui.windows.main.size.width", ""+frame.getWidth(),false);
            cfg.set("gui.windows.main.size.height", ""+frame.getHeight(),false);
            cfg.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
    
    public void actionPerformed(ActionEvent e) {
        JComponent source = (JComponent) (e.getSource());
        if (source == m_Quit) {
            quit();
        } else if (source == m_Open || source == b_Open) {
            JFileChooser fc = new JFileChooser(cfg.get("input.lastdir"));
            fc.setMultiSelectionEnabled(true);
            int returnVal = fc.showOpenDialog(source);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File[] files = fc.getSelectedFiles();
                for (File f : files) {
                    /*
                     * if more than one file contains more than one data item,
                     * they're opened not in order by file (threading)
                     */
                    c.open(f);
                }
                try {
                    File f = files[0];
                    File dir = f.isDirectory() ? f : f.getParentFile();
                    cfg.set(
                            "input.lastdir", dir.getCanonicalPath(), false);
                } catch (IOException e1) {
                    Log.warning(
                            "Getting the directory of the (first) chosen file failed.");
                }
            } else {
                // file could not be opened. doing nothing might be appropriate
            }

            // OPEN SAMPLE FILES

        } else if (source == m_TestFile) {
            final String resName = "/gralej/resource/sample.GRALE";
            InputStream is = getClass().getResourceAsStream(resName);
            if (is == null) {// should never happen
                Log.critical("Initializing InputStream failed.");
                throw new RuntimeException("Internal program error");
            }
            c.newStream(is, new StreamInfo("grisu", resName));

            // CONNECT TO WEB SERVER

        } else if (source == m_WebTrale) {
            while (true) {
                String surl = JOptionPane.showInputDialog(frame,
                        "Choose server", cfg.get("input.lastserver"));
                if (surl == null)
                    break; // cancel
                try {
                    c.startWebTraleClient(new URL(surl));
                } catch (Exception ex) {
                    Log.error(ex.getMessage());
                    continue; // try again
                }
                try {
                    cfg.set("input.lastserver", surl, false);
                } catch (Exception ex) {
                    Log.warning(ex);
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
            }
        } else if (source == m_SaveAllXML) {
            File f = saveDialog(OutputFormatter.XMLFormat);
            if (f != null) {
                c.getModel().saveAll(f, OutputFormatter.XMLFormat);
            }

            // VIEW MENU ITEMS

        } else if (source == m_About) {
            AboutGralejWindow w = new AboutGralejWindow(frame);
            w.showWindow();
        } else if (source == m_Cascade) {
            c.getModel().cascade();
        } else if (source == m_Tile) {
            c.getModel().tile();
        } else if (source == m_ShowToolBar) {
            cfg.set("behavior.showtoolbar", ""+m_ShowToolBar.getState(), false);
            toolbar.setVisible(m_ShowToolBar.getState());

        } else if (source == m_ShowStatusBar) {
            cfg.set("behavior.showstatusbar",  ""+m_ShowStatusBar.getState(), false);
            statusbar.setVisible(m_ShowStatusBar.getState());

        } else if (source == m_Options || source == b_Options) {
            // TODO tie preferences window in here
            //GenDialog prefFrame = new GenDialog(frame);
            //prefFrame.setVisible(true);
            new OptionsDialog(frame, true).setVisible(true);
        }
        else if (source == m_Server) {
            // maybe not the best way to store the information
            if (m_Server.getText().equals("Start Server")) {
                c.startServer();
            } else {
                c.stopServer();
            }
        } else if (source == m_AutoOpenWindows) {
            cfg.set("behavior.openonload", ""+m_AutoOpenWindows.getState(), false);
        } else if (source == m_AutoExpandTags) {
            cfg.set("behavior.autoexpandtags", ""+m_AutoExpandTags.getState(), false);
        }
    }

    /**
     * A standard saving dialog with convenient file extension handling
     * 
     * @param format
     * @return the chosen file
     */
    public File saveDialog(int format) {
        JFileChooser fc = new JFileChooser(cfg.get("input.lastdir"));
        fc.setMultiSelectionEnabled(false);
        // fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(c.getModel().getOutputFormatter().getFilter(
                format));
        int returnVal = fc.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            try {
                File dir = f.isDirectory() ? f : f.getParentFile();
                cfg.set("input.lastdir", dir.getCanonicalPath(), false);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            if (!f.getName().toLowerCase().endsWith(
                    c.getModel().getOutputFormatter().getExtension(format))) {
                f = new File(f.getPath()
                        + "."
                        + c.getModel().getOutputFormatter()
                                .getExtension(format));
            }

            if (!f.exists()
                    || JOptionPane.showConfirmDialog(null, "File "
                            + f.getName() + " exists. Overwrite?",
                            "Overwrite?", 
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
    public MainGUI(Controller c, boolean useServer) {
        lastInstance = this;
        
        this.c = c;
        cfg = Config.currentConfig();
        theme = IconThemeFactory.getIconTheme(cfg.get("gui.l+f.icontheme"));

        frame = new JFrame(gralej.Globals.APP_NAME);
        frame.setIconImage(theme.getIcon("grale").getImage());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // instantiate menus
        frame.setJMenuBar(this.createMenuBar(useServer));

        // instantiate toolbar
        createToolBar();
        frame.getContentPane().add(toolbar, BorderLayout.NORTH);

        // observers
        ContentObserver list = new ListContentObserver(c.getModel(), this);
        new WindowsContentObserver(c.getModel(), theme, this);
        frame.getContentPane().add(list.getDisplay(), BorderLayout.CENTER);

        statusbar = new StatusBar();
        frame.getContentPane().add(statusbar, BorderLayout.PAGE_END);

        notifyOfSelection(false);
        notifyOfListElements(0);

        frame.pack();
        frame.setSize(cfg.getInt("gui.windows.main.size.width"), 
                      cfg.getInt("gui.windows.main.size.height"));

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                quit();
            }
        });
        
        initConfigDeps();
        
        cfg.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                initConfigDeps();
            }
        });
        
        list.getDisplay().requestFocus();

        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
    
    private void initConfigDeps() {
        m_AutoExpandTags.setSelected(cfg.getBool("behavior.autoexpandtags"));
        m_AutoOpenWindows.setSelected(cfg.getBool("behavior.openonload"));
        statusbar.setVisible(cfg.getBool("behavior.showstatusbar"));
        toolbar.setVisible(cfg.getBool("behavior.showtoolbar"));
    }

    /**
     * Inner class for the status bar.
     * Rudimentary functionality: Number of items in the list; connection
     * status
     * 
     * @author Armin
     *
     */
    public class StatusBar extends JPanel {

        JLabel counter;
        JLabel connectionInfo;

        StatusBar() {
            setLayout(new GridLayout(1, 2));
            //setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            final int MARGIN = 3;
            counter = new JLabel("0");
            //counter.setHorizontalAlignment(JLabel.LEADING);
            counter.setBorder(new CompoundBorder(
                        BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                        BorderFactory.createEmptyBorder(0, MARGIN, 0, MARGIN)
                    ));
            add(counter);
            connectionInfo = new JLabel();
            //connectionInfo.setHorizontalAlignment(JLabel.TRAILING);
            connectionInfo.setBorder(new CompoundBorder(
                        BorderFactory.createBevelBorder(BevelBorder.LOWERED),
                        BorderFactory.createEmptyBorder(0, MARGIN, 0, MARGIN)
                    ));
            add(connectionInfo);
        }

        public void setNumberOfItems(int i) {
            counter.setText(i + " data items");
        }

        public void setConnectionInfo(String info) {
            connectionInfo.setText(info);
        }
    }

    /**
     * Some menu items and buttons depend on a file being selected. This method
     * is called by the list whenever the selection changes.
     * 
     * @param b:
     *            whether a list item is selected
     */
    public void notifyOfSelection(boolean b) {
        m_Close.setEnabled(b);
        m_Save.setEnabled(b);
        b_Close.setEnabled(b);
        b_Save.setEnabled(b);
    }

    /**
     * Other menu items depend on the non-emptiness of the list. This method
     * shows or hides them, and it's the list's responsibility to notify the GUI
     * accordingly.
     * 
     * @param number of elements in the list
     */
    public void notifyOfListElements(int number) {
        boolean isEmpty = (number == 0);
        saveallmenu.setEnabled(!isEmpty);
        m_CloseAll.setEnabled(!isEmpty);
        statusbar.setNumberOfItems(number);
    }

    public void notifyOfServerConnection(boolean isConnected) {
        if (isConnected) {
            if (m_Server != null)
                m_Server.setText("Stop Server");
            int openStreamCount = c.getModel().getOpenStreamCount();
            if (openStreamCount == 0)
                statusbar.setConnectionInfo("Listening...");
            else {
                String clients;
                if (openStreamCount == 1)
                    clients = " client";
                else
                    clients = " clients";
                statusbar.setConnectionInfo(openStreamCount + clients);
            }
        } else {
            if (m_Server != null)
                m_Server.setText("Start Server");
            statusbar.setConnectionInfo("Disconnected");
        }
    }

    /**
     * Get main window into the foreground.
     * Called from any data window.
     */
    public void raiseMainWindow() {
        frame.toFront();
    }

    private void doOpenURL() {
        String surl = JOptionPane.showInputDialog(frame, "Enter URL:");
        if (surl == null)
            return;
        try {
            c.open(new URL(surl));
        }
        catch (Exception ex) {
            Log.error(ex);
        }
    }
}
