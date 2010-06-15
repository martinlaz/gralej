/*
 *  $Id$
 *
 *  Author:
 *     Martin Lazarov [mlazarov at sfs.uni-tuebingen.de]
 *     
 *  This file is part of the Gralej system
 *     http://code.google.com/p/gralej/
 *
 *  Gralej is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gralej is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */


/*
 * OptionsDialog.java
 *
 * Created on 16. September 2008, 14:50
 * 
 * $Id$
 */

package gralej.gui;

import gralej.Config;
import gralej.blocks.configurator.BlockConfiguratorDialog;
import gralej.util.Log;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TreeSet;
import javax.swing.ButtonModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author  Martin
 */
public class OptionsDialog extends javax.swing.JDialog {
    private Config _cfg;
    private final String[] LOG_MESSAGE_MODES = new String[] {
        "popup", "stderr", "file", "ignore"
    };
    private TableModelListener _advTabOptsListener;
    
    /** Creates new form OptionsDialog */
    public OptionsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        _cfg = new Config(Config.currentConfig());
        initComponents();
        initAdvancedOptionsTable();
        initComboBoxes();
        setLocationRelativeTo(parent);
    }
    
    private void initAdvancedOptionsTable() {
        DefaultTableModel tm = (DefaultTableModel) _tabAdvOpts.getModel();
        
        for (String key : new TreeSet<String>(_cfg.keySet()))
            tm.addRow(new Object[] { key, _cfg.get(key)});
        
        _cfg.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                TableModel tm = _tabAdvOpts.getModel();
                tm.removeTableModelListener(_advTabOptsListener);
                try {
                    for (int row = 0; row < tm.getRowCount(); ++row) {
                        String key = (String) tm.getValueAt(row, 0);
                        tm.setValueAt(_cfg.get(key), row, 1);
                    }
                }
                finally {
                    tm.addTableModelListener(_advTabOptsListener);
                }
            }
        });
        
        _advTabOptsListener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                handleTableModelEvent(e);
            }
        };
        
        _tabAdvOpts.getModel().addTableModelListener(_advTabOptsListener);
    }
    
    private void initComboBoxes() {
        initComboBox(_cboJavaLF, "gui.l+f.java-l+f", getJavaLookAndFeels());
        initComboBox(_cboIconTheme, "gui.l+f.icontheme", "crystal", "traditional");
        initComboBox(_cboLogCritical, "log.message.critical", LOG_MESSAGE_MODES);
        initComboBox(_cboLogError, "log.message.error", LOG_MESSAGE_MODES);
        initComboBox(_cboLogWarning, "log.message.warning", LOG_MESSAGE_MODES);
        initComboBox(_cboLogDebug, "log.message.debug", LOG_MESSAGE_MODES);
        initComboBox(_cboLogInfo, "log.message.info", LOG_MESSAGE_MODES);
    }
    
    private void initComboBox(final JComboBox cb, final String key, String... options) {
        for (String option : options)
            cb.addItem(option);
        
        cb.setSelectedItem(_cfg.get(key));
        cb.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _cfg.set(key, (String)cb.getSelectedItem());
            }
        });
        
        new Config.KeyObserver(_cfg, key) {
            protected void keyChanged() { cb.setSelectedItem(_val); }
        };
    }
    
    private String[] getJavaLookAndFeels() {
        UIManager.LookAndFeelInfo[] lfs = UIManager.getInstalledLookAndFeels();
        String[] a = new String[lfs.length + 1];
        a[0] = "System Default";
        for (int i = 0; i < lfs.length; ++i)
            a[i + 1] = lfs[i].getClassName();
        return a;
    }
    
    private ButtonModel checkBoxModel(final String key) {
        final ButtonModel bm = new JToggleButton.ToggleButtonModel() {
            public void setSelected(boolean b) {
                _cfg.set(key, b);
                super.setSelected(b); 
            }
        };
        bm.setSelected(_cfg.getBool(key));
        new Config.KeyObserver(_cfg, key) {
            protected void keyChanged() { bm.setSelected(_cfg.getBool(key)); }
        };
        return bm;
    }
    
    private void handleTableModelEvent(TableModelEvent e) {
        if (e.getType() != TableModelEvent.UPDATE) return;
        String key = (String) _tabAdvOpts.getModel().getValueAt(e.getFirstRow(), 0);
        String val = (String) _tabAdvOpts.getModel().getValueAt(e.getFirstRow(), 1);
        _cfg.set(key, val);
    }

    private void applyChanges() {
        if (_tabAdvOpts.isEditing())
            _tabAdvOpts.getCellEditor().stopCellEditing(); // ... and accept changes
        Config.currentConfig().updateFrom(_cfg);
        Config.currentConfig().save();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        _pGeneral = new javax.swing.JPanel();
        _chkAutoOpenWindows = new javax.swing.JCheckBox();
        _chkAutoExpandTags = new javax.swing.JCheckBox();
        _chkDisplayHidden = new javax.swing.JCheckBox();
        _chkAVMsInitiallyVisible = new javax.swing.JCheckBox();
        _chkOutputLatexSnippet = new javax.swing.JCheckBox();
        _chkModeGrale = new javax.swing.JCheckBox();
        _chkAutoResize = new javax.swing.JCheckBox();
        _pLookAndFeel = new javax.swing.JPanel();
        _chkSelectOnClick = new javax.swing.JCheckBox();
        _chkSelectOnHover = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        _cboJavaLF = new javax.swing.JComboBox();
        _cboIconTheme = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        _bOpenAMVTreeConfigurator = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        _pLogging = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        _cboLogCritical = new javax.swing.JComboBox();
        _cboLogError = new javax.swing.JComboBox();
        _cboLogWarning = new javax.swing.JComboBox();
        _cboLogDebug = new javax.swing.JComboBox();
        _cboLogInfo = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        _pConfirmations = new javax.swing.JPanel();
        _chkConfirmAppExit = new javax.swing.JCheckBox();
        _pAdvanced = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        _tabAdvOpts = new javax.swing.JTable();
        _bCancel = new javax.swing.JButton();
        _bOk = new javax.swing.JButton();
        _bImport = new javax.swing.JButton();
        _bExport = new javax.swing.JButton();
        _bDefaults = new javax.swing.JButton();
        _bApply = new javax.swing.JButton();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Options");

        _chkAutoOpenWindows.setModel(checkBoxModel("behavior.openonload"));
        _chkAutoOpenWindows.setText("Automatically open windows as new data arrive");

        _chkAutoExpandTags.setModel(checkBoxModel("behavior.autoexpandtags"));
        _chkAutoExpandTags.setText("Ensure that each tag in an AVM is expanded at least once");

        _chkDisplayHidden.setModel(checkBoxModel("behavior.displayModelHiddenFeatures"));
        _chkDisplayHidden.setText("Display features marked as hidden by TRALE");

        _chkAVMsInitiallyVisible.setModel(checkBoxModel("behavior.nodeContentInitiallyVisible"));
        _chkAVMsInitiallyVisible.setText("Make AVMs initially visible (in the tree display)");

        _chkOutputLatexSnippet.setModel(checkBoxModel("output.latex.snippet"));
        _chkOutputLatexSnippet.setText("Output LaTeX files as snippets");

        _chkModeGrale.setModel(checkBoxModel("mode.grale"));
        _chkModeGrale.setText("Start Gralej in GRALE compatibility mode");

        _chkAutoResize.setModel(checkBoxModel("behavior.alwaysfitsize"));
        _chkAutoResize.setText("Resize windows to fit trees and AVMs");

        javax.swing.GroupLayout _pGeneralLayout = new javax.swing.GroupLayout(_pGeneral);
        _pGeneral.setLayout(_pGeneralLayout);
        _pGeneralLayout.setHorizontalGroup(
            _pGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(_pGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(_pGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(_chkAutoResize)
                    .addComponent(_chkAutoExpandTags)
                    .addComponent(_chkAutoOpenWindows)
                    .addComponent(_chkDisplayHidden)
                    .addComponent(_chkAVMsInitiallyVisible)
                    .addComponent(_chkOutputLatexSnippet)
                    .addComponent(_chkModeGrale))
                .addContainerGap(107, Short.MAX_VALUE))
        );
        _pGeneralLayout.setVerticalGroup(
            _pGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(_pGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(_chkAutoResize, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_chkAutoOpenWindows, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_chkAutoExpandTags, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_chkDisplayHidden, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_chkAVMsInitiallyVisible, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_chkOutputLatexSnippet, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_chkModeGrale, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("General", _pGeneral);

        _chkSelectOnClick.setModel(checkBoxModel("behavior.selectOnClick"));
        _chkSelectOnClick.setText("<html>Select content labels on mouse <b>click</b><br><i>(double click expands contents)");
        _chkSelectOnClick.setToolTipText("");
        _chkSelectOnClick.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        _chkSelectOnHover.setModel(checkBoxModel("behavior.selectOnHover"));
        _chkSelectOnHover.setText("<html>Select content labels on mouse <b>hover</b><br><i>(single click expands contents)");
        _chkSelectOnHover.setToolTipText("This option has priority over the upper one");
        _chkSelectOnHover.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jLabel1.setText("Java Look & Feel:");

        jLabel2.setText("Icon theme:");

        jLabel3.setText("AVM tree:");

        _bOpenAMVTreeConfigurator.setText("Open configurator...");
        _bOpenAMVTreeConfigurator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _bOpenAMVTreeConfiguratorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout _pLookAndFeelLayout = new javax.swing.GroupLayout(_pLookAndFeel);
        _pLookAndFeel.setLayout(_pLookAndFeelLayout);
        _pLookAndFeelLayout.setHorizontalGroup(
            _pLookAndFeelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, _pLookAndFeelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(_pLookAndFeelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, _pLookAndFeelLayout.createSequentialGroup()
                        .addGroup(_pLookAndFeelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addGroup(_pLookAndFeelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(_pLookAndFeelLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(_cboJavaLF, 0, 378, Short.MAX_VALUE))
                            .addGroup(_pLookAndFeelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(_pLookAndFeelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(_cboIconTheme, 0, 377, Short.MAX_VALUE)
                                    .addComponent(_bOpenAMVTreeConfigurator)))))
                    .addComponent(_chkSelectOnClick, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(_chkSelectOnHover, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        _pLookAndFeelLayout.setVerticalGroup(
            _pLookAndFeelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(_pLookAndFeelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(_pLookAndFeelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(_cboJavaLF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(_pLookAndFeelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(_cboIconTheme, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(_pLookAndFeelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(_bOpenAMVTreeConfigurator))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(_chkSelectOnClick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(_chkSelectOnHover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        _chkSelectOnHover.getAccessibleContext().setAccessibleDescription("");

        jTabbedPane1.addTab("Look & Feel", _pLookAndFeel);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel4.setText("Message Type");

        jLabel5.setText("Critical:");

        jLabel6.setText("Error:");

        jLabel7.setText("Warning:");

        jLabel8.setText("Debug:");

        jLabel9.setText("Informational:");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel10.setText("Handling");

        javax.swing.GroupLayout _pLoggingLayout = new javax.swing.GroupLayout(_pLogging);
        _pLogging.setLayout(_pLoggingLayout);
        _pLoggingLayout.setHorizontalGroup(
            _pLoggingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(_pLoggingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(_pLoggingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                    .addGroup(_pLoggingLayout.createSequentialGroup()
                        .addGroup(_pLoggingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(_pLoggingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(_cboLogCritical, 0, 385, Short.MAX_VALUE)
                            .addComponent(_cboLogError, 0, 385, Short.MAX_VALUE)
                            .addComponent(_cboLogWarning, 0, 385, Short.MAX_VALUE)
                            .addComponent(_cboLogDebug, 0, 385, Short.MAX_VALUE)
                            .addComponent(_cboLogInfo, 0, 385, Short.MAX_VALUE))))
                .addContainerGap())
        );
        _pLoggingLayout.setVerticalGroup(
            _pLoggingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(_pLoggingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(_pLoggingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(_pLoggingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(_cboLogCritical, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(_pLoggingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(_cboLogError, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(_pLoggingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(_cboLogWarning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(_pLoggingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(_cboLogDebug, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(_pLoggingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(_cboLogInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Logging", _pLogging);

        _chkConfirmAppExit.setModel(checkBoxModel("behavior.confirm.exit"));
        _chkConfirmAppExit.setText("Application exit");

        javax.swing.GroupLayout _pConfirmationsLayout = new javax.swing.GroupLayout(_pConfirmations);
        _pConfirmations.setLayout(_pConfirmationsLayout);
        _pConfirmationsLayout.setHorizontalGroup(
            _pConfirmationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(_pConfirmationsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(_chkConfirmAppExit)
                .addContainerGap(378, Short.MAX_VALUE))
        );
        _pConfirmationsLayout.setVerticalGroup(
            _pConfirmationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(_pConfirmationsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(_chkConfirmAppExit)
                .addContainerGap(221, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Confirmations", _pConfirmations);

        _tabAdvOpts.setAutoCreateRowSorter(true);
        _tabAdvOpts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Key", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        _tabAdvOpts.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(_tabAdvOpts);

        javax.swing.GroupLayout _pAdvancedLayout = new javax.swing.GroupLayout(_pAdvanced);
        _pAdvanced.setLayout(_pAdvancedLayout);
        _pAdvancedLayout.setHorizontalGroup(
            _pAdvancedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(_pAdvancedLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                .addContainerGap())
        );
        _pAdvancedLayout.setVerticalGroup(
            _pAdvancedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(_pAdvancedLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Advanced", _pAdvanced);

        _bCancel.setText("Close");
        _bCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _bCancelActionPerformed(evt);
            }
        });

        _bOk.setText("OK");
        _bOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _bOkActionPerformed(evt);
            }
        });

        _bImport.setText("Import...");
        _bImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _bImportActionPerformed(evt);
            }
        });

        _bExport.setText("Export...");
        _bExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _bExportActionPerformed(evt);
            }
        });

        _bDefaults.setText("Defaults");
        _bDefaults.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _bDefaultsActionPerformed(evt);
            }
        });

        _bApply.setText("Apply");
        _bApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _bApplyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(_bImport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_bExport)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_bDefaults)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                        .addComponent(_bOk)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_bApply)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_bCancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(_bCancel)
                    .addComponent(_bImport)
                    .addComponent(_bExport)
                    .addComponent(_bDefaults)
                    .addComponent(_bApply)
                    .addComponent(_bOk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void _bOpenAMVTreeConfiguratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__bOpenAMVTreeConfiguratorActionPerformed
        new BlockConfiguratorDialog(this, true, _cfg).setVisible(true);
}//GEN-LAST:event__bOpenAMVTreeConfiguratorActionPerformed

    private void _bImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__bImportActionPerformed
        JFileChooser fc = new JFileChooser(new File(_cfg.get("input.lastdir")));
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        try {
            _cfg.set("input.lastdir", f.getParentFile().getCanonicalPath(), false);
        }
        catch (IOException e) {}
        
        try {
            InputStream fis = new FileInputStream(f);
            _cfg.importFrom(fis);
            fis.close();
        }
        catch (IOException e) {
            Log.error(e);
        }
}//GEN-LAST:event__bImportActionPerformed

    private void _bExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__bExportActionPerformed
        JFileChooser fc = new JFileChooser(new File(_cfg.get("input.lastdir")));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        try {
            _cfg.set("input.lastdir", f.getParentFile().getCanonicalPath(), false);
        }
        catch (IOException e) {}
        
        if (f.exists()) {
            if (JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to overwrite this file?",
                    "Export",
                    JOptionPane.YES_NO_OPTION
                    )
                    != JOptionPane.YES_OPTION) return;
        }
        
        try {
            OutputStream fos = new FileOutputStream(f);
            _cfg.exportTo(fos);
            fos.close();
        }
        catch (IOException e) {
            Log.error(e);
        }
    }//GEN-LAST:event__bExportActionPerformed

    private void _bDefaultsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__bDefaultsActionPerformed
        _cfg.updateFrom(Config.defaultConfig());
}//GEN-LAST:event__bDefaultsActionPerformed

    private void _bOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__bOkActionPerformed
        applyChanges();
        dispose();
    }//GEN-LAST:event__bOkActionPerformed

    private void _bCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__bCancelActionPerformed
        dispose();
    }//GEN-LAST:event__bCancelActionPerformed

    private void _bApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event__bApplyActionPerformed
        applyChanges();
    }//GEN-LAST:event__bApplyActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                OptionsDialog dialog = new OptionsDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton _bApply;
    private javax.swing.JButton _bCancel;
    private javax.swing.JButton _bDefaults;
    private javax.swing.JButton _bExport;
    private javax.swing.JButton _bImport;
    private javax.swing.JButton _bOk;
    private javax.swing.JButton _bOpenAMVTreeConfigurator;
    private javax.swing.JComboBox _cboIconTheme;
    private javax.swing.JComboBox _cboJavaLF;
    private javax.swing.JComboBox _cboLogCritical;
    private javax.swing.JComboBox _cboLogDebug;
    private javax.swing.JComboBox _cboLogError;
    private javax.swing.JComboBox _cboLogInfo;
    private javax.swing.JComboBox _cboLogWarning;
    private javax.swing.JCheckBox _chkAVMsInitiallyVisible;
    private javax.swing.JCheckBox _chkAutoExpandTags;
    private javax.swing.JCheckBox _chkAutoOpenWindows;
    private javax.swing.JCheckBox _chkAutoResize;
    private javax.swing.JCheckBox _chkConfirmAppExit;
    private javax.swing.JCheckBox _chkDisplayHidden;
    private javax.swing.JCheckBox _chkModeGrale;
    private javax.swing.JCheckBox _chkOutputLatexSnippet;
    private javax.swing.JCheckBox _chkSelectOnClick;
    private javax.swing.JCheckBox _chkSelectOnHover;
    private javax.swing.JPanel _pAdvanced;
    private javax.swing.JPanel _pConfirmations;
    private javax.swing.JPanel _pGeneral;
    private javax.swing.JPanel _pLogging;
    private javax.swing.JPanel _pLookAndFeel;
    private javax.swing.JTable _tabAdvOpts;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
    
}
