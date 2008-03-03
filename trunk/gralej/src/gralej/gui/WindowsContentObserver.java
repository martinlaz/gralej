package gralej.gui;

import gralej.controller.ContentModel;
import gralej.error.ErrorHandler;
import gralej.gui.icons.IconTheme;
import gralej.blocks.BlockPanel;
import gralej.parsers.*;
import gralej.prefs.GralePreferences;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

/**
 * A WindowContentObserver is that type of ContentObserver showing
 * AVMs in single JFrames. These frames are modelled as an inner
 * class.
 * 
 * 
 * @author Armin
 * @version
 */
public class WindowsContentObserver extends ContentObserver {

    private ArrayList<Window> frames;

    private IconTheme theme;

    private MainGUI gui;

    public WindowsContentObserver(ContentModel m, IconTheme theme, MainGUI gui) {
        super(m);
        this.theme = theme;
        this.gui = gui;
        m.setObserver(this);
        frames = new ArrayList<Window>();
    }

    public void add(IDataPackage data) {
        Window w = new Window(data);
        frames.add(w);
    }

    @Override
    public void close() {
        for (int i = 0; i < model.getFocus().length; i++) {
            IDataPackage d = model.getData(model.getFocus()[i]);
            for (int j = 0; j < frames.size(); j++) {
                if (frames.get(j).data == d) {
                    frames.get(j).dispose();
                }
            }
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < frames.size(); i++) {
            frames.get(i).dispose();
        }
        frames.clear();
    }

    /**
     * distribute open frames over the existing space all same size
     * 
     * The code is almost directly taken from
     * http://www.javalobby.org/forums/thread.jspa?threadID=15696&tstart=30
     * (cannot find out their copyright policy)
     * 
     */
    public void tile() {
        Rectangle size = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();

        int cols = (int) Math.sqrt(frames.size());
        int rows = (int) (Math.ceil(((double) frames.size()) / cols));
        int lastRow = frames.size() - cols * (rows - 1);
        int width, height;

        if (lastRow == 0) {
            rows--;
            height = size.height / rows;
        } else {
            height = size.height / rows;
            if (lastRow < cols) {
                rows--;
                width = size.width / lastRow;
                for (int i = 0; i < lastRow; i++) {
                    frames.get(cols * rows + i).setBounds(i * width,
                            rows * height, width, height);
                }
            }
        }

        width = size.width / cols;
        for (int j = 0; j < rows; j++) {
            for (int i = 0; i < cols; i++) {
                frames.get(i + j * cols).setBounds(i * width, j * height,
                        width, height);
            }
        }
    }

    /**
     * Cascade windows. Offsets are taken from the general preferences.
     */
    public void cascade() {
        GralePreferences gp = GralePreferences.getInstance();
        int xMarginOffset = gp.getInt("gui.windows.location.xmarginoffset");
        int yMarginOffset = gp.getInt("gui.windows.location.ymarginoffset");
        int xDiff = gp.getInt("gui.windows.location.xdiff");
        int yDiff = gp.getInt("gui.windows.location.ydiff");
        int xDiff2 = gp.getInt("gui.windows.location.xdiff2");

        Rectangle size = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getMaximumWindowBounds();

        Point x = new Point(0, 0);
        for (int i = 0; i < frames.size(); i++) {
            x.translate(xDiff, yDiff);
            if (x.y + yMarginOffset > size.height) {
                x.move(x.x - x.y + xDiff2, yDiff);
            }
            if (x.x + xMarginOffset > size.width) {
                x.move(xDiff, yDiff);
            }

            frames.get(i).setLocation(x);
        }
    }

    class Window extends JFrame implements ActionListener {

        IDataPackage data;

        BlockPanel display;

        boolean autoResize;

