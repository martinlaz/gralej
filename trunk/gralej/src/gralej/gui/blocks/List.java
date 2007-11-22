package gralej.gui.blocks;

class List extends ContentOwningBlock {
    ListContent _content;
    
    List(IBlock parent) {
        super(parent);
    }
    
    void init(ListContent content) {
        LabelFactory labfac = getPanel().getLabelFactory();
        
        addChild(labfac.createListLBracketLabel(this));
        addChild(content);
        addChild(labfac.createListRBracketLabel(this));
        
        _content = content;
    }
    
    @Override
    protected void layoutChildren() {
        int x = getX();
        int y = getY();
        int h = getHeight();
        
        for (IBlock child : getChildren()) {
            child.setPosition(
                    x,
                    y + h / 2 - child.getHeight() / 2
                    );
            x += child.getWidth();
        } 
    }
    
    @Override
    public IBlock getContent() {
        return _content;
    }
    
    @Override
    public void updateSize() {
        if (!isVisible())
            return;
        int w = 0;
        int h = 0;
        for (IBlock child : getChildren()) {
            w += child.getWidth();
            h = Math.max(h, child.getHeight());
        }
        setSize(w, h);
    }
}
