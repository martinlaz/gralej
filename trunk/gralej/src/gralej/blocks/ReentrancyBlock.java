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

public class ReentrancyBlock extends ContentOwningBlock {
    int _tag;
    ContentCreator _contentCreator;

    ReentrancyBlock(BlockPanel panel, ContentLabel tagLabel, int tag, ContentCreator contentCreator) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getReentrancyLayout());
        
        addChild(tagLabel);
        
        _tag = tag;
        _contentCreator = contentCreator;
    }
    
    @Override
    public void update() {
        if (_content != null) {
            super.update();
            return;
        }
        if (!isHiddenByAncestor()) {
            if (getPanel().getExpandedTags(getOutermostAVMBlock()).add(_tag)) {
                addChild(_content = _contentCreator.createContent());
            }
        }
        super.update();
        if (_content != null && !getPanel().isAutoExpandingTags())
            _content.setVisible(false);
    }
    
    public ContentLabel getTagLabel() {
        return (ContentLabel) _children.get(0);
    }
    
    public int getTag() {
        return _tag;
    }

    @Override
    public Block getContent() {
        if (_content == null) {
            _content = _contentCreator.createContent();
            // don't make it visible,
            // the content label will do it
            _content.update();
            _content.setVisible(false);
            lastAddChild(_content);
        }
        return _content;
    }
    
    private ContainerBlock getOutermostAVMBlock() {
        ContainerBlock b = getParent();
        ContainerBlock p = b.getParent();
        while (p != null && !(p instanceof NodeBlock || p instanceof RootBlock)) {
            b = p;
            p = p.getParent();
        }
        return b;
    }
    
    @Override
    protected Block getEastNeighbour(Block child) {
        Block b = getContent();
        if (child == b || b == null || !b.isVisible())
            return getParent().getEastNeighbour(this);
        b = b.getPrincipalBlock();
        if (b == null || !b.isVisible())
            return getParent().getEastNeighbour(this);
        return b;
    }
    
    @Override
    protected Block getWestNeighbour(Block child) {
        if (child != getTagLabel())
            return getTagLabel();
        return getParent().getWestNeighbour(this);
    }
}
