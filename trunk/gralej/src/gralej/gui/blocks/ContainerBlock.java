package gralej.gui.blocks;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Graphics2D;

abstract class ContainerBlock extends Block {
    protected List<IBlock> _children = new ArrayList<IBlock>();
    private ILayout _layout;
    
    ContainerBlock(IBlock parent) {
        super(parent);
    }
    
    protected void addChild(IBlock child) {
        _children.add(child);
        child.setVisible(true);
    }
    
    public boolean isEmpty() {
        return _children.isEmpty();
    }
    
    public boolean isLeaf() {
        return false;
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
    
    protected void setLayout(ILayout layout) {
        assert _layout == null;
        _layout = layout;
    }
    
    @Override
    protected void layoutChildren() {
        if (_layout != null)
            _layout.layoutBlockChildren(this);
    }
    
    public void paint(Graphics2D g) {
        for (IBlock child : _children)
            if (child.isVisible())
                child.paint(g);
    }
    
    public void updateSize() {
        if (_layout != null)
            _layout.updateBlockSize(this);
    }
}
