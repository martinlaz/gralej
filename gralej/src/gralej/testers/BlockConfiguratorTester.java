/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gralej.testers;

import gralej.blocks.configurator.BlockConfigurator;
import gralej.blocks.configurator.BlockConfigurator.Handler;
import gralej.blocks.BlockLayout;
import gralej.blocks.LabelStyle;
import gralej.blocks.configurator.BlockLayoutEditor;
import gralej.blocks.configurator.LabelStyleEditor;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Martin
 */
public class BlockConfiguratorTester {

    public static BlockConfigurator instance;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    System.err.println("-- failed to set the system's native look and feel");
                }
                final JLabel messageLabel = new JLabel();
                messageLabel.setText(" ");
                final JFrame f = new JFrame();
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                final BlockConfigurator bc = new BlockConfigurator(new Handler() {
                    
                    LabelStyleEditor labed;
                    BlockLayoutEditor layed;
                    
                    public void modifyLabelStyle(LabelStyle style) {
                        //echo("Will modify label style: " + style.getName()
                        //        + "\n-- for label: " + text);
                        if (labed == null)
                            labed = new LabelStyleEditor(f, false, instance.getStyle());
                        labed.reset(style);
                        if (!labed.isVisible())
                            labed.setVisible(true);
                    }

                    public void modifyBlockLayout(BlockLayout layout) {
                        //echo("Will modify block layout: " + layout.getName());
                        if (layed == null)
                            layed = new BlockLayoutEditor(f, false, instance.getStyle());
                        layed.reset(layout);
                        if (!layed.isVisible())
                            layed.setVisible(true);
                    }
                    
                    public void modifyMiscSettings() {
                        echo("Will modify misc settings");
                    }

                    public void updateMessage(String message) {
                        messageLabel.setText(message);
                    }

                    public void clearMessage() {
                        messageLabel.setText(" ");
                    }

                    void echo(String s) {
                        //System.err.println(s);
                        JOptionPane.showMessageDialog(f, s);
                    }
                });
                instance = bc;
                f.getContentPane().add(bc.getUI(), BorderLayout.CENTER);
                f.getContentPane().add(messageLabel, BorderLayout.PAGE_END);
                f.pack();
                f.setVisible(true);
            }
        });
    }
}
