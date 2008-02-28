package gralej.gui.blocks;

class AVPairBlock extends ContentOwningBlock {

    AVPairBlock(Label a, Block v) {
        addChild(a);
        addChild(v);
        setContent(v);
    }
    
    @Override
    public void init() {
        setLayout(getPanel().getLayoutFactory().getAVPairLayout());
        super.init();
    }

    public IBlock getAttribute() {
        return _children.get(0);
    }

    public IBlock getValue() {
        return _children.get(1);
    }

    @Override
    public IBlock getContent() {
        return getValue();
    }
}
