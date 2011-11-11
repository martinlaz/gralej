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

import java.util.List;

/**
 *
 * @author Martin
 */
public class RelationArgsBlock extends ContainerBlock {
    public RelationArgsBlock(BlockPanel panel, List<Block> args) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getRelationArgsLayout());

        LabelFactory labfac = getPanelStyle().getLabelFactory();

        if (args.size() > 0) {
            addChild(labfac.createRelationLabel("(", panel));
        }

        boolean comma = false;
        for (Block arg : args) {
            if (comma)
                addChild(labfac.createRelationLabel(",", panel));
            else
                comma = true;

            addChild(arg);
        }

        if (args.size() > 0) {
            addChild(labfac.createRelationLabel(")", panel));
        }

        sealChildren();
    }
}
