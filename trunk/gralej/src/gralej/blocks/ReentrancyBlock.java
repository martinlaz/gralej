package gralej.blocks;

class ReentrancyBlock extends ContentOwningBlock {
    int _tag;
    ContentCreator _contentCreator;

    ReentrancyBlock(BlockPanel panel, int tag, ContentCreator contentCreator) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getReentrancyLayout());
        
        Label tagLabel = getPanelStyle().getLabelFactory().createTagLabel(
                Integer.toString(tag), panel);
        addChild(tagLabel);
        
        _tag = tag;
        _contentCreator = contentCreator;
        
        if (getPanelStyle().isAutoExpandingTags())
            if (getPanel().getExpandedTags().add(_tag))
                getContent().setVisible(true);
    }
    
    public Label getTagLabel() {
        return (Label) _children.get(0);
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
}
