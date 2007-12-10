/**
 * 
 */
package gralej.gui;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;

import java.util.prefs.*;


import gralej.controller.*;
import gralej.gui.icons.IconTheme;
import gralej.gui.icons.IconThemeFactory;
import gralej.parsers.OutputFormatter;
import gralej.prefs.GralePreferences;
import gralej.prefs.GralePrefsInitException;

import javax.swing.ImageIcon;

/**
 * 
 * @author Armin
 * @version $Id$
 */
public class MainGUI implements ActionListener, ItemListener {

	// display mode
	static int FRAMES = 0; 
	static int WINDOWS = 1; 
//	private int mode = WINDOWS;
//	private int mode = FRAMES;
	
	private final IconTheme theme = IconThemeFactory.getIconTheme("crystal");
	
	private String lastDir;
	
	private boolean hasSelection = true;


	private Controller c; // the gralej.controller 
	
	// menu items
	private JMenuItem m_Exit, m_Close, m_CloseAll, m_Open, m_Latex, m_Postscript, m_SVG, 
	                  m_Print, m_About, m_Pref, m_Cascade, m_Tile, m_TestFile, m_WebTrale,
	                  m_Save, m_SaveAll, m_XML, m_JPG;
	
	// buttons (basically the same as the menu items)
	private JButton b_Open, b_Close, b_CloseAll, b_Print, b_Save, b_SaveAll;
	
	
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

        m_TestFile = new JMenuItem("Open Sample");
        m_TestFile.addActionListener(this);
        filemenu.add(m_TestFile);

        m_Close = new JMenuItem("Close");
        m_Close.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
        m_Close.addActionListener(this);
        filemenu.add(m_Close);

        m_CloseAll = new JMenuItem("Close All");
        m_CloseAll.addActionListener(this);
        filemenu.add(m_CloseAll);

        m_Save = new JMenuItem("Save");
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
        

        m_Cascade = new JMenuItem("Cascade Windows/Frames");
        m_Cascade.addActionListener(this);
        viewmenu.add(m_Cascade);
        
        m_Tile = new JMenuItem("Tile Windows/Frames");
        m_Tile.addActionListener(this);
        viewmenu.add(m_Tile);
        

        
        viewmenu.addSeparator();

        
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
		
/*		b_SaveAll = new JButton(theme.getIcon("filefloppy"));
        b_SaveAll.addActionListener(this);
		toolbar.add(b_SaveAll);
*/
		
		b_Print = new JButton(theme.getIcon("fileprint"));
        b_Print.addActionListener(this);
        b_Print.setToolTipText("Print");
		toolbar.add(b_Print);
    		
