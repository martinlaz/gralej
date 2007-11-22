package gralej.gui.blocks;

class Reentrancy extends ContentOwningBlock {
    Label _tag;
    IBlock _content;
    IContentCreator _contentCreator;
    
    Reentrancy(IBlock parent, int tag) {
        super(parent);
        _tag = getPanel().getLabelFactory().createTagLabel(
                Integer.toString(tag),
                this
                );
        addChild(_tag);
    }
    
    void init(IContentCreator contentCreator) {
        _contentCreator = contentCreator;
    }
    
    @Override
    protected void layoutChildren() {
        if (_content == null) {
            _tag.setPosition(getX(), getY());
            return;
        }
        
        int x = getX();
        int y = getY();
        int h = getHeight();
        
        _tag.setPosition(
                x,
                y + h / 2 - _tag.getHeight() / 2
                );
        _content.setPosition(
                x + _tag.getWidth(),
                y + h / 2 - _content.getHeight() / 2
                );
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
    
    @Override
    public void updateSize() {
        if (!isVisible())
            return;
        int w = _tag.getWidth();
        int h = _tag.getHeight();
        if (_content != null) {
            w += _content.getWidth();
            h = Math.max(h, _content.getHeight());
        }
        setSize(w, h);
    }
}
