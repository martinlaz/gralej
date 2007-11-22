package gralej.gui.blocks;

class AVPairList extends ContainerBlock {
    int _maxAttributeWidth;
    
    AVPairList(IBlock parent) {
        super(parent);
    }
    
    void addChild(AVPair child) {
        super.addChild(child);
    }
    
    public void updateSize() {
        if (!isVisible())
            return;
       
        int h = 0;
        // max attribute width
        int maw = 0;
        // max value width
        int mvw = 0;
        
        for (IBlock child : getChildren()) {
            AVPair av = (AVPair) child;
            maw = Math.max(maw, av.getAttribute().getWidth()); 
            mvw = Math.max(mvw, av.getValue().getWidth());
            h += av.getHeight();
        }
        
        _maxAttributeWidth = maw;
        int w = maw + mvw;
        setSize(w, h);
    }
    
    public int getMaxAttributeWidth() {
        return _maxAttributeWidth;
    }
    
    protected void layoutChildren() {
        int x = getX();
        int y = getY();
        
        for (IBlock child : getChildren()) {
            child.setPosition(x, y);
            y += child.getHeight();
        }
    }
}
