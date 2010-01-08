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

/**
 *
 * @author Martin
 */
public class TableBlock extends ContentOwningBlock {

    TableBlock(BlockPanel panel, Label heading, AVPairListBlock avPairs) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getAVMLayout());

        addChild(heading);
        addChild(avPairs);

        setContent(avPairs);
    }

    public Label getHeading() {
        return (Label) _children.get(0);
    }

    public AVPairListBlock getAVPairs() {
        return (AVPairListBlock) getContent();
    }
}