		return toolbar;
	}

	

    // User actions broadcast Events. Depending on the source, they're ActionEvents (menu item) or ItemEvents (checkbox)
    public void actionPerformed(ActionEvent e) {
        JComponent source = (JComponent)(e.getSource());
    	if (source == m_Exit) {
    		System.exit(0);
    	} else if (source == m_Open || source == b_Open) {
    		JFileChooser fc = new JFileChooser(lastDir);
    		fc.setMultiSelectionEnabled(true);
    		int returnVal = fc.showOpenDialog(source);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	File[] files = fc.getSelectedFiles();
//            	File f = fc.getSelectedFile();
            	for (File f : files) {
                    c.open(f);            		
            	}
                try {
					lastDir = files[0].getCanonicalPath();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            } else {
            	// file could not be opened. doing nothing might be appropriate
            }
    	} else if (source == m_TestFile) {
    		   final String resName = "/gralej/resource/sample.GRALE";
    		   InputStream is = getClass().getResourceAsStream(resName);
    		   if (is == null) // should never happen
    		       throw new RuntimeException("Internal program error");
    		   c.newStream(is, new StreamInfo("grisu", resName)); 
    	} else if (source == m_WebTrale) {
    		URL url;
    		try {
				url = new URL(JOptionPane.showInputDialog(null, "Choose server"));
	    		c.startWebTraleClient(url);
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (MalformedURLException e1) {
				System.err.println("Malformed URL");
			}

    	} else if (source == m_Close || source == b_Close) {
    		c.getModel().close();
    	} else if (source == m_CloseAll || source == b_CloseAll) {
    		c.getModel().closeAll();
    	} else if (source == m_Save || source == b_Save) {
    		save(OutputFormatter.TRALEFormat);
    	} else if (source == m_SaveAll || source == b_SaveAll) {
    		File f = saveDialog(OutputFormatter.TRALEFormat);
    		if (f != null) {
    			c.getModel().saveAll(f, OutputFormatter.TRALEFormat);
            } else {
            	// file could not be opened. doing nothing might be appropriate
            }
    	} else if (source == m_Latex) {
    		save(OutputFormatter.LaTeXFormat);
    	} else if (source == m_Postscript) {
    		save(OutputFormatter.PostscriptFormat);
    	} else if (source == m_Print || source == b_Print) {
    		// call print method
    	} else if (source == m_About) {
    		JOptionPane.showMessageDialog(null, "GRALE, Java version (2007)");
    	} else if (source ==  m_SVG) {
    		save(OutputFormatter.SVGFormat);
    	} else if (source ==  m_XML) {
    		save(OutputFormatter.XMLFormat);
		} else if (source ==  m_JPG) {
    		save(OutputFormatter.JPGFormat);
    	} else if (source ==  m_Cascade) {
    		// send "cascade" to viewer
    		c.getModel().cascade();
    	} else if (source ==  m_Tile) {
    		// send "tile" to viewer
    		c.getModel().tile();
    	} else if (source ==  m_Pref) {
    		// open Preferences Dialog
    		prefDialog();
    	}
    }
    
	private void save (int format) {
		File f = saveDialog(format);
		if (f != null) {
			c.getModel().saveAll(f, format);
        } else {
        	// file could not be opened. doing nothing might be appropriate
        }
	}
    
    public File saveDialog (int format) {
    	// TODO implement filter according to format
		JFileChooser fc = new JFileChooser(lastDir);
		fc.setMultiSelectionEnabled(false);
		int returnVal = fc.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File f = fc.getSelectedFile();
            try {
				lastDir = f.getCanonicalPath();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        	if (!f.exists() || JOptionPane.showConfirmDialog(null,
        			"File exists. Overwrite?", "Overwrite?", JOptionPane.YES_NO_OPTION)
        			== JOptionPane.YES_OPTION) {
            	return f;
        	}
        }
    	return null;    	
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


        //Create and set up the window.
        JFrame frame = new JFrame("GraleJ");
        // set program icon
        ImageIcon icon = theme.getIcon("grale");
        Image image = icon.getImage();
        frame.setIconImage(image);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // instantiate menus
        frame.setJMenuBar(this.createMenuBar());
        
        // instantiate toolbar
        frame.getContentPane().add(this.createToolBar(),BorderLayout.NORTH);
        notifyOfSelection(false);
        notifyOfEmptyList(true);

        // content part

        // always needed: list observer (registers with model in its constructor)
    	ContentObserver list = new ListContentObserver(c.getModel(), this);
    	
/*        if (mode == FRAMES) {
        
        	//Create a split pane with the two scroll panes in it.
        	JSplitPane content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); 
        
        	content.setOneTouchExpandable(true);
        	content.setDividerLocation(150);

        	//Provide a preferred size for the split pane.
        	content.setPreferredSize(new Dimension(500, 250));
        
        	frame.getContentPane().add(content, BorderLayout.CENTER);
        
        	content.add(list.getDisplay());

        	// frame observer (registers with model in its constructor)
        	ContentObserver frames = new FramesContentObserver(c.getModel());

        	// add list to GUI
        	content.add(frames.getDisplay());
    	
        } else if (mode == WINDOWS) {
*/        	
        	// alternative: external windows. no split
        	//ContentObserver frames = 
        	new WindowsContentObserver(c.getModel(), theme, this);
        	frame.getContentPane().add(list.getDisplay(), BorderLayout.CENTER);
//        	frame.add(frames.getDisplay());
        	
        	
/*        }
 */
        
        // bottom status line
        // to be implemented
        JPanel statusLine = new JPanel();
        statusLine.add(new JLabel("status line"));
        frame.getContentPane().add(statusLine, BorderLayout.PAGE_END);
        
        frame.pack();
        // TODO size from preferences
        try {
			GralePreferences gp = GralePreferences.getInstance();
	        frame.setSize(gp.getInt("gui.windows.main.size.width"),
	        		gp.getInt("gui.windows.main.size.height"));
		} catch (GralePrefsInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			frame.setSize(400,400);
		}
        frame.setVisible(true);
        
	}
	

	/**
	 * Some menu items depend on a file being selected. This method
	 * is called by the list whenever the selection changes.
	 * 
	 * @param b
	 */
	public void notifyOfSelection(boolean b) {
		if (hasSelection == b) return; // do nothing 
		hasSelection = b;
		
		// menu items
		m_Latex.setEnabled(b);
		m_Close.setEnabled(b);
//		m_CloseAll.setEnabled(b);
		m_Latex.setEnabled(b);
		m_Postscript.setEnabled(b);
		m_SVG.setEnabled(b);
        m_Print.setEnabled(b);
        m_Save.setEnabled(b);
//        m_SaveAll.setEnabled(b); // SaveAll depends on items being in the list, not on selection
        // TODO handle empty/non-empty list
		
        // buttons
    	b_Close.setEnabled(b);
//    	b_CloseAll
    	b_Print.setEnabled(b);
    	b_Save.setEnabled(b);
//    	b_SaveAll;

	}

	public void notifyOfEmptyList(boolean isEmpty) {
		m_SaveAll.setEnabled(!isEmpty);
		m_CloseAll.setEnabled(!isEmpty);
//		b_SaveAll.setEnabled(!isEmpty);
	}



}
