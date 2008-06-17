package gralej.blocks;

public class ListContentBlock extends ContainerBlock {
    ListContentBlock(BlockPanel panel, Iterable<Block> listItems, Block tail) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getListContentLayout());
        
        LabelFactory labfac = getPanelStyle().getLabelFactory();
        
        boolean firstAdded = false;
        for (Block item : listItems) {
            if (firstAdded)
                addChild(labfac.createListSeparatorLabel(panel));
            else
                firstAdded = true;

            addChild(item);
        }
        if (tail != null) {
            addChild(labfac.createListTailSeparatorLabel(panel));
            addChild(tail);
        }
        sealChildren();
    }
}
