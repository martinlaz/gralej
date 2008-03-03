/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.testers;

import gralej.blocks.BlockConfigurator;
import gralej.blocks.BlockConfigurator.Handler;
import gralej.blocks.BlockLayout;
import gralej.blocks.LabelStyle;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Martin
 */
public class BlockConfiguratorTester {
    public static BlockConfigurator instance;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
           public void run() {
               final JLabel messageLabel = new JLabel();
               messageLabel.setText(" ");
               final JFrame f = new JFrame();
               f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
               final BlockConfigurator bc = new BlockConfigurator(new Handler() {
                    public boolean modifyLabelStyle(LabelStyle style, String text) {
                        echo("Will modify label style: " + style.getName()
                                + "\n-- for label: " + text);
                        return true;
                    }
                    public boolean modifyBlockLayout(BlockLayout layout) {
                        echo("Will modify block layout: " + layout.getName());
                        return true;
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
