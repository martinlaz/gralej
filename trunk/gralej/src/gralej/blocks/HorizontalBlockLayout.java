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

class HorizontalBlockLayout extends BlockLayout {
    
    @Override
    void layoutChildrenOfBlock(ContainerBlock block) {
        int x = block.getX() + getLeadingSpace();
        int y = block.getY();
        int h = block.getHeight();

        for (Block child : block.getChildren()) {
            if (!child.isVisible())
                continue;

            child.setPosition(x, y + h / 2 - child.getHeight() / 2);
            x += child.getWidth();
            x += getIntraSpace();
        }
    }

    @Override
    void updateBlockSize(ContainerBlock block) {
        int w = 0;
        int h = 0;
        int numChildren = 0;

        for (Block child : block.getChildren()) {
            if (!child.isVisible())
                continue;
            w += child.getWidth();
            h = Math.max(h, child.getHeight());
            ++numChildren;
        }

        if (numChildren > 0) {
            w += getLeadingSpace();
            w += (numChildren - 1) * getIntraSpace();
            w += getTrailingSpace();
        }

        block.setSize(w, h);
    }
}
