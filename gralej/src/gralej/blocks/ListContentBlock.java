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

public class ListContentBlock extends ContainerBlock {
    ListContentBlock(BlockPanel panel, Iterable<Block> listItems, Block tail) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getListContentLayout());
        
        LabelFactory labfac = getPanelStyle().getLabelFactory();
        
        boolean firstAdded = false;
        for (Block item : listItems) {
            if (firstAdded)
                addChild(labfac.createListSeparatorLabel(panel));
            else
                firstAdded = true;

            addChild(item);
        }
        if (tail != null) {
            addChild(labfac.createListTailSeparatorLabel(panel));
            addChild(tail);
        }
        sealChildren();
    }
    
    @Override
    protected Block getWestNeighbour(Block child) {
        int i = indexOf(child);
        if (i == 0)
            return getParent().getWestNeighbour(this);
        return getChildren().get(i - 2).getPrincipalBlock();
    }
    
    @Override
    protected Block getEastNeighbour(Block child) {
        int i = indexOf(child);
        if (i == getChildren().size() - 1)
            return getParent().getWestNeighbour(this);
        return getChildren().get(i + 2).getPrincipalBlock();
    }
}
