package gralej.gui.blocks;

class HorizontalLayout implements ILayout {
    static int SPACE_BEFORE  = Config.getInt("space.general.horizontal.before");
    static int SPACE_BETWEEN = Config.getInt("space.general.horizontal.between");
    static int SPACE_AFTER   = Config.getInt("space.general.horizontal.after");
    
    public void layoutBlockChildren(IBlock block) {
        int x = block.getX() + SPACE_BEFORE;
        int y = block.getY();
        int h = block.getHeight();
        
        for (IBlock child : block.getChildren()) {
            if (!child.isVisible())
                continue;
            
            child.setPosition(
                    x,
                    y + h / 2 - child.getHeight() / 2
                    );
            x += child.getWidth();
            x += SPACE_BETWEEN;
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
            w += SPACE_BEFORE;
            w += (numChildren - 1) * SPACE_BETWEEN;
            w += SPACE_AFTER;
        }
        block.setSize(w, h);
    }
}
