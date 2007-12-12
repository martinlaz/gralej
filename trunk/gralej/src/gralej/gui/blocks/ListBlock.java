package gralej.gui.blocks;

class ListBlock extends ContentOwningBlock {
    ListBlock(ListContentBlock content) {
        setLayout(LayoutFactory.getListLayout());

        LabelFactory labfac = LabelFactory.getInstance();

        addChild(labfac.createListLBracketLabel());

        if (!content.isEmpty()) {
            addChild(content);
            setContent(content);
        }

        addChild(labfac.createListRBracketLabel());
    }
}
