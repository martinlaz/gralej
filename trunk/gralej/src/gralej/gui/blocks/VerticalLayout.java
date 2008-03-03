package gralej.gui.blocks;

class VerticalLayout extends AbstractLayout {
    
    VerticalLayout(String name) {
        super(name);
    }
    
    // default child alignment: left
    protected int computeChildX(IBlock parent, IBlock child) {
        return parent.getX();
    }

    public void layoutBlockChildren(IBlock block) {
        int y = block.getY() + getLeadingSpace();

        for (IBlock child : block.getChildren()) {
            child.setPosition(computeChildX(block, child), y);
            y += child.getHeight();
            if (child.isVisible())
                y += getIntraSpace();
        }
    }

    public void updateBlockSize(IBlock block) {
        if (!block.isVisible())
            return;

        int w = 0;
        int h = getLeadingSpace();
        int numChildren = 0;

        for (IBlock child : block.getChildren()) {
            if (!child.isVisible())
                continue;
            w = Math.max(w, child.getWidth());
            h += child.getHeight();
            numChildren++;
        }

        if (numChildren > 1)
            h += (numChildren - 1) * getIntraSpace();
        h += getTrailingSpace();

        block.setSize(w, h);
    }
}
