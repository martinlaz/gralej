package gralej.gui.blocks;

class AVPair extends ContentOwningBlock {
    
    AVPair(IBlock parent) {
        super(parent);
        setLayout(LayoutFactory.getAVPairLayout());
    }
    
    void init(Label a, IBlock v) {
        addChild(a);
        addChild(v);
        setContent(v);
    }
    
    public IBlock getAttribute() { return _children.get(0); }
    public IBlock getValue()     { return _children.get(1); }
    public IBlock getContent()   { return getValue(); }
}
