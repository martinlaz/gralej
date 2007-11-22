package gralej.gui.blocks;

public class ListContent extends ContainerBlock {
    ListContent(IBlock parent) {
        super(parent);
    }
    
    void init(Iterable<IBlock> listItems) {
        for (IBlock item : listItems)
            addChild(item);
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
