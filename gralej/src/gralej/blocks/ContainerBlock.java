/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.blocks;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Martin
 */
public abstract class ContainerBlock extends Block {
    protected boolean _isUpdatingChildren;
    protected List<Block> _children = new LinkedList<Block>();
    private BlockLayout _layout;
    
    protected void setLayout(BlockLayout layout) {
        assert _layout == null;
        _layout = layout;
    }
    
    public BlockLayout getLayout() {
        assert _layout != null;
        return _layout;
    }
    
    @Override
    public List<Block> getChildren() {
        return _children;
    }
    
    @Override
    public boolean isLeaf() {
        return false;
    }
    
    public boolean isEmpty() {
        return _children.isEmpty();
    }
    
    int indexOf(Block child) {
        return _children.indexOf(child);
    }
    
    protected void addChild(Block block) {
        block.setParent(this);
        _children.add(block);
    }
    
    protected void updateLayoutManager() {
        if (_layout != null && _layout.getName() != null)
            _layout = getPanelStyle().getLayoutFactory().getLayout(_layout.getName());
    }
    
    @Override
    public void update() {
        updateLayoutManager();
        
        _isUpdatingChildren = true;
        try {
            for (Block child : getChildren())
                child.update();
        }
        finally {
            _isUpdatingChildren = false;
        }
        updateSelf();
    }
    
    @Override
    public void setPosition(int x, int y) {
        assert _layout != null;
        
        //if (x == getX() && y == getY())
            //return;
        
        super.setPosition(x, y);
        _layout.layoutChildrenOfBlock(this);
    }
    
    @Override
    public void updateSelf() {
        assert _layout != null;
        
        if (_isUpdatingChildren)
            return;
        
        _layout.updateBlockSize(this);
    }
    
    public Block getDescendant(String path) {
        String[] ss = path.trim().split("\\s*/\\s*");
        if (ss == null || ss.length == 0 || (ss.length == 1 && ss[0].length() == 0))
            return this;
        int[] ipath = new int[ss.length];
        for (int i = 0; i < ss.length; ++i)
            ipath[i] = Integer.parseInt(ss[i]);
        return getDescendant(ipath, 0);
    }
    
    private Block getDescendant(int[] path, int pathPos) {
        if (pathPos == path.length)
            return this;
        int i = path[pathPos++];
        if (i < 0)
            i = _children.size() - i;
        if (i >= _children.size())
            return null;
        Block child = _children.get(i);
        if (child instanceof ContainerBlock)
            return ((ContainerBlock) child).getDescendant(path, pathPos);
        if (pathPos == path.length)
            return child;
        return null;
    }
}
