package gralej.gui.blocks;

public class ListContent extends ContainerBlock {
    ListContent(IBlock parent) {
        super(parent);
        setLayout(LayoutFactory.getListContentLayout());
    }
    
    void init(Iterable<IBlock> listItems) {
        boolean firstAdded = false;
        LabelFactory labfac = getPanel().getLabelFactory();
        for (IBlock item : listItems) {
            if (firstAdded)
                addChild(labfac.createListSeparatorLabel(this));
            else
                firstAdded = true;
            
            addChild(item);
        }
    }
}
