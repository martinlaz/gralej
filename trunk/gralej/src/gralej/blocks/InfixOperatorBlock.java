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

import gralej.om.IRelation;

/**
 *
 * @author Martin
 */
public class InfixOperatorBlock extends ContainerBlock {
    public InfixOperatorBlock(BlockPanel panel, IRelation rel, Block op1, Block op2) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getInfixOperatorLayout());

        Label relName = getPanelStyle().getLabelFactory().createInfixOperatorLabel(rel.name(), panel);
        relName.setModel(rel);

        addChild(op1);
        addChild(relName);
        addChild(op2);
        
        sealChildren();
    }
}
