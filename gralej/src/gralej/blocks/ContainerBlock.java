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

import java.util.Collections;
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
    
    protected void lastAddChild(Block block) {
        addChild(block);
        sealChildren();
    }
    
    protected void sealChildren() {
        _children  = Collections.unmodifiableList(_children);
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
    
    @Override Block getPrincipalBlock() {
        for (Block b : _children) {
            if (!b.isVisible())
                continue;
            if (b instanceof ContainerBlock) {
                b = ((ContainerBlock) b).getPrincipalBlock();
                if (b != null)
                    return b;
            }
            else if (b instanceof ContentLabel && b.isVisible())
                return b;
        }
        return null;
    }
    
    protected Block getWestNeighbour(Block child) {
        if (getParent() != null)
            return getParent().getWestNeighbour(this);
        return null;
    }
    
    protected Block getNorthNeighbour(Block child) {
        if (getParent() != null)
            return getParent().getNorthNeighbour(this);
        return null;
    }
    
    protected Block getEastNeighbour(Block child) {
        if (getParent() != null)
            return getParent().getEastNeighbour(this);
        return null;
    }
    
    protected Block getSouthNeighbour(Block child) {
        if (getParent() != null)
            return getParent().getSouthNeighbour(this);
        return null;
    }
}
