package gralej.gui;

import gralej.controller.ContentModel;
import gralej.gui.icons.IconTheme;
import gralej.gui.blocks.BlockPanel;
import gralej.parsers.*;
import gralej.gui.MainGUI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;

/**
 * 
 * 
 * 
 * 
 * 
 * @author Armin
 * @version $id$
 */
public class WindowsContentObserver extends ContentObserver {
	
	private ArrayList<Window> frames;
	
	private IconTheme theme;

	private MainGUI gui;

	/**
	 * @param m
	 */
	public WindowsContentObserver(ContentModel m, IconTheme theme, MainGUI gui) {
		super(m);
		this.theme = theme;
		this.gui = gui;
		m.setObserver(this);
		frames = new ArrayList<Window>();
	}
	

	public void add (IDataPackage data) {
		Window w = new Window (data);
		frames.add(w);
	}
	
	@Override
	public void close () {
		IDataPackage d = model.getData(model.getFocus());
		for (int i = 0; i < frames.size(); i++) {
			if (frames.get(i).data == d) {
				frames.get(i).dispose();				
			}
		}
	}
	
	@Override
	public void clear () {
		for (int i = 0; i < frames.size(); i++) {
			frames.get(i).dispose();
		}
		frames.clear();		
	}
	
	/**
	 * distribute open frames over the existing space
	 * all same size
	 * 
	 * The code is almost directly taken from
	 * http://www.javalobby.org/forums/thread.jspa?threadID=15696&tstart=30
	 * (cannot find out their copyright policy)
	 * 
	 */
	public void tile () {
		Rectangle size = GraphicsEnvironment.getLocalGraphicsEnvironment()
			.getMaximumWindowBounds();
//		Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		int cols = (int)Math.sqrt(frames.size());
	    int rows = (int)(Math.ceil( ((double)frames.size()) / cols));
	    int lastRow = frames.size() - cols*(rows-1);
	    int width, height;
	 
	    if ( lastRow == 0 ) {
	        rows--;
	        height = size.height / rows;
	    }
	    else {
	        height = size.height / rows;
	        if ( lastRow < cols ) {
	            rows--;
	            width = size.width / lastRow;
	            for (int i = 0; i < lastRow; i++ ) {
	                frames.get(cols*rows+i).setBounds( i*width, rows*height,
	                                               width, height );
	            }
	        }
	    }
	            
	    width = size.width/cols;
	    for (int j = 0; j < rows; j++ ) {
	        for (int i = 0; i < cols; i++ ) {
	            frames.get(i+j*cols).setBounds( i*width, j*height,
	                                        width, height );
	        }
	    }
	}

	
	/**
	 * cascade windows as if newly generated
	 * 
	 */
	public void cascade () {
		Rectangle size = GraphicsEnvironment.getLocalGraphicsEnvironment()
		.getMaximumWindowBounds();
//	Dimension size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

		Point x = new Point(0,0);
		for (int i = 0; i < frames.size(); i++) {
	       	x.translate(30, 30);
	       	if (x.y + 100 > size.height ) {
	       		x.move(x.x - x.y + 75, 30);
	       	}
	       	if (x.x + 100 > size.width ) {
	       		x.move(30, 30);
	       	}
	        
		    frames.get(i).setLocation(x);
//			model.setFocused(i); // threading problems: infinite re-focus loop
		}
	}

	private class Window extends JFrame implements ActionListener {
		
		IDataPackage data;
		
		JComponent display;
		
		boolean autoResize = false;
		
		Window (IDataPackage data) {
			super(data.getTitle());
			this.data = data;
			this.display = data.createView();
		    display.setOpaque(true);
	        setJMenuBar(createMenuBar());        
	        add(createToolBar(),BorderLayout.NORTH);
	        add(display);
		    setLocationByPlatform(true);
		    setMinimumSize(new Dimension(250,150));
		    setSize(display.getSize());
		    setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		    setVisible(true);
		    pack();
			return;
		}
	
	private JMenuItem m_Close, m_Latex, m_Postscript, m_SVG, 
    	m_Print, m_Tree, m_Struc, m_Expand, m_Restore, 
    	m_Hidden, m_Find, m_Resize, m_AutoResize, m_ZoomPlus, m_ZoomMinus,
    	m_Save, m_XML, m_JPG;

	// buttons (basically the same as the menu items)
	private JButton b_Close, b_TreeStruc, b_Print, b_Expand, b_Hidden,
		b_Restore, b_Find, b_Resize, b_AutoResize, b_ZoomPlus, b_ZoomMinus,
		b_Save;
	