        Window(IDataPackage data) {
            super(data.getTitle());
            this.data = data;
            this.display = data.createView();
            
            setIconImage(theme.getIcon("grale").getImage());
            
            this.autoResize = gp.getBoolean("behavior.alwaysfitsize");
            display.setAutoResize(autoResize);

            setJMenuBar(createMenuBar());
            createToolBar();
            toolbar.setVisible(gp.getBoolean("behavior.showwindowtoolbar"));
            add(toolbar, BorderLayout.NORTH);
            add(display.getUI());
            setLocationByPlatform(true);
            setMinimumSize(new Dimension(
                    gp.getInt("gui.windows.size.xmin"),
                    gp.getInt("gui.windows.size.ymin")));
            setSize(display.getUI().getSize());
            setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
            
            pack();
            display.getUI().requestFocus();
            setVisible(true);
        }

        private GralePreferences gp = GralePreferences.getInstance();

        private JMenuItem m_Close, m_Latex, m_Postscript, m_SVG, m_Print,
                m_Tree, m_Struc, m_Expand, m_Restore, m_Hidden, m_Find,
                m_Resize, m_ZoomPlus, m_ZoomMinus, m_Save, m_XML,
                m_JPG, m_PNG, m_Raise;
        
        private JCheckBoxMenuItem m_ShowWindowToolBar;

        private JButton b_Close, b_Print, b_Find, b_Resize, b_ZoomPlus, 
                b_ZoomMinus, b_Save, b_Raise;
        private JToolBar toolbar;
        private JTextField zoomfield, searchfield;

        private JMenuBar createMenuBar() {

            JMenuBar menubar = new JMenuBar();
            // menu "File"
            JMenu filemenu = new JMenu("File");
            filemenu.setMnemonic('F');

            m_Save = new JMenuItem("Save");
            m_Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                    InputEvent.CTRL_DOWN_MASK));
            m_Save.addActionListener(this);
            filemenu.add(m_Save);

            JMenu exportSubmenu = new JMenu("Export");
            // sub LaTeX
            m_Latex = new JMenuItem("LaTeX");
            m_Latex.addActionListener(this);
            exportSubmenu.add(m_Latex);
            // sub Postscript
            m_Postscript = new JMenuItem("Postscript");
            m_Postscript.addActionListener(this);
            exportSubmenu.add(m_Postscript);
            filemenu.add(exportSubmenu);
            // sub SVG
            m_SVG = new JMenuItem("SVG");
            m_SVG.addActionListener(this);
            exportSubmenu.add(m_SVG);
            // sub XML
            m_XML = new JMenuItem("XML");
            m_XML.addActionListener(this);
            exportSubmenu.add(m_XML);
            // sub JPG
            m_JPG = new JMenuItem("JPG");
            m_JPG.addActionListener(this);
            exportSubmenu.add(m_JPG);

            m_PNG = new JMenuItem("PNG");
            m_PNG.addActionListener(this);
            exportSubmenu.add(m_PNG);

            filemenu.add(exportSubmenu);
            // menuitem Print
            m_Print = new JMenuItem("Print");
            m_Print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                    InputEvent.CTRL_DOWN_MASK));
            m_Print.addActionListener(this);
            filemenu.add(m_Print);

            m_Close = new JMenuItem("Close");
            m_Close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                    InputEvent.CTRL_DOWN_MASK));
            m_Close.addActionListener(this);
            filemenu.add(m_Close);
            
            menubar.add(filemenu);

            JMenu viewmenu = new JMenu("View");
            viewmenu.setMnemonic('V');
            
            ButtonGroup viewmode = new ButtonGroup();

            m_Tree = new JRadioButtonMenuItem("Tree");
            m_Tree.setSelected(true); // default
            m_Tree.addActionListener(this);
            viewmode.add(m_Tree);
//            viewmenu.add(m_Tree); // TODO implement and uncomment

            m_Struc = new JRadioButtonMenuItem("Structure");
            m_Struc.addActionListener(this);
            viewmode.add(m_Struc);
