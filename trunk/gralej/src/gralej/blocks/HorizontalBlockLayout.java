package gralej.blocks;

class HorizontalBlockLayout extends BlockLayout {
    
    @Override
    void layoutChildrenOfBlock(ContainerBlock block) {
        int x = block.getX() + getLeadingSpace();
        int y = block.getY();
        int h = block.getHeight();

        for (Block child : block.getChildren()) {
            if (!child.isVisible())
                continue;

            child.setPosition(x, y + h / 2 - child.getHeight() / 2);
            x += child.getWidth();
            x += getIntraSpace();
        }
    }

    @Override
    void updateBlockSize(ContainerBlock block) {
        int w = 0;
        int h = 0;
        int numChildren = 0;

        for (Block child : block.getChildren()) {
            if (!child.isVisible())
                continue;
            w += child.getWidth();
            h = Math.max(h, child.getHeight());
            ++numChildren;
        }

        if (numChildren > 0) {
            w += getLeadingSpace();
            w += (numChildren - 1) * getIntraSpace();
            w += getTrailingSpace();
        }

        block.setSize(w, h);
    }
}