	private JTextField zoomfield, searchfield;


	private JMenuBar createMenuBar() {
		// menu
		JMenuBar menubar = new JMenuBar();
		// menu file
		JMenu filemenu = new JMenu("File");
		filemenu.add(new JSeparator());

		m_Close = new JMenuItem("Close");
		m_Close.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
		m_Close.addActionListener(this);
		filemenu.add(m_Close);
		
        m_Save = new JMenuItem("Save");
        m_Save.addActionListener(this);
        filemenu.add(m_Save);
        

		filemenu.add(new JSeparator());
		JMenu exportSubmenu = new JMenu("Export");
//		sub LaTeX
		m_Latex = new JMenuItem("LaTeX");
		m_Latex.addActionListener(this);
		exportSubmenu.add(m_Latex);
//		sub Postscript
		m_Postscript = new JMenuItem("Postscript");
		m_Postscript.addActionListener(this);
		exportSubmenu.add(m_Postscript);        
		filemenu.add(exportSubmenu);
//		sub SVG
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
		
		filemenu.add(exportSubmenu);
//		menuitem Print
		m_Print = new JMenuItem("Print");
		m_Print.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
		m_Print.addActionListener(this);
		filemenu.add(m_Print);

		menubar.add(filemenu);

		JMenu viewmenu = new JMenu("View");

		viewmenu.addSeparator();
		ButtonGroup viewmode = new ButtonGroup();

		m_Tree = new JRadioButtonMenuItem("Tree");
		m_Tree.setSelected(true); // default
		m_Tree.addActionListener(this);
		viewmode.add(m_Tree);
		viewmenu.add(m_Tree);

		m_Struc = new JRadioButtonMenuItem("Structure");
		m_Struc.addActionListener(this);
		viewmode.add(m_Struc);
		viewmenu.add(m_Struc);
		viewmenu.addSeparator();

		m_Expand = new JMenuItem("Expand");
		m_Expand.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		m_Expand.addActionListener(this);
		viewmenu.add(m_Expand);

		m_Restore = new JMenuItem("Restore");
		m_Restore.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		m_Restore.addActionListener(this);
		viewmenu.add(m_Restore);

//		checkbox "Show Hidden Nodes" (shaded)
		m_Hidden = new JCheckBoxMenuItem("Show Hidden Nodes");
		m_Hidden.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
		m_Hidden.addActionListener(this);
		viewmenu.add(m_Hidden);
		
		m_Resize = new JMenuItem("Adjust window size");
		m_Resize.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		m_Resize.addActionListener(this);
		viewmenu.add(m_Resize);

		m_AutoResize = new JCheckBoxMenuItem("Auto-Adjust window size");
//		m_AutoResize.setAccelerator(KeyStroke.getKeyStroke(
//				KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		m_AutoResize.addActionListener(this);
		viewmenu.add(m_AutoResize);

		m_ZoomPlus = new JMenuItem("Zoom in");
		m_ZoomPlus.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK));
		m_ZoomPlus.addActionListener(this);
		viewmenu.add(m_ZoomPlus);

		m_ZoomMinus = new JMenuItem("Zoom out");
		m_ZoomMinus.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK));
		m_ZoomMinus.addActionListener(this);
		viewmenu.add(m_ZoomMinus);


		viewmenu.addSeparator();

//		menuitem "Find"
		m_Find = new JMenuItem("Find");
		m_Find.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
		m_Find.addActionListener(this);
		viewmenu.add(m_Find);


		menubar.add(viewmenu);
		return menubar;
	}

	private JToolBar createToolBar () {
		// toolbar: some example buttons
		JToolBar toolbar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
		
		b_Save = new JButton(theme.getIcon("filefloppy"));
        b_Save.addActionListener(this);
		toolbar.add(b_Save);

		b_Resize = new JButton(theme.getIcon("maximize"));
        b_Resize.addActionListener(this);
        b_Resize.setToolTipText("Resize window to fit");
		toolbar.add(b_Resize);

		b_AutoResize = new JButton(theme.getIcon("fitwindow"));
        b_AutoResize.addActionListener(this);
        b_AutoResize.setToolTipText("En-/Disable Auto-Resizing");
		toolbar.add(b_AutoResize);

		toolbar.addSeparator();
		
		b_ZoomMinus = new JButton(theme.getIcon("zoomout"));
        b_ZoomMinus.addActionListener(this);
        b_ZoomMinus.setToolTipText("Zoom out");
		toolbar.add(b_ZoomMinus);
		
		toolbar.add(new JLabel("Zoom:"));
		zoomfield = new JTextField("100");
		zoomfield.addActionListener(this);
//		zoomfield.setPreferredSize(new Dimension(10,30));
		zoomfield.setMaximumSize(new Dimension(40,20));
		zoomfield.setToolTipText("Zoom value");
		toolbar.add(zoomfield);
		toolbar.add(new JLabel("%"));
		b_ZoomPlus = new JButton(theme.getIcon("zoomin"));
        b_ZoomPlus.addActionListener(this);
        b_ZoomPlus.setToolTipText("Zoom in");
		toolbar.add(b_ZoomPlus);

		toolbar.addSeparator();
		
		searchfield = new JTextField();
		searchfield.setMaximumSize(new Dimension(90,20));
		searchfield.addActionListener(this);
		toolbar.add(searchfield);
		
		b_Find = new JButton(theme.getIcon("magglass"));
        b_Find.addActionListener(this);
        b_Find.setToolTipText("Find");
		toolbar.add(b_Find);
		
		

		return toolbar;
	}



