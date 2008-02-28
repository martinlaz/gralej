package gralej.gui.blocks;

import java.util.ArrayList;
import java.util.List;

class ListContentBlock extends ContainerBlock {
    ListContentBlock(Iterable<Block> listItems) {
        for (Block b : listItems)
            _children.add(b);
    }
    
    @Override
    public void init() {
        setLayout(getPanel().getLayoutFactory().getListContentLayout());

        List<IBlock> items = new ArrayList<IBlock>(_children);
        LabelFactory labfac = getPanel().getLabelFactory();
        
        boolean firstAdded = false;
        _children.clear();
        for (IBlock item : items) {
            if (firstAdded)
                addChild(labfac.createListSeparatorLabel());
            else
                firstAdded = true;

            addChild((Block) item);
        }
        
        super.init();
    }
}
