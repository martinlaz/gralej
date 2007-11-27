/**
 * 
 */
package gralej.gui;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import java.util.prefs.*;


import gralej.controller.*;
import gralej.gui.icons.IconTheme;
import gralej.gui.icons.IconThemeFactory;

/**
 * TODO remember last open directory (via prefs?)
 * 
 * @author Armin
 * @version $Id$
 */
public class MainGUI implements ActionListener, ItemListener {

	// display mode
	static int FRAMES = 0; 
	static int WINDOWS = 1; 
	private int mode = WINDOWS;
	
	private String lastDir;


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
        m_Pref.setAccelerator(KeyStroke.getKeyStroke("F2"));
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
		
		// load icon theme
		IconTheme theme = IconThemeFactory.getIconTheme("crystal");

		b_Open = new JButton(theme.getIcon("fileopen"));
        b_Open.addActionListener(this);
		toolbar.add(b_Open);
    
		/* code for icon change
    	b_Open.setRolloverIcon(new ImageIcon("fileopen.png"));
    	b_Open.setPressedIcon(...);
		 */

		JButton zoomin = new JButton(theme.getIcon("zoomin"));
		toolbar.add(zoomin);
		JButton zoomout = new JButton(theme.getIcon("zoomout"));
		toolbar.add(zoomout);
		return toolbar;
	}

	

    // User actions broadcast Events. Depending on the source, they're ActionEvents (menu item) or ItemEvents (checkbox)
    public void actionPerformed(ActionEvent e) {
        JComponent source = (JComponent)(e.getSource());
    	if (source == m_Exit) {
    		System.exit(0);
    	} else if (source == m_Open || source == b_Open) {
    		// open file dialog, send file to tabs
    		JFileChooser fc = new JFileChooser(lastDir);
    		int returnVal = fc.showOpenDialog(source);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	File selected = fc.getSelectedFile();
                c.open(selected);
                try {
					lastDir = selected.getCanonicalPath();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
    
    /**
     * The preferences dialog allows to set (for the time being: global)
     * preferences. It does not store them directly, 
     * they are stored in the content model
     * 
     * The offered I/O functions (import/export from/to file) are handled 
     * within the GUI
     * 
     * TODO is it inefficient to build the window each time it is called?
     * how to circumvent?
     * 
     */
	public void prefDialog () {		
		System.err.println("Preferences window opened");
		
		final JDialog window = new JDialog();
		final JPanel root = new JPanel();
		window.add(root);
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		
		JPanel field;
		
		try {
			String[] fields = c.getModel().getPrefs().keys();
			for (int i = 0; i < fields.length; i++) {
				field = new JPanel();
				root.add(field);		
				field.add(new JLabel(fields[i]));
				field.add(new JTextField(c.getModel().getPrefs().get(fields[i], "")));
				
			}
		} catch (BackingStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		final JPanel buttons = new JPanel();
		root.add(buttons);
		
		final JButton b_import = new JButton("Import");

		buttons.add(b_import);

		final JButton b_export = new JButton("Export");
		buttons.add(b_export);

		final JButton b_cancel = new JButton("Cancel");
		buttons.add(b_cancel);

		final JButton b_ok = new JButton("OK");
		buttons.add(b_ok);

		final ActionListener prefWindowListener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
            	
            	JButton source = (JButton) e.getSource();
            	if (source == b_import) {
            		System.err.println("Importing Preferences");
            		// open file dialog
            		JFileChooser fc = new JFileChooser();
            		int returnVal = fc.showOpenDialog((JButton) e.getSource());

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                	    InputStream is = null;
                	    try {
                	        is = new BufferedInputStream(
                	        		new FileInputStream(fc.getSelectedFile()));
                	    } catch (FileNotFoundException exception) {
                	    }
                	    
                	    // Import preference data
                	    try {
                	    	// load
                	        Preferences.importPreferences(is);
                        	Preferences prefs = c.getModel().getPrefs();
                	        // TODO update display 
                	        
                    		try {
                    			String[] fields = prefs.keys();
                    			for (int i = 0; i < fields.length; i++) {
                            		System.err.println("updating value " + 
                            				((JTextField) ((JPanel) root.getComponent(i))
                            				.getComponent(1)).getText() + " for key "
                            				+ fields[i]);
                    				((JTextField) ((JPanel) root.getComponent(i))
                    						.getComponent(1)).setText(prefs.
                    						get(fields[i], ""));
                    				// (default should probably be the old value (if 
                    				// not overridden))
                    				
                    			}
                    		} catch (BackingStoreException e1) {
                    			// TODO Auto-generated catch block
                    			e1.printStackTrace();
                    		}

                	        
                	        
                	    } catch (InvalidPreferencesFormatException exception) {
                    		System.err.println("Wrong format. Ignored.");
                	    } catch (IOException exception) {
                	    }
                    } else {
                    	// file could not be opened. doing nothing might be appropriate
                    }
            	} else if (source == b_export) {
                	// first: save
                	savePref();
            		System.err.println("Exporting Preferences");
            		JFileChooser fc = new JFileChooser();
            		int returnVal = fc.showOpenDialog((JButton) e.getSource());

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                	    try {
            	    	    // Export the node to a file
                	    	c.getModel().getPrefs().exportNode(
                	    			new FileOutputStream(fc.getSelectedFile()));
                	    } catch (IOException exception) {
                	    } catch (BackingStoreException exception) {
                	    }
                    } else {
                    	// do nothing
                    }

            	} else if (source == b_ok) {
                	savePref();
            		window.dispose();
            	} else if (source == b_cancel) {
//            		window.setVisible(false);
            		window.dispose();
            	}
            }
        	/**
        	 * outsourced from preferences window since needed twice
        	 * reads the first n entries in the window assuming they
        	 * mirror the n entries of the model's prefs
        	 * 
        	 * 
        	 * @param root
        	 */
        	private void savePref () {
        		System.err.println("Preferences being saved (for this session)");
        		// save preferences
        		try {
        			String[] fields = c.getModel().getPrefs().keys();
        			for (int i = 0; i < fields.length; i++) {
                		System.err.println("saving value " + 
                				((JTextField) ((JPanel) root.getComponent(i))
                				.getComponent(1)).getText() + " for key "
                				+ fields[i]);
        				c.getModel().getPrefs().put(fields[i],
        						((JTextField) ((JPanel) root.getComponent(i))
        						.getComponent(1)).getText());
        				
        			}
        		} catch (BackingStoreException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}

        	}


		};

		
		b_import.addActionListener(prefWindowListener);
		b_export.addActionListener(prefWindowListener);
		b_cancel.addActionListener(prefWindowListener);
		b_ok.addActionListener(prefWindowListener);
		
		// "escape closes window"
		root.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true),
				"Copy" );
		root.getActionMap().put( "Copy", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
        		System.err.println("Preferences window aborted");
        		window.dispose();
            }
		});

		// "return saves and closes window"
		root.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
		"OK" );
		root.getActionMap().put( "OK", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				
				// TODO the following is a copy from savePrefs().
				// where to store this to have it accessible from both places? 
        		System.err.println("Preferences being saved (for this session)");
        		// save preferences
        		try {
        			String[] fields = c.getModel().getPrefs().keys();
        			for (int i = 0; i < fields.length; i++) {
                		System.err.println("saving value " + 
                				((JTextField) ((JPanel) root.getComponent(i))
                				.getComponent(1)).getText() + " for key "
                				+ fields[i]);
        				c.getModel().getPrefs().put(fields[i],
        						((JTextField) ((JPanel) root.getComponent(i))
        						.getComponent(1)).getText());
        				
        			}
        		} catch (BackingStoreException e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
				
				
				window.dispose();
			}
		});


        root.registerKeyboardAction(
        		new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
//                    	savePref(root);
                		window.dispose();
                    }
                }, 
        		KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

		
		window.pack();
		window.setVisible(true);

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

        // always needed: list observer (registers with model in its constructor)
    	ContentObserver list = new ListContentObserver(c.getModel());
        
        if (mode == FRAMES) {
        
        	//Create a split pane with the two scroll panes in it.
        	JSplitPane content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); 
        
        	content.setOneTouchExpandable(true);
        	content.setDividerLocation(150);

        	//Provide a preferred size for the split pane.
        	content.setPreferredSize(new Dimension(500, 250));
        
        	frame.add(content, BorderLayout.CENTER);
        
        	content.add(list.getDisplay());

        	// frame observer (registers with model in its constructor)
        	ContentObserver frames = new FramesContentObserver(c.getModel());

        	// add list to GUI
        	content.add(frames.getDisplay());
    	
        } else if (mode == WINDOWS) {
        	
        	// alternative: external windows. no split
        	//ContentObserver frames = 
        		new WindowsContentObserver(c.getModel());
        	frame.add(list.getDisplay());
//        	frame.add(frames.getDisplay());
        	
        	
        }
        
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
