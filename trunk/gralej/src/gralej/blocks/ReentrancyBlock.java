package gralej.blocks;

public class ReentrancyBlock extends ContentOwningBlock {
    int _tag;
    ContentCreator _contentCreator;

    ReentrancyBlock(BlockPanel panel, ContentLabel tagLabel, int tag, ContentCreator contentCreator) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getReentrancyLayout());
        
        addChild(tagLabel);
        
        _tag = tag;
        _contentCreator = contentCreator;
    }
    
    @Override
    public void update() {
        if (!isHiddenByAncestor()) {
            if (getPanel().getExpandedTags(getOutermostAVMBlock()).add(_tag)) {
                addChild(_content = _contentCreator.createContent());
                if (!getPanel().isAutoExpandingTags())
                    _content.setVisible(false);
            }
        }
        super.update();
    }
    
    public ContentLabel getTagLabel() {
        return (ContentLabel) _children.get(0);
    }
    
    public int getTag() {
        return _tag;
    }

    @Override
    public Block getContent() {
        if (_content == null) {
            _content = _contentCreator.createContent();
            // don't make it visible,
            // the content label will do it
            _content.update();
            _content.setVisible(false);
            addChild(_content);
        }
        return _content;
    }
    
    private ContainerBlock getOutermostAVMBlock() {
        ContainerBlock b = getParent();
        ContainerBlock p = b.getParent();
        while (p != null && !(p instanceof NodeBlock || p instanceof RootBlock)) {
            b = p;
            p = p.getParent();
        }
        return b;
    }
}
