/**
 * 
 */
package gralej.gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;


import gralej.controller.*;

/**
 * @author Armin
 * @version $Id$
 */
public class MainGUI implements ActionListener, ItemListener {

//	private static GRALEContentWindow content = new GRALEContentWindow();
	private JSplitPane content;
	
	private Controller c; // the gralej.controller 
	
	// menu items
	private JMenuItem m_Exit, m_Close, m_Open, m_Latex, m_Postscript, m_SVG, 
	                  m_Print, m_About, m_Tree, m_Struc, m_Expand, m_Restore, 
	                  m_Hidden, m_Find, m_Pref;
	
	// buttons (basically the same as the menu items)
	private JButton b_Open, b_Close, b_TreeStruc, b_Print, b_Expand, b_Hidden,
	                b_Restore, b_Find;
	
	
	private JMenuBar createMenuBar() {
        // menu
        JMenuBar menubar = new JMenuBar();
        // menu file
        JMenu filemenu = new JMenu("File");
        filemenu.add(new JSeparator());

        m_Open = new JMenuItem("Open");
        m_Open.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        m_Open.addActionListener(this);
        filemenu.add(m_Open);

        m_Close = new JMenuItem("Close Tab");
        m_Close.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        m_Close.addActionListener(this);
        filemenu.add(m_Close);

        filemenu.add(new JSeparator());
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
        filemenu.add(exportSubmenu);
        //  menuitem Print
        m_Print = new JMenuItem("Print");
        m_Print.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        m_Print.addActionListener(this);
        filemenu.add(m_Print);
        
        m_Exit = new JMenuItem("Exit");
        m_Exit.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
        m_Exit.addActionListener(this);
        filemenu.add(m_Exit);
        menubar.add(filemenu);
        
        
        // menu View
        JMenu viewmenu = new JMenu("View");
        
        // Tree/Structure (alternating; defaults to Tree, changeable)
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
        
        // menuitem "Expand" (expands tags)
        m_Expand = new JMenuItem("Expand");
        m_Expand.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        m_Expand.addActionListener(this);
        viewmenu.add(m_Expand);
        
        // menuitem "Restore" (restores original view)
        m_Restore = new JMenuItem("Restore");
        m_Restore.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        m_Restore.addActionListener(this);
        viewmenu.add(m_Restore);
        
        // checkbox "Show Hidden Nodes" (shaded)
        m_Hidden = new JCheckBoxMenuItem("Show Hidden Nodes");
        m_Hidden.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
        m_Hidden.addActionListener(this);
        viewmenu.add(m_Hidden);

        viewmenu.addSeparator();

        // menuitem "Find"
        m_Find = new JMenuItem("Find");
        m_Find.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        m_Find.addActionListener(this);
        viewmenu.add(m_Find);
        
        // menuitem "Settings" (spacing, fonts, colors)
        m_Pref = new JMenuItem("Preferences");
        m_Pref.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_P, InputEvent.CTRL_DOWN_MASK));
        m_Pref.addActionListener(this);
        viewmenu.add(m_Pref);
        
        // menuitem "About GRALE"
        m_About = new JMenuItem("About GRALE");
        m_About.addActionListener(this);
        viewmenu.add(m_About);
        
        menubar.add(viewmenu);
		return menubar;
	}
	
	private JToolBar createToolBar () {
		// toolbar: some example buttons
		JToolBar toolbar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);

		b_Open = new JButton(new ImageIcon("fileopen.png"));
        b_Open.addActionListener(this);
		toolbar.add(b_Open);
    
		/* code for icon change
    	b_Open.setRolloverIcon(new ImageIcon("fileopen.png"));
    	b_Open.setPressedIcon(...);
		 */
    
		JButton zoomin = new JButton(new ImageIcon("zoomin.png"));
		toolbar.add(zoomin);
		JButton zoomout = new JButton(new ImageIcon("zoomout.png"));
		toolbar.add(zoomout);
		return toolbar;
	}

	private void createSplit() {
	    //Create a split pane with the two scroll panes in it.
        content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); 
        
        // observer GUIs yet missing as parameters. 
        // how to access the scrollpane from here?

        
        content.setOneTouchExpandable(true);
        content.setDividerLocation(150);

/*        //Provide minimum sizes for the two components in the split pane.
        Dimension minimumSize = new Dimension(100, 50);
        listScrollPane.setMinimumSize(minimumSize);
        pictureScrollPane.setMinimumSize(minimumSize);
        */

        //Provide a preferred size for the split pane.
        content.setPreferredSize(new Dimension(500, 250));
        
	}
	
	public void addToSplit(ContentObserver o) {
		content.add(o.getDisplay());

	}

    // User actions broadcast Events. Depending on the source, they're ActionEvents (menu item) or ItemEvents (checkbox)
    public void actionPerformed(ActionEvent e) {
        JComponent source = (JComponent)(e.getSource());
    	if (source == m_Exit) {
    		System.exit(0);
    	} else if (source == m_Open || source == b_Open) {
    		// open file dialog, send file to tabs
    		JFileChooser fc = new JFileChooser();
    		int returnVal = fc.showOpenDialog(source);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                c.open(fc.getSelectedFile());
            } else {
            	// file could not be opened. doing nothing might be appropriate
            }
    	} else if (source == m_Close || source == b_Close) {
    		c.close();
    	} else if (source == m_Latex) {
    		// call output method
    	} else if (source == m_Postscript) {
    		// call output method
    	} else if (source == m_Print || source == b_Print) {
    		// call print method
    	} else if (source == m_About) {
    		JOptionPane.showMessageDialog(null, "GRALE, Java version (2007)");
    	} else if (source ==  m_SVG) {
    		// call output method
    	} else if (source ==  m_Tree) {
    		// send "set tree" via content window to focus window    		
    	} else if (source ==  m_Struc) {
    		// send "set structure" via content window to focus window
    	} else if (source ==  b_TreeStruc) {
    		// send "toggle tree/structure" via content window to focus window
    		// on this occasion the button icon should change (displaying the NOW state)
    	} else if (source ==  m_Expand || source == b_Expand) {
    		// send "expand" via content window to focus window
    	} else if (source ==  m_Restore || source == b_Restore) {
    		// send "restore" via content window to focus window
    	} else if (source ==  m_Hidden || source == b_Hidden) {
    		// send "toggle hidden" via content window to focus window
    	} else if (source ==  m_Find || source == b_Find) {
    		// send search request to content window
    	} else if (source ==  m_Pref) {
    		// open Preferences Dialog
    		prefDialog();
    	}
    }

    public void itemStateChanged(ItemEvent e) {
//        JMenuItem source = (JMenuItem)(e.getSource());
    }
    
    
	public void prefDialog () {		

		
		JButton b_import = new JButton("Import");
		b_import.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // import preferences from file
            }
        });

	}


	
	/**
	 * 
	 */
	public MainGUI(Controller c) {
		this.c = c;

        //Create and set up the window.
        JFrame frame = new JFrame("GraleJ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // instantiate menus
        frame.setJMenuBar(this.createMenuBar());
        
        // instantiate toolbar
        frame.getContentPane().add(this.createToolBar(),BorderLayout.NORTH);

        // content part
        createSplit();
        frame.add(content, BorderLayout.CENTER);
        
        // bottom status line
        // to be implemented
        
        //Display the window.
//        frame.setUndecorated(true);
//        frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
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
        frame.pack();
        frame.setSize(700,400);
        frame.setVisible(true);
        
	}

}
