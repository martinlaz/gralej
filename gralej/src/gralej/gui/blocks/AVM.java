package gralej.gui.blocks;

import java.awt.Color;
import java.awt.Graphics2D;

class AVM extends ContentOwningBlock {
    Label _sort;
    AVPairList _avPairs;
    
    AVM(IBlock parent) {
        super(parent);
    }
    
    void init(Label sort, AVPairList avPairs) {
        assert _sort == null && _avPairs == null;
        
        _sort = sort;
        _avPairs = avPairs;
        
        addChild(sort);
        addChild(avPairs);
    }
    
    public IBlock getContent() { return _avPairs; }
    
    public void updateSize() {
        if (!isVisible())
            return;
        
        int w = Math.max(_sort.getWidth(), _avPairs.getWidth());
        int h = _sort.getHeight() + _avPairs.getHeight();
        
        setSize(w, h);
    }
    
    protected void layoutChildren() {
        _sort.setPosition(getX(), getY());
        _avPairs.setPosition(getX(), getY() + _sort.getHeight());
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        // paint the brackets
        g.setColor(Color.BLACK);
        int x = getX(), y = getY(), w = getWidth(), h = getHeight();
        g.drawLine(x, y, x, y + h);
        g.drawLine(x + w, y, x + w, y + h);
    }
}