//	User actions broadcast Events. Depending on the source, they're ActionEvents (menu item) or ItemEvents (checkbox)
	public void actionPerformed(ActionEvent e) {
		JComponent source = (JComponent)(e.getSource());
		if (source ==  m_Close || source == b_Close) {
			dispose();
    	} else if (source == m_Save || source == b_Save) {
    		save(OutputFormatter.TRALEFormat);
		} else if (source == m_Latex) {
    		save(OutputFormatter.LaTeXFormat);
 		} else if (source == m_Postscript) {
    		save(OutputFormatter.PostscriptFormat);
		} else if (source == m_Print || source == b_Print) {
			model.print(data);
		} else if (source ==  m_SVG) {
    		save(OutputFormatter.SVGFormat);
		} else if (source ==  m_XML) {
    		save(OutputFormatter.XMLFormat);
		} else if (source ==  m_JPG) {
    		save(OutputFormatter.JPGFormat);
		} else if (source ==  m_Tree) {

		} else if (source ==  m_Struc) {

		} else if (source ==  b_TreeStruc) {

			// on this occasion the button icon should change (displaying the NOW state)
		} else if (source ==  m_Expand || source == b_Expand) {
			// send "expand" via content window to focus window
		} else if (source ==  m_Restore || source == b_Restore) {
			// send "restore" via content window to focus window
		} else if (source ==  m_Hidden || source == b_Hidden) {
			// send "toggle hidden" via content window to focus window
		} else if (source ==  m_Resize || source == b_Resize) {
			this.pack();
		} else if (source ==  m_ZoomPlus || source == b_ZoomPlus) {
			((BlockPanel) display).increaseScaleFactor();
			zoomfield.setText(
					Integer.toString((int) Math.floor(((BlockPanel) display).getScaleFactor() * 100)));
		} else if (source ==  m_ZoomMinus || source == b_ZoomMinus) {
			((BlockPanel) display).decreaseScaleFactor();			
			zoomfield.setText(
					Integer.toString((int) Math.floor(((BlockPanel) display).getScaleFactor() * 100)));
		} else if (source ==  zoomfield) {
			try {
				((BlockPanel) display).setScaleFactor(
						Math.floor(Double.parseDouble(((JTextField)source).getText())) / 100);
				zoomfield.setText(
						Integer.toString((int) Math.floor(((BlockPanel) display).getScaleFactor() * 100)));
			} catch (NumberFormatException e1) {
				zoomfield.setText("100");
				System.err.println("Invalid zoom value. Defaulting to 100%.");
				((BlockPanel) display).setScaleFactor(1);
			}			
		} else if (source ==  m_AutoResize || source == b_AutoResize) {
			autoResize = ! autoResize;
			b_AutoResize.setSelected(autoResize);
			((JCheckBoxMenuItem) m_AutoResize).setSelected(autoResize);
			((BlockPanel) display).setAutoResize(autoResize);
		} else if (source ==  m_Find) {
			String searchFor = JOptionPane.showInputDialog(null, 
					"Search for sorts or attributes containing:");
			// TODO send search request to content window
		} else if (source == b_Find || source == searchfield) {
			String searchFor = searchfield.getText();
			// TODO send search request to content window
			
		}
	}

	private void save (int format) {
		File f = gui.saveDialog(format);
		if (f != null) {
    		model.save(f, data, format);
        } else {
        	// file could not be opened. doing nothing might be appropriate
        }
		
	}


	}
        
}
