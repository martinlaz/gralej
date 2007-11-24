package gralej.gui.blocks;

class List extends ContentOwningBlock {
    List(IBlock parent) {
        super(parent);
        setLayout(LayoutFactory.getListLayout());
    }
    
    void init(ListContent content) {
        LabelFactory labfac = getPanel().getLabelFactory();
        
        addChild(labfac.createListLBracketLabel(this));
        
        if (!content.isEmpty()) {
            addChild(content);
            setContent(content);
        }
        
        addChild(labfac.createListRBracketLabel(this));
    }
}
