package gralej.gui.blocks;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.awt.Graphics2D;

abstract class ContainerBlock extends Block {
    protected List<IBlock> _children = new LinkedList<IBlock>();
    
    ContainerBlock(IBlock parent) {
        super(parent);
    }
    
    protected void addChild(IBlock child) {
        _children.add(child);
        child.setVisible(true);
    }
    
    @Override
    public Iterable<IBlock> getChildren() {
        return Collections.unmodifiableList(_children);
    }
    
    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        layoutChildren();
    }
    
    public void paint(Graphics2D g) {
        for (IBlock child : _children)
            if (child.isVisible())
                child.paint(g);
    }
}
