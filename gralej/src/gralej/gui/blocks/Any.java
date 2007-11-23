package gralej.gui.blocks;

import java.awt.Graphics2D;

class Any extends Block {
    Label _label;
    
    Any(IBlock parent, String s) {
        super(parent);
        _label = getPanel().getLabelFactory().createAnyLabel(s, this);
        _label.setVisible(true);
    }

    public void paint(Graphics2D g) {
        _label.paint(g);
    }
    
    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        _label.setPosition(x, y);
    }
    
    public void updateSize() {
        if (!isVisible())
            return;
        setSize(_label.getWidth(), _label.getHeight());
    }
}
