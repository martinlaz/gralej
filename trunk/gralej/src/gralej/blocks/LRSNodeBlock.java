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

import java.awt.Graphics2D;
import java.util.List;

/**
 *
 * @author Martin
 */
public class LRSNodeBlock extends NodeBlock {
    Label _parentLabel; // label that points to this node

    LRSNodeBlock(BlockPanel panel, List<Block> labels, List<NodeBlock> childNodes) {
        super(panel);
        setLayout(getPanelStyle().getLayoutFactory().getLRSNodeLayout());

        for (Block child : labels)
            addChild(child);
        _childNodes = childNodes;

        sealChildren();
    }

    void setParentLabel(Label l) {
        _parentLabel = l;
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);

//        g.setColor(Color.BLUE);
//        g.drawRect(_x, _y, getWidth(), getHeight());

        if (_parentLabel == null)
            return;

        int x1 = _x + getWidth() / 2;
        int y1 = _y;
        int x2 = _parentLabel.getX() + _parentLabel.getWidth() / 2;
        int y2 = _parentLabel.getY() + _parentLabel.getHeight();
        g.setColor(getPanelStyle().getTreeEdgeColor());
        g.drawLine(x1, y1, x2, y2);
    }
}