//            viewmenu.add(m_Struc); // TODO implement and uncomment

            m_Expand = new JMenuItem("Expand");
            m_Expand.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
                    InputEvent.CTRL_DOWN_MASK));
            m_Expand.addActionListener(this);
//            viewmenu.add(m_Expand); // TODO implement and uncomment

            m_Restore = new JMenuItem("Restore");
            m_Restore.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
                    InputEvent.CTRL_DOWN_MASK));
            m_Restore.addActionListener(this);
//            viewmenu.add(m_Restore); // TODO implement and uncomment

            // checkbox "Show Hidden Nodes" (shaded)
            m_Hidden = new JCheckBoxMenuItem("Show Hidden Nodes");
            m_Hidden.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
                    InputEvent.CTRL_DOWN_MASK));
            m_Hidden.addActionListener(this);
//            viewmenu.add(m_Hidden); // TODO implement and uncomment

            m_Resize = new JCheckBoxMenuItem("Adjust window size");
            m_Resize.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
            m_Resize.addActionListener(this);
            ((JCheckBoxMenuItem) m_Resize).setSelected(autoResize);
            viewmenu.add(m_Resize);

            m_ZoomPlus = new JMenuItem("Zoom in");
            m_ZoomPlus.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,
                    InputEvent.CTRL_DOWN_MASK));
            m_ZoomPlus.addActionListener(this);
            viewmenu.add(m_ZoomPlus);

            m_ZoomMinus = new JMenuItem("Zoom out");
            m_ZoomMinus.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK));
            m_ZoomMinus.addActionListener(this);
            viewmenu.add(m_ZoomMinus);

            // menuitem "Find"
            m_Find = new JMenuItem("Find");
            m_Find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
                    InputEvent.CTRL_DOWN_MASK));
            m_Find.addActionListener(this);
//            viewmenu.add(m_Find);

            // menuitem "Find"
            m_Raise = new JMenuItem("Raise Main Window");
            m_Raise.addActionListener(this);
            viewmenu.add(m_Raise);

            m_ShowWindowToolBar = new JCheckBoxMenuItem("Show toolbar");
            m_ShowWindowToolBar.addActionListener(this);
            m_ShowWindowToolBar.setState(gp.getBoolean("behavior.showwindowtoolbar"));
            viewmenu.add(m_ShowWindowToolBar);

            menubar.add(viewmenu);
            return menubar;
        }

        private void createToolBar() {
            toolbar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);

            b_Save = new JButton(theme.getIcon("filefloppy"));
            b_Save.addActionListener(this);
            toolbar.add(b_Save);
            
            b_Print = new JButton(theme.getIcon("fileprint"));
            b_Print.addActionListener(this);
            b_Print.setToolTipText("Print");
            toolbar.add(b_Print);

            b_Resize = new JButton(theme.getIcon("maximize"));
            b_Resize.addActionListener(this);
            b_Resize.setToolTipText("Enable/disable auto-resizing");
            b_Resize.setSelected(autoResize);
            toolbar.add(b_Resize);

            toolbar.addSeparator();

            b_ZoomMinus = new JButton(theme.getIcon("zoomout"));
            b_ZoomMinus.addActionListener(this);
            b_ZoomMinus.setToolTipText("Zoom out");
            toolbar.add(b_ZoomMinus);

            toolbar.add(new JLabel("Zoom:"));
            zoomfield = new JTextField("100");
            zoomfield.setHorizontalAlignment(JTextField.RIGHT);
            zoomfield.addActionListener(this);
            // zoomfield.setPreferredSize(new Dimension(10,30));
            zoomfield.setMaximumSize(new Dimension(40, 20));
            zoomfield.setToolTipText("Zoom value");
            zoomfield.setText(Integer.toString(display.getZoom()));

            toolbar.add(zoomfield);
            toolbar.add(new JLabel("%"));
            b_ZoomPlus = new JButton(theme.getIcon("zoomin"));
            b_ZoomPlus.addActionListener(this);
            b_ZoomPlus.setToolTipText("Zoom in");
            toolbar.add(b_ZoomPlus);

            searchfield = new JTextField();
            searchfield.setMaximumSize(new Dimension(90, 20));
            searchfield.addActionListener(this);
