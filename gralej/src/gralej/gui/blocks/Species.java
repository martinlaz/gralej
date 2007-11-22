package gralej.gui.blocks;

import java.awt.Graphics2D;

class Species extends Block {
    Label _label;
    
    Species(IBlock parent, String sort) {
        super(parent);
        _label = getPanel().getLabelFactory().createSpeciesLabel(sort, this);
        _label.setVisible(true);
    }
    @Override
    public void paint(Graphics2D g) {
        _label.paint(g);
    }
    
    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        _label.setPosition(x, y);
    }
    
    @Override
    public void updateSize() {
        if (!isVisible())
            return;
        setSize(_label.getWidth(), _label.getHeight());
    }
}
