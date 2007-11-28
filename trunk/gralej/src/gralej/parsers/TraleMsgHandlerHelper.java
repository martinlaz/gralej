package gralej.parsers;

import gralej.gui.blocks.BlockCreator;
import gralej.gui.blocks.BlockPanel;
import gralej.om.IVisitable;
import java.awt.GridLayout;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class TraleMsgHandlerHelper {
    IParseResultReceiver _resultReceiver;
    
    void setResultReceiver(IParseResultReceiver resultReceiver) {
        _resultReceiver = resultReceiver;
    }
    /*
    private static void createAndShowGUI(IVisitable vob) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        
        BlockPanel root = new BlockPanel();
        IBlock rootContent = new BlockCreator().createBlock(vob);
        root.setContent(rootContent);
        //rootContent.setVisible(true);
        
        JScrollPane scrollPane = new JScrollPane(root);
        scrollPane.setPreferredSize(new Dimension(100,100));
        contentPane.add(scrollPane);
        
        f.setContentPane(contentPane);
        //f.setContentPane(new JScrollPane(contentPane));
        f.pack();
        f.setVisible(true);
    }
    */
    void adviceResult(final String title, final IVisitable vob) {
        if (_resultReceiver == null) {
            System.err.println("++ parsed ok, but no result receiver");
            return;
        }
        try {
            /*
             javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() { createAndShowGUI(vob); }
            });
             */
            final BlockPanel blockPanel = new BlockPanel(
                    new BlockCreator().createBlock(vob)
                    );
            _resultReceiver.newParse(
                    new IParsedAVM() {
                        public String getName() {
                            return title;
                        }
                        public JPanel display() {
                            return blockPanel;
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
