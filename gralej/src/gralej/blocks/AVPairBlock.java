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

public class AVPairBlock extends ContentOwningBlock {
    boolean _isModelHidden;

    AVPairBlock(BlockPanel panel, Label a, Block v, boolean isModelHidden) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getAVPairLayout());
        
        addChild(a);
        lastAddChild(v);
        setContent(v);
        
        _isModelHidden = isModelHidden;
    }
    
    @Override
    public boolean isModelHidden() {
        return _isModelHidden;
    }
    
    void setModelHidden(boolean b) {
        if (_isModelHidden == b)
            return;
        b = isVisible();
        _isModelHidden = !_isModelHidden;
        if (b != isVisible())
            updateParent();
    }
    
    public ContentLabel getAttribute() {
        return (ContentLabel) _children.get(0);
    }

    public Block getValue() {
        return getContent();
    }
    
    @Override
    protected Block getEastNeighbour(Block child) {
        if (child == getAttribute()) {
            Block b = getValue();
            if (b.isVisible())
                return b.getPrincipalBlock();
        }
        return getParent().getEastNeighbour(this);
    }
    
    @Override
    protected Block getWestNeighbour(Block child) {
        if (child == getValue()) {
            return getAttribute();
        }
        return getParent().getWestNeighbour(this);
    }
    
    @Override
    protected Block getNorthNeighbour(Block child) {
        if (child == getValue()) {
            return getAttribute();
        }
        return super.getNorthNeighbour(this);
    }
    
    @Override
    protected Block getSouthNeighbour(Block child) {
        if (child == getValue()) {
            return getAttribute();
        }
        return super.getSouthNeighbour(this);
    }
}
