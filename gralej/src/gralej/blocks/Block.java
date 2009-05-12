/*
 *  $Id$
 *
 *  Author:
 *     Martin Lazarov [mlazarov at sfs.uni-tuebingen.de]
 *     
 *  This file is part of the Gralej system
 *     http://code.google.com/p/gralej/
 *
 *  Gralej is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gralej is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package gralej.blocks;

import gralej.om.IEntity;
import java.awt.Color;
import java.util.Collections;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
    private IEntity _model;
    
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
    
    public void setModel(IEntity model) {
        assert _model == null;
        _model = model;
    }
    
    public IEntity getModel() {
        return _model;
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
    
    // the neighbourhood -- for keyboard navigation
    //
    Block getWestNeighbour() {
        if (_parent != null)
            return _parent.getWestNeighbour(this);
        return null;
    }
    Block getNorthNeighbour() {
        if (_parent != null)
            return _parent.getNorthNeighbour(this);
        return null;
    }
    Block getEastNeighbour() {
        if (_parent != null)
            return _parent.getEastNeighbour(this);
        return null;
    }
    Block getSouthNeighbour() {
        if (_parent != null)
            return _parent.getSouthNeighbour(this);
        return null;
    }
    
    Block getPrincipalBlock() { return null; }
    
    // paths
    //
    public String getPath() {
        if (_parent == null)
            return "";
        return _parent.getPath() + "/" + _parent.indexOf(this);
    }
    
    public Block getRoot() {
        if (_parent == null)
            return this;
        return _parent.getRoot();
    }
    
    public boolean isDifferent() {
        if (_model != null)
            return _model.isDifferent();
        return false;
    }

    public void paint(Graphics2D g) {
        if (isDifferent()) {
            Color oldColor = g.getColor();
            g.setColor(getPanelStyle().getDifferentBackgroundColor());
            Rectangle frame = new Rectangle(_x, _y, _width, _height);
            g.fill(frame);
            g.setColor(oldColor);
        }
        for (Block child : getChildren())
            if (child.isVisible())
                child.paint(g);
    }
}
