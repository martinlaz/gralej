package gralej.gui.blocks;

import java.awt.*;
import javax.swing.*;

public class TestLabel {
    public static JFrame f;
    public static Label label;
    
    public static void main(String[] args) throws Exception {
        new TestLabel();
    }
    
    public TestLabel() {
        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //JPanel contentPane = new JPanel(new BorderLayout());
        //contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //contentPane.setOpaque(true);
        BlockPanel panel = new BlockPanel();
        
        label = new Label(
              panel,
              "19",
              Font.decode("ARIAL-BOLD-18"),
              Color.BLACK,
              5,
              2,
              2);
        panel.setContent(label);
        label.setVisible(true);
        
        //contentPane.add(panel);
        f.setContentPane(panel);
        
        f.pack();
        f.setVisible(true);
    }
}
