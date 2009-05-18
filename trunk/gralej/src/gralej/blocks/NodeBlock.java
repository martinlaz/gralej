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

import java.util.List;
import java.util.Stack;

public class NodeBlock extends ContentOwningBlock {
    private NodeBlock _parentNode;
    private List<NodeBlock> _childNodes;

    NodeBlock(BlockPanel panel, Label label, Block content, List<NodeBlock> childNodes) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getNodeLayout());
        
        addChild(label);
        lastAddChild(content);

        setContent(content);

        _childNodes = childNodes;
    }
    
    public ContentLabel getLabel() {
        return (ContentLabel) _children.get(0);
    }
    
    public Iterable<NodeBlock> getChildNodes() {
        return _childNodes;
    }

    public boolean isLeafNode() {
        return _childNodes.isEmpty();
    }
    
    void setParentNode(NodeBlock parentNode) {
        assert _parentNode == null;
        _parentNode = parentNode;
    }
    
    @Override
    protected Block getNorthNeighbour(Block child) {
        if (child == getContent())
            return getLabel();
        if (_parentNode != null) {
            Block b = _parentNode.getContent();
            Stack<Block> s = new Stack<Block>();
            s.push(_parentNode);
            if (b instanceof AVMBlock && b.isVisible()) {
                for (Block bb : b.getChildren().get(1).getChildren())
                    s.push(bb);
            }
            while (!s.isEmpty()) {
                b = s.pop();
                if (b.isVisible())
                    return b.getPrincipalBlock();
            }
        }
        return null;
    }
    
    @Override
    protected Block getSouthNeighbour(Block child) {
        if (child == getLabel()) {
            Block b = getContent();
            if (b != null && b.isVisible() && b instanceof ContainerBlock)
                return ((ContainerBlock)b).getPrincipalBlock();
        }
        if (!isLeafNode())
            return _childNodes.get(0).getPrincipalBlock();
        return null;
    }
    
    @Override
    protected Block getEastNeighbour(Block child) {
        if (child != getLabel())
            return getLabel();
        if (_parentNode == null)
            return null;
        int i = _parentNode._childNodes.indexOf(this);
        if (i == _parentNode._childNodes.size() - 1)
            return null;
        return _parentNode._childNodes.get(i + 1).getPrincipalBlock();
    }
    
    @Override
    protected Block getWestNeighbour(Block child) {
        if (child != getLabel())
            return getLabel();
        if (_parentNode == null)
            return null;
        int i = _parentNode._childNodes.indexOf(this);
        if (i == 0)
            return null;
        return _parentNode._childNodes.get(i - 1).getPrincipalBlock();
    }
}
