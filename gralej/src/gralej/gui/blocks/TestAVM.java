package gralej.gui.blocks;

import java.awt.*;
import javax.swing.*;

public class TestAVM {
    public static JFrame f;
    public static AVM avm;
    
    public static void main(String[] args) throws Exception {
        new TestAVM();
    }
    
    public TestAVM() {
        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel contentPane = new JPanel(new BorderLayout());
        
        BlockPanel panel = new BlockPanel();
        avm = makeTestAVM(panel);
        panel.setContent(avm);
        avm.setVisible(true);
        
        contentPane.add(panel);
        f.setContentPane(contentPane);
        
        f.pack();
        f.setVisible(true);
    }
    
    LabelFactory _lfac = LabelFactory.getInstance();
    
    AVM makeTestAVM(IBlock panel) {
        return makeSpecies("book", panel);
    }
    
    
    
    AVM makeSpecies(String type, IBlock parent) {
        AVM avm = new AVM(parent);
        avm.init(_lfac.createSortLabel(type, avm), new AVPairList(avm));
        return avm;
    }
}
