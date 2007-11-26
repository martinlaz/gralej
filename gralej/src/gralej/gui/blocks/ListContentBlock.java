package gralej.gui.blocks;

public class ListContentBlock extends ContainerBlock {
    ListContentBlock(Iterable<Block> listItems) {
        setLayout(LayoutFactory.getListContentLayout());
    
        boolean firstAdded = false;
        LabelFactory labfac = LabelFactory.getInstance();
        for (Block item : listItems) {
            if (firstAdded)
                addChild(labfac.createListSeparatorLabel());
            else
                firstAdded = true;
            
            addChild(item);
        }
    }
}
