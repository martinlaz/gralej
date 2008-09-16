package gralej.gui.prefsdialog;

import gralej.gui.icons.IconTheme;
import gralej.gui.icons.IconThemeFactory;
import gralej.prefs.GralePreferences;
import gralej.prefs.GralePrefsInitException;
import gralej.prefs.ObserverListMemento;

import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.prefs.BackingStoreException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

/**
 * Eclipse-generated preferences dialog with some human modifications
 * 
 * @author no
 * @version $Id$
 */
public class GenDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JPanel jPanel = null;

    private JPanel jPanel1 = null;

    private JPanel jPanel2 = null;

    private JButton ImportButton = null;

    private JButton DefaultsButton = null;

    private JPanel jPanel3 = null;

    private JButton CancelButton = null;

    private JButton OKButton = null;

    private JButton ExportButton = null;

    private JSplitPane jSplitPane = null;

    private JPanel tabsPanel = null;

    private JList tabsList = null;

    private IconTheme icontheme;

    private GralePreferences prefs;
    
    private ObserverListMemento observers;
    
    private AdvancedOptsPane advOptsPane;

    /**
     * @param owner
     * @throws GralePrefsInitException
     */
    public GenDialog(Frame owner) throws GralePrefsInitException {
        super(owner);
        initialize();
    }

    /**
     * This method initializes this
     * 
     * @return void
     * @throws GralePrefsInitException
     */
    private void initialize() throws GralePrefsInitException {
        this.setSize(750, 450);
        this.setLocationRelativeTo(getParent());
        this.setTitle("Gralej Preferences");
        this.setContentPane(getJContentPane());
        this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        		//ModalExclusionType.APPLICAION_EXCLUDED);

        // get preferences and initialize observer things
        prefs = GralePreferences.getInstance();
        observers = prefs.getObservers();
        prefs.setSync(false);
        prefs.removeAllListeners();
        
        // get icon theme
        icontheme = IconThemeFactory.getIconTheme(prefs
                .get("gui.l+f.icontheme"));

        // set icons to buttons and the dialog
        CancelButton.setIcon(icontheme.getIcon("cancel"));
        OKButton.setIcon(icontheme.getIcon("ok"));
        ImportButton.setIcon(icontheme.getIcon("fileopen"));
        ExportButton.setIcon(icontheme.getIcon("filefloppy"));
        DefaultsButton.setIcon(icontheme.getIcon("dummy.v"));
        ImageIcon icon = icontheme.getIcon("configure");
        Image image = icon.getImage();
        setIconImage(image);

        // set list model
        DefaultListModel lmodel = new DefaultListModel();
        tabsList.setModel(lmodel);

        ImageIcon listicons[] = new ImageIcon[4];
        String[] listlabels = new String[4];

        // add one tab
        JComponent panel0 = new BehaviorOptsPane(prefs);
        listlabels[0] = "Behavior";
        lmodel.addElement("0");
        tabsPanel.add("0", panel0);
        listicons[0] = icontheme.getIcon("large-configure");

        // one more tab
        JComponent panel1 = new AvmDisplayOptsPane(prefs);
        listlabels[1] = "AVM Display";
        lmodel.addElement("1");
        tabsPanel.add("1", panel1);
        listicons[1] = icontheme.getIcon("large-showstruc");

        // add another tab
        JComponent panel2 = new LookAndFeelOptsPane(prefs);
        listlabels[2] = "Look & Feel";
        lmodel.addElement("2");
        tabsPanel.add("2", panel2);
        listicons[2] = icontheme.getIcon("large-l+f");
        
        advOptsPane = new AdvancedOptsPane(prefs);
        JComponent panel3 = advOptsPane;
        listlabels[3] = "Advanced Settings";
        lmodel.addElement("3");
        tabsPanel.add("3", panel3);
        listicons[3] = icontheme.getIcon("large-advanced-settings");
        

        // attach the icon based cell renderer
        tabsList.setCellRenderer(new ImageListCellRenderer(listicons,
                listlabels));

        // marry listeners
        tabsList.setSelectedIndex(0);
        tabsList.addListSelectionListener(new tabSelectListener());
        ButtonClickListener buttonclick = new ButtonClickListener(this);
        OKButton.addActionListener(buttonclick);
        CancelButton.addActionListener(buttonclick);
        ImportButton.addActionListener(buttonclick);
        ExportButton.addActionListener(buttonclick);
        DefaultsButton.addActionListener(buttonclick);

        // set proper close handler
        addWindowListener(new WindowEventHandler());

    }

    /**
     * listener invoked when user clicks on tab list
     */
    private class tabSelectListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent e) {
            CardLayout cl = (CardLayout) (tabsPanel.getLayout());
            cl.show(tabsPanel, (String) tabsList.getSelectedValue());
        }

    }

    /** 
     * An internal handler allowing to listen to 
     * the closing of a window
     */
    private class WindowEventHandler extends WindowAdapter {

		/* (non-Javadoc)
		 * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
		 */
		@Override
		public void windowClosing(WindowEvent e) {
			closeWindow(false);
		}
    }
    
    private void closeWindow(Boolean doSave) {
    	prefs.removeAllListeners();
    	prefs.setObservers(observers);
    	prefs.setSync(true);
    	if ( doSave ) {
    		prefs.syncToBackend();
    	}
    	this.setVisible(false);
    	this.dispose();
    }

    /**
     * Simplistic file format filter used for storing/saving
     */
    class Filter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.getName().endsWith(".gralecfg");
		}

		@Override
		public String getDescription() {
			return "Gralej Configuration Files";
		}
    }
    
    public File saveDialog() {
    	JFileChooser fc = new JFileChooser(prefs.get("input.lastdir"));
    	fc.setMultiSelectionEnabled(false);
    	// fc.setAcceptAllFileFilterUsed(false);
    	fc.addChoosableFileFilter(new Filter());
    	int returnVal = fc.showSaveDialog(null);

    	if (returnVal == JFileChooser.APPROVE_OPTION) {
    		File f = fc.getSelectedFile();
    		try{
    			prefs.put("input.lastdir", f.getCanonicalPath());
    		} catch (IOException e1) {
    			JOptionPane.showMessageDialog(this,
    					"Cannot save due to I/O error!",
    					"Exporting Preferences", JOptionPane.ERROR_MESSAGE);
    			e1.printStackTrace();
    			return null;
    		}

    		if (!f.exists() || JOptionPane.showConfirmDialog(null,
    				"File exists. Overwrite?", "Exporting Preferences",
    				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
    			return f;
    		}
    	}
    	return null;
    }
    
    public File openDialog() {
    	JFileChooser fc = new JFileChooser(prefs.get("input.lastdir"));
    	fc.setMultiSelectionEnabled(false);
    	// fc.setAcceptAllFileFilterUsed(false);
    	fc.addChoosableFileFilter(new Filter());
    	int returnVal = fc.showOpenDialog(null);

    	if (returnVal == JFileChooser.APPROVE_OPTION) {
    		File f = fc.getSelectedFile();
    		try{
    			prefs.put("input.lastdir", f.getCanonicalPath());
    		} catch (IOException e1) {
    			JOptionPane.showMessageDialog(this,
    					"Cannot load due to I/O error!",
    					"Importing Preferences", JOptionPane.ERROR_MESSAGE);
    			e1.printStackTrace();
    			return null;
    		}
    		
    		return f;

    	}
    	return null;
    }    

    /**
     * listener invoke when the user clicks on of the buttons like cancel or OK
     */
    private class ButtonClickListener implements ActionListener {

        GenDialog parent;

        public ButtonClickListener(GenDialog parent) {
            this.parent = parent;
        }
        
        private void loadDefaults() {
        	// ask the user for confirmation
        	int i = JOptionPane.showConfirmDialog(parent, 
        		"Do you really want to load the factory settings?\n\n",
        		"Load Defaults", 
        		JOptionPane.YES_NO_OPTION);
        	// if the user answers "YES", try to restore the defaults
        	if ( i == JOptionPane.YES_OPTION ) {
        		try {
					prefs.restoreDefaults();
				} catch (BackingStoreException e1) {
	                JOptionPane.showMessageDialog(parent,
                    "Loading the default settings failed due to an error during " +
                    "the communication with Java's configuration registry.",
                    "Load Defaults", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
        	}        	
        }
        
        private void exportSettings() {
        	
        	File f = saveDialog();
        	
        	// abort if user didn't select a file
        	if ( f == null ) {
        		return;
        	}

        	try {
        		// append .gralecfg extension if necessary
        		if (! f.getName().endsWith(".gralecfg") ) {
        			f = new File(f.getCanonicalPath() + ".gralecfg");
        		}

        		prefs.saveToXML(f);
        	} catch (Exception e) {
        		JOptionPane.showMessageDialog(parent,				
        				"An error occured while exporting preferences.\n\n" +
        				"The system said:\n" +
        				e.getMessage(),
        				"Exporting Preferences", JOptionPane.ERROR_MESSAGE);
        		e.printStackTrace();
        	}	
        	
        }
        
        private void loadSettings() {
        	
        	File f = openDialog();

        	// abort if user didn't select a file
        	if ( f == null ) {
        		return;
        	}

        	
        	try {
				prefs.loadFromXML(f);
        	} catch (Exception e) {
        		JOptionPane.showMessageDialog(parent,				
        				"An error occured while importing preferences.\n\n" +
        				"The system said:\n" +
        				e.getMessage(),
        				"Importing Preferences", JOptionPane.ERROR_MESSAGE);
        		e.printStackTrace();
        	}	
        	
        }
        

        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == OKButton) {
            	/*JOptionPane.showMessageDialog(parent,
                        "As this is only a prototype, options are not being saved on clicking OK.",
                        "Prototype Limitation", JOptionPane.INFORMATION_MESSAGE);
            	
            	closeWindow(false);
            	*/
                if (advOptsPane.table.isEditing())
                    advOptsPane.table.getCellEditor().stopCellEditing();
                
            	closeWindow(true);
                // laz: ugly hack until we fix preferences
                //gralej.blocks.BlockPanelStyle.getInstance().preferencesChange();
            } else if (e.getSource() == CancelButton) {
                closeWindow(false);
            } else if (e.getSource() == ImportButton) {
            	loadSettings();
            } else if (e.getSource() == ExportButton) {
                exportSettings();
            } else if (e.getSource() == DefaultsButton) {
            	loadDefaults();
            }	

        }

    }




    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BoxLayout(getJContentPane(),
                    BoxLayout.Y_AXIS));
            jContentPane.add(getJPanel(), null);
            jContentPane.add(getJPanel1(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.BOTH;
            gridBagConstraints11.weighty = 1.0;
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.gridy = 0;
            gridBagConstraints11.weightx = 1.0;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            jPanel.add(getJSplitPane(), gridBagConstraints11);
        }
        return jPanel;
    }

    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setLayout(new BoxLayout(getJPanel1(), BoxLayout.X_AXIS));
            jPanel1.setBorder(BorderFactory.createEmptyBorder(0, 10, 15, 10));
            jPanel1.add(getJPanel2(), null);
            jPanel1.add(getJPanel3(), null);
        }
        return jPanel1;
    }

    /**
     * This method initializes jPanel2
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel2() {
        if (jPanel2 == null) {
            FlowLayout flowLayout = new FlowLayout();
            flowLayout.setAlignment(java.awt.FlowLayout.LEFT);
            flowLayout.setVgap(5);
            flowLayout.setHgap(5);
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = -1;
            gridBagConstraints1.gridy = -1;
            jPanel2 = new JPanel();
            jPanel2.setLayout(flowLayout);
            jPanel2.add(getImportButton(), null);
            jPanel2.add(getExportButton(), null);
            jPanel2.add(getDefaultsButton(), null);
        }
        return jPanel2;
    }

    /**
     * This method initializes ImportButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getImportButton() {
        if (ImportButton == null) {
            ImportButton = new JButton();
            ImportButton.setText("Import...");
        }
        return ImportButton;
    }

    /**
     * This method initializes DefaultsButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getDefaultsButton() {
        if (DefaultsButton == null) {
            DefaultsButton = new JButton();
            DefaultsButton.setText("Load Defaults");
        }
        return DefaultsButton;
    }

    /**
     * This method initializes jPanel3
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            FlowLayout flowLayout1 = new FlowLayout();
            flowLayout1.setAlignment(java.awt.FlowLayout.RIGHT);
            flowLayout1.setVgap(5);
            flowLayout1.setHgap(5);
            jPanel3 = new JPanel();
            jPanel3.setLayout(flowLayout1);
            jPanel3.add(getCancelButton(), null);
            jPanel3.add(getOKButton(), null);
        }
        return jPanel3;
    }

    /**
     * This method initializes CancelButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getCancelButton() {
        if (CancelButton == null) {
            CancelButton = new JButton();
            CancelButton.setText("Cancel");
        }
        return CancelButton;
    }

    /**
     * This method initializes OKButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getOKButton() {
        if (OKButton == null) {
            OKButton = new JButton();
            OKButton.setText("OK");
        }
        return OKButton;
    }

    /**
     * This method initializes ExportButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getExportButton() {
        if (ExportButton == null) {
            ExportButton = new JButton();
            ExportButton.setText("Export...");
        }
        return ExportButton;
    }

    /**
     * This method initializes jSplitPane
     * 
     * @return javax.swing.JSplitPane
     */
    private JSplitPane getJSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane();
            jSplitPane.setRightComponent(getTabsPanel());
            jSplitPane.setLeftComponent(getTabsList());
        }
        return jSplitPane;
    }

    /**
     * This method initializes tabsPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getTabsPanel() {
        if (tabsPanel == null) {
            tabsPanel = new JPanel(new CardLayout());
            // tabsPanel.setLayout();
        }
        return tabsPanel;
    }

    /**
     * This method initializes tabsList
     * 
     * @return javax.swing.JList
     */
    private JList getTabsList() {
        if (tabsList == null) {
            tabsList = new JList();
            tabsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        return tabsList;
    }

} // @jve:decl-index=0:visual-constraint="34,16"
