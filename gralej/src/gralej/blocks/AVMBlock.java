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
import java.awt.geom.Path2D;

public class AVMBlock extends ContentOwningBlock {

    AVMBlock(BlockPanel panel, Label sort, AVPairListBlock avPairs) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getAVMLayout());
        
        addChild(sort);
        lastAddChild(avPairs);
        setContent(avPairs);
    }
    
    public Label getTypeLabel() {
        return (Label) _children.get(0);
    }

    public AVPairListBlock getAVPairs() {
        return (AVPairListBlock) getContent();
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        
        BlockPanelStyle style = getPanelStyle();
        
        // paint the brackets
        g.setColor(style.getAVMBracketColor());
        final int x = getX(), y = getY(), w = getWidth(), h = getHeight();
        final int e = style.getAVMBracketEdgeLength();

        if (style.isAVMBracketRounded()) {
            Path2D path = new Path2D.Float();

            // left bracket
            // draw top down, counter-clockwise
            path.moveTo(x + e, y);
            // same first Bezier control point
            path.curveTo(x, y, x, y, x, y + e);
            path.lineTo(x, y + h - e);
            path.curveTo(x, y + h, x, y + h, x + e, y + h);

            g.draw(path);

            // right bracket
            // top down, clockwise
            path.reset();
            path.moveTo(x + w - e, y);
            path.curveTo(x + w, y, x + w, y, x + w, y + e);
            path.lineTo(x + w, y + h - e);
            path.curveTo(x + w, y + h, x + w, y + h, x + w - e, y + h);

            g.draw(path);

            return;
        }

        // the default [style]

        // left
        g.drawLine(x, y, x, y + h);
        g.drawLine(x, y, x + e, y);
        g.drawLine(x, y + h, x + e, y + h);

        // right
        g.drawLine(x + w, y, x + w, y + h);
        g.drawLine(x + w, y, x + w - e, y);
        g.drawLine(x + w, y + h, x + w - e, y + h);
    }
}
