package gralej.gui.blocks;

class ListBlock extends ContentOwningBlock {
    ListBlock(ListContentBlock content) {
        if (!content.isEmpty())
            _children.add(content);
    }
    
    @Override
    public void init() {
        setLayout(getPanel().getLayoutFactory().getListLayout());
        
        LabelFactory labfac = getPanel().getLabelFactory();
        
        if (isEmpty()) {
            addChild(labfac.createListLBracketLabel());
            addChild(labfac.createListRBracketLabel());
        }
        else {
            ListContentBlock content = (ListContentBlock) _children.get(0);
            _children.clear();
            
            addChild(labfac.createListLBracketLabel());
            addChild(content);
            setContent(content);
            addChild(labfac.createListRBracketLabel());
        }

        super.init();
    }
}
