package gralej.gui.blocks;

public class ListContent extends ContainerBlock {
    ListContent(IBlock parent) {
        super(parent);
        setLayout(new HorizontalLayout());
    }
    
    void init(Iterable<IBlock> listItems) {
        for (IBlock item : listItems)
            addChild(item);
    }
}
