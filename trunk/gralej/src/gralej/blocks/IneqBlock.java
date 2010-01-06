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

import gralej.Config;

/**
 *
 * @author Martin
 */
public class IneqBlock extends ContainerBlock {
    private static String FUNCTOR_TEXT;

    IneqBlock(BlockPanel panel, Block fs1, Block fs2) {
        setPanel(panel);
        setLayout(panel.getStyle().getLayoutFactory().getListContentLayout());

        if (FUNCTOR_TEXT == null)
            FUNCTOR_TEXT = Config.s("block.ineq.functor.text");

        addChild(fs1);
        addChild(panel.getStyle().getLabelFactory().createRelationLabel(FUNCTOR_TEXT, panel));
        addChild(fs2);
    }
}
