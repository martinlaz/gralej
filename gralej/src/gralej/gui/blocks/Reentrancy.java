package gralej.gui.blocks;

class Reentrancy extends ContentOwningBlock {
    IContentCreator _contentCreator;
    
    Reentrancy(IBlock parent, int tag) {
        super(parent);
        setLayout(new HorizontalLayout());
        
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
            //addChild(_content);
            _children.add(_content);
        }
        return _content;
    }
}
