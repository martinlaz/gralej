package gralej.blocks;

class VerticalBlockLayout extends BlockLayout {
    
    // default child alignment: left
    protected int computeChildX(ContainerBlock parent, Block child) {
        return parent.getX();
    }
    
    @Override
    void layoutChildrenOfBlock(ContainerBlock block) {
        int y = block.getY() + getLeadingSpace();

        for (Block child : block.getChildren()) {
            if (!child.isVisible())
                continue;
            child.setPosition(computeChildX(block, child), y);
            y += child.getHeight();
            y += getIntraSpace();
        }
    }
    
    @Override
    void updateBlockSize(ContainerBlock block) {
        int w = 0;
        int h = getLeadingSpace();
        int numChildren = 0;

        for (Block child : block.getChildren()) {
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
