package gralej.gui.prefsdialog;

import gralej.prefs.GralePreferences;
import gralej.prefs.GralePrefsInitException;

import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Eclipse-generated preferences dialog with some human modifications
 * @author no
 * @version $Id$
 */public class GenDialog extends JDialog {

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
		this.setSize(570, 270);
		this.setTitle("GraleJ Preferences");
		this.setContentPane(getJContentPane());
		
		GralePreferences prefs = GralePreferences.getInstance();
		
		// set list model
		DefaultListModel lmodel = new DefaultListModel();
		tabsList.setModel(lmodel);

		// add one tab
		JComponent panel1 = new AvmDisplayOptsPane(prefs);
		lmodel.addElement("AVM Display");
		tabsPanel.add("AVM Display", panel1);

		// add another tab
		JComponent panel2 = new JLabel("Empty right now");
		lmodel.addElement("Empty Panel");
		tabsPanel.add("Empty Panel", panel2);
		
		// marry listener
		tabsList.setSelectedIndex(0);
		tabsList.addListSelectionListener(new tabSelectListener());
		
		
	}
	
	/**
	 * listener invoked when user clicks on tab list
	 */
	private class tabSelectListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			CardLayout cl = (CardLayout)(tabsPanel.getLayout());
		    cl.show(tabsPanel, (String)tabsList.getSelectedValue());
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
			jContentPane.setLayout(new BoxLayout(getJContentPane(), BoxLayout.Y_AXIS));
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
			//tabsPanel.setLayout();
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

}  //  @jve:decl-index=0:visual-constraint="34,16"
