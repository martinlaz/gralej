package gralej.parsers;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import gralej.gui.blocks.BlockCreatingVisitor;
import gralej.gui.blocks.BlockPanel;
import gralej.gui.blocks.IBlock;
import gralej.om.IVisitable;

class TraleMsgHandlerHelper {
    IParseResultReceiver _resultReceiver;
    
    void setResultReceiver(IParseResultReceiver resultReceiver) {
        _resultReceiver = resultReceiver;
    }
    
    static void createAndShowGUI(IVisitable vob) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        
        BlockPanel root = new BlockPanel();
        IBlock rootContent = new BlockCreatingVisitor().createBlock(root, vob);
        root.setContent(rootContent);
        rootContent.setVisible(true);
        
        contentPane.add(root);
        
        f.setContentPane(contentPane);
        f.pack();
        f.setVisible(true);
    }
    
    void adviceResult(final String title, final IVisitable vob) {
        if (_resultReceiver == null) {
            System.err.println("++ parsed ok, but no result receiver");
            return;
        }
        try {
            /*
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                        createAndShowGUI(vob);
                }
            });
            */
            BlockPanel root = new BlockPanel();
            IBlock rootContent = new BlockCreatingVisitor().createBlock(root, vob);
            root.setContent(rootContent);
            rootContent.setVisible(true);
            final JPanel panel = new JPanel(new BorderLayout());
            //panel.add(new JScrollPane(root));
            panel.setOpaque(true);
            panel.add(root);
            
            _resultReceiver.newParse(
                    new IParsedAVM() {
                        public String getName() {
                            return title;
                        }
                        public JPanel display() {
                            return panel;
                        }
                    }
                );
            return;
        }
        catch (UnsupportedOperationException e) {
            System.err.println(e);
            // trees are not implemented yet
            // -- show as xml for now
        }
        
        StringWriter s = new StringWriter();
        vob.accept(new OM2XMLVisitor(new PrintWriter(s, true)));
        String text = s.toString();
        
        final JPanel panel = new JPanel(new GridLayout());
        panel.add(new JScrollPane(new JTextArea(text)));
        
        _resultReceiver.newParse(
                new IParsedAVM() {
                    public String getName() {
                        return title;
                    }
                    public JPanel display() {
                        return panel;
                    }
                }
            );
    }
}
