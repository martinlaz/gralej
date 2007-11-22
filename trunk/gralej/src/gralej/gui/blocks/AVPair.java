package gralej.gui.blocks;

class AVPair extends ContentOwningBlock {
    Label   _a;
    IBlock  _v;
    
    AVPair(IBlock parent) {
        super(parent);
    }
    
    void init(Label a, IBlock v) {
        _a = a;
        _v = v;
        
        addChild(a);
        addChild(v);
    }
    
    public Label getAttribute() { return _a; }
    public IBlock getValue() { return _v; }
    public IBlock getContent() { return _v; }
    
    public void updateSize() {
        if (!isVisible())
            return;
        int w = _a.getWidth() + _v.getWidth();
        int h = Math.max(_a.getHeight(), _v.getHeight());
        setSize(w, h);
    }
    
    protected void layoutChildren() {
        int x = getX();
        int y = getY();
        int h = getHeight();
        
        _a.setPosition(
            x,
            y + h / 2 - _a.getHeight() / 2
            );
        
        _v.setPosition(
            x + ((AVPairList) getParentBlock()).getMaxAttributeWidth(),
            y + h / 2 - _v.getHeight() / 2
            );
    }
}
