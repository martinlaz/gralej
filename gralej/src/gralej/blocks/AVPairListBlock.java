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

public class AVPairListBlock extends ContainerBlock {

    AVPairListBlock(BlockPanel panel, Iterable<AVPairBlock> avPairs) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getAVPairListLayout());
        
        for (AVPairBlock av : avPairs)
            addChild(av);
        
        sealChildren();
    }

    @Override
    public void update() {
        
        if (getPanelStyle().isAVMLayoutCompact()) {
            super.update();
            return;
        }
        
        updateLayoutManager();
        
        _isUpdatingChildren = true;
        try {
            // max attribute width
            int maw = 0;
            
            for (Block child : getChildren()) {
                AVPairBlock av = (AVPairBlock) child;
                av.update();
                maw = Math.max(maw, av.getAttribute().getWidth());
            }
            
            for (Block child : getChildren()) {
                AVPairBlock av = (AVPairBlock) child;
                Block a = av.getAttribute();
                a.setSize(maw, a.getHeight());
            }
        }
        finally {
            _isUpdatingChildren = false;
        }
        updateSelf();
    }
}
