package gralej.gui.blocks;

class Reentrancy extends ContentOwningBlock {
    IContentCreator _contentCreator;
    
    Reentrancy(IBlock parent, int tag) {
        super(parent);
        setLayout(LayoutFactory.getReentrancyLayout());
        
        Label tagLabel = getPanel().getLabelFactory().createTagLabel(
                Integer.toString(tag),
                this
                );
        addChild(tagLabel);
    }
    
    void init(IContentCreator contentCreator) {
        _contentCreator = contentCreator;
    }
    
    @Override
    public IBlock getContent() {
        if (_content == null) {
            _content = _contentCreator.createContent(this);
            // don't make it visible,
            // the content label will do so
            addChild(_content, false);
        }
        return _content;
    }
}