//            toolbar.add(searchfield);

            b_Find = new JButton(theme.getIcon("magglass"));
            b_Find.addActionListener(this);
            b_Find.setToolTipText("Find");
//            toolbar.add(b_Find);

            b_Raise = new JButton(theme.getIcon("raisewindow"));
            b_Raise.addActionListener(this);
            b_Raise.setToolTipText("Raise main window");
            toolbar.add(b_Raise);

        }

        /**
         * This method listens to user actions and calls the appropriate
         * handlers.
         * 
         */
        public void actionPerformed(ActionEvent e) {
            JComponent source = (JComponent) (e.getSource());
            if (source == m_Close || source == b_Close) {
                dispose();
            } else if (source == m_Save || source == b_Save) {
                save(OutputFormatter.TRALEFormat);
            } else if (source == m_Latex) {
                save(OutputFormatter.LaTeXFormat);
            } else if (source == m_Postscript) {
                save(OutputFormatter.PostscriptFormat);
            } else if (source == m_SVG) {
                save(OutputFormatter.SVGFormat);
            } else if (source == m_XML) {
                save(OutputFormatter.XMLFormat);
            } else if (source == m_JPG) {
                save(OutputFormatter.JPGFormat);
            } else if (source == m_PNG) {
                save(OutputFormatter.PNGFormat);
            } else if (source == m_Print || source == b_Print) {
                model.print(display);

            } else if (source == m_ZoomPlus || source == b_ZoomPlus) {
                ((BlockPanel) display).zoomIn();
                zoomfield.setText(Integer.toString(display.getZoom()));
            } else if (source == m_ZoomMinus || source == b_ZoomMinus) {
                ((BlockPanel) display).zoomOut();
                zoomfield.setText(Integer.toString(display.getZoom()));
            } else if (source == zoomfield) {
                try {
                    display.setZoom(
                            Integer.parseInt(zoomfield.getText().trim()));
                    zoomfield.setText(
                            Integer.toString(display.getZoom()));
                } catch (NumberFormatException e1) {
                    zoomfield.setText(gp.get("behavior.defaultzoom"));
                    ErrorHandler.getInstance().report(
                            "Invalid zoom value. Defaulting to 100%.",
                            ErrorHandler.WARNING);
                    display.setZoom(Integer.parseInt(zoomfield.getText()));
                }
            } else if (source == m_Resize || source == b_Resize) {
                autoResize = !autoResize;
                b_Resize.setSelected(autoResize);
                ((JCheckBoxMenuItem) m_Resize).setSelected(autoResize);
                ((BlockPanel) display).setAutoResize(autoResize);
                if (autoResize) this.pack();
            } else if (source == m_Find) {
//                String searchFor = JOptionPane.showInputDialog(null,
//                        "Search for sorts or attributes containing:");
            } else if (source == b_Find || source == searchfield) {
//                String searchFor = searchfield.getText();

            } else if (source == m_Raise || source == b_Raise) {
                gui.raiseMainWindow();
            } else if (source == m_ShowWindowToolBar) {
                gp.putBoolean("behavior.showwindowtoolbar", m_ShowWindowToolBar.getState());
                toolbar.setVisible(m_ShowWindowToolBar.getState());
            }

        }

        private void save(int format) {
            File f = gui.saveDialog(format);
            if (f != null) {
                model.save(f, data, display, format);
            } else {
                // file could not be opened. doing nothing might be appropriate
            }

        }

    }

    public void notifyOfServerConnection(boolean isConnected) {
        gui.notifyOfServerConnection(isConnected);
    }

}
