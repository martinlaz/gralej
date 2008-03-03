package gralej.blocks;

public class ListBlock extends ContentOwningBlock {
    ListBlock(BlockPanel panel, ListContentBlock content) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getListLayout());
        
        LabelFactory labfac = getPanelStyle().getLabelFactory();
        
        addChild(labfac.createListLBracketLabel(panel));
        if (!content.isEmpty()) {
            addChild(content);
            setContent(content);
        }
        addChild(labfac.createListRBracketLabel(panel));
    }
}
