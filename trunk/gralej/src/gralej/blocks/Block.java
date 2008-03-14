/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.blocks;

import java.util.Collections;
import java.awt.Graphics2D;
import java.util.List;

/**
 *
 * @author Martin
 */
public abstract class Block {
    private boolean _isVisible = true;
    protected int _x = -1, _y = -1;
    
    private int _width = -1, _height = -1;
    private BlockPanel _panel;
    private ContainerBlock _parent;
    
    protected BlockPanel getPanel() {
        assert _panel != null;
        return _panel;
    }
    
    protected BlockPanelStyle getPanelStyle() {
        return getPanel().getStyle();
    }
    
    protected void setPanel(BlockPanel panel) {
        assert _panel == null;
        _panel = panel;
    }
    
    // visibility
    //
    public void setVisible(boolean newValue) {
        if (_isVisible == newValue)
            return;
        _isVisible = newValue;
        updateParent();
    }
    
    public boolean isVisible() {
        if (!_isVisible)
            return false;
        if (isModelHidden())
            return getPanel().isDisplayingModelHiddenFeatures();
        return true;
    }
    
    // TODO: override in AVPairBlock
    public boolean isModelHidden() {
        return false;
    }
    
    public boolean isHiddenByAncestor() {
        for (ContainerBlock b = getParent(); b != null; b = b.getParent())
            if (!b.isVisible())
                return true;
        return false;
    }
    
    protected void updateParent() {
        if (_parent != null)
            _parent.updateSelf();
    }
    
    protected abstract void updateSelf();
    
    public void update() {
        updateSelf();
    }
    
    // size
    //
    public int getWidth() {
        return _width;
    }
    
    public int getHeight() {
        return _height;
    }
    
    void setSize(int w, int h) {
        _width = w; _height = h;
        if (!isVisible())
            return;
        updateParent();
    }
    
    // location
    //
    public int getX() {
        return _x;
    }
    
    public int getY() {
        return _y;
    }
    
    public void setPosition(int x, int y) {
        _x = x; _y = y;
    }
    
    // hierarchy
    // 
    public List<Block> getChildren() {
        return Collections.emptyList();
    }

    public ContainerBlock getParent() {
        return _parent;
    }
    
    void setParent(ContainerBlock parent) {
        assert _parent == null;
        _parent = parent;
    }

    public boolean isLeaf() {
        return true;
    }
    
    public String getPath() {
        if (_parent == null)
            return "";
        return _parent.getPath() + "/" + _parent.indexOf(this);
    }

    public void paint(Graphics2D g) {
        for (Block child : getChildren())
            if (child.isVisible())
                child.paint(g);
    }
}
