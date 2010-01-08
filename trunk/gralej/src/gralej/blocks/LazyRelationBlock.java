/*
 *  $Id $
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

import gralej.om.IEntity;
import gralej.om.IRelation;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Martin
 */
public class LazyRelationBlock extends ContentOwningBlock {
    private BlockCreator _blockCreator;

    public LazyRelationBlock(BlockPanel panel, IRelation rel, BlockCreator blockCreator) {
        _blockCreator = blockCreator;

        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getRelationLayout());

        setModel(rel);

        RelationContentLabel relName = new RelationContentLabel(panel,
                getPanelStyle().getLabelFactory().getLabelStyle("relation"),
                rel);
        addChild(relName);
    }

    @Override
    public Block getContent() {
        if (_content == null) {
            List<Block> argBlocks = new LinkedList<Block>();
            for (IEntity arg : ((IRelation) getModel()).args()) {
                argBlocks.add(_blockCreator.createBlock(arg, false));
            }
            _content = new RelationArgsBlock(getPanel(), argBlocks);
            // don't make it visible,
            // the content label will do it
            _content.update();
            _content.setVisible(false);
            lastAddChild(_content);
        }
        return _content;
    }

}
