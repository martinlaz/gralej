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
import java.util.List;
import java.util.Stack;

public class AVMNodeBlock extends NodeBlock {

    @SuppressWarnings("unchecked")
    AVMNodeBlock(BlockPanel panel, Label label, Block content) {
        this(panel, label, content, Collections.EMPTY_LIST);
    }

    AVMNodeBlock(BlockPanel panel, Label label, Block content, List<NodeBlock> childNodes) {
        super(panel);
        setLayout(getPanelStyle().getLayoutFactory().getNodeLayout());

        if (label != null)
            addChild(label);
        if (content != null) {
            addChild(content);
            setContent(content);
        }
        else if (label == null)
            throw new RuntimeException("there can't be NodeBlock with label and content both equal to null");

        sealChildren();

        _childNodes = childNodes;
    }

    public ContentLabel getLabel() {
        return (ContentLabel) _children.get(0);
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
                s.push(b);
                Block avs = b.getChildren().get(1);
                if (avs.isVisible())
                    for (Block bb : avs.getChildren())
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
