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

class VerticalBlockLayout extends BlockLayout {
    
    // default child alignment: left
    protected int computeChildX(ContainerBlock parent, Block child) {
        return parent.getX();
    }
    
    @Override
    void layoutChildrenOfBlock(ContainerBlock block) {
        int y = block.getY() + getLeadingSpace();

        for (Block child : block.getChildren()) {
            if (!child.isVisible())
                continue;
            child.setPosition(computeChildX(block, child), y);
            y += child.getHeight();
            y += getIntraSpace();
        }
    }
    
    @Override
    void updateBlockSize(ContainerBlock block) {
        int w = 0;
        int h = getLeadingSpace();
        int numChildren = 0;

        for (Block child : block.getChildren()) {
            if (!child.isVisible())
                continue;
            w = Math.max(w, child.getWidth());
            h += child.getHeight();
            numChildren++;
        }

        if (numChildren > 1)
            h += (numChildren - 1) * getIntraSpace();
        h += getTrailingSpace();

        block.setSize(w, h);
    }
}
