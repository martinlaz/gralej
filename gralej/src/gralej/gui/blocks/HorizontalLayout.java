package gralej.gui.blocks;

class HorizontalLayout extends AbstractLayout {
    
    HorizontalLayout() {}
    
    HorizontalLayout(int lead, int intra, int trail) {
        super(lead, intra, trail);
    }

    public void layoutBlockChildren(IBlock block) {
        int x = block.getX() + getLeadingSpace();
        int y = block.getY();
        int h = block.getHeight();

        for (IBlock child : block.getChildren()) {
            if (!child.isVisible())
                continue;

            child.setPosition(x, y + h / 2 - child.getHeight() / 2);
            x += child.getWidth();
            x += getIntraSpace();
        }
    }

    public void updateBlockSize(IBlock block) {
        if (!block.isVisible())
            return;

        int w = 0;
        int h = 0;
        int numChildren = 0;

        for (IBlock child : block.getChildren()) {
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
