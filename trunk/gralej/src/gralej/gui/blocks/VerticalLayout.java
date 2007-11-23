package gralej.gui.blocks;

class VerticalLayout implements ILayout {
    static int SPACE_BEFORE  = Config.getInt("space.general.vertical.before");
    static int SPACE_BETWEEN = Config.getInt("space.general.vertical.between");
    static int SPACE_AFTER   = Config.getInt("space.general.vertical.after");
    
    @Override
    public void layoutBlockChildren(IBlock block) {
        final int x = block.getX();
        int y = block.getY() + SPACE_BEFORE;
        
        for (IBlock child : block.getChildren()) {
            child.setPosition(x, y);
            y += child.getHeight();
            if (child.isVisible())
                y += SPACE_BETWEEN;
        }
    }
    
    @Override
    public void updateBlockSize(IBlock block) {
        if (!block.isVisible())
            return;
        
        int w = 0;
        int h = SPACE_BEFORE;
        int numChildren = 0;
        
        for (IBlock child : block.getChildren()) {
            if (!child.isVisible())
                continue;
            w = Math.max(w, child.getWidth());
            h += child.getHeight();
            numChildren++;
        }
        
        if (numChildren > 1)
            h += (numChildren - 1) * SPACE_BETWEEN;
        h += SPACE_AFTER;
        
        block.setSize(w, h);
    }
}
