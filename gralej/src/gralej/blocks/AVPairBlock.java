package gralej.blocks;

public class AVPairBlock extends ContentOwningBlock {
    boolean _isModelHidden;

    AVPairBlock(BlockPanel panel, Label a, Block v, boolean isModelHidden) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getAVPairLayout());
        
        addChild(a);
        addChild(v);
        setContent(v);
        
        _isModelHidden = isModelHidden;
    }
    
    @Override
    public boolean isModelHidden() {
        return _isModelHidden;
    }
    
    public ContentLabel getAttribute() {
        return (ContentLabel) _children.get(0);
    }

    public Block getValue() {
        return getContent();
    }
}
