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

public abstract class NodeBlock extends ContentOwningBlock {
    protected NodeBlock _parentNode;
    protected List<NodeBlock> _childNodes = Collections.EMPTY_LIST;
    private boolean _isCollapsed;

    NodeBlock(BlockPanel panel) {
        setPanel(panel);
    }
    
    public Iterable<NodeBlock> getChildNodes() {
        return _childNodes;
    }

    public boolean isLeafNode() {
        return _childNodes.isEmpty();
    }

    boolean isCollapsed() {
        return _isCollapsed;
    }

    void setCollapsed(boolean newValue) {
        if (_isCollapsed == newValue)
            return;
        _isCollapsed = newValue;
        updateParent();
    }
    
    void setParentNode(NodeBlock parentNode) {
        assert _parentNode == null;
        _parentNode = parentNode;
    }

//    @Override
//    public void setVisible(boolean newValue) {
//        if (isVisible() == newValue || _isUpdatingChildren)
//            return;
//        _isUpdatingChildren = true;
//        try {
//            for (NodeBlock b : _childNodes) {
//                b.setVisible(newValue);
//            }
//        }
//        finally {
//            _isUpdatingChildren = false;
//        }
//        super.setVisible(newValue);
//    }
}
