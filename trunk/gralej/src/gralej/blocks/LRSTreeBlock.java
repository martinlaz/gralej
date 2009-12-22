// $Id$
//
// Copyright (C) 2009, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.blocks;

import java.awt.Graphics;

/**
 *
 * @author Martin
 */
public class LRSTreeBlock extends TreeBlock {
    LRSTreeBlock(BlockPanel panel, LRSNodeBlock root) {
        super(panel, root);
    }
    
//    @Override
//    public void paint(Graphics2D g) {
//        super.paint(g);
//        g.setColor(getPanelStyle().getLrsTreeFrameColor());
//        g.drawRect(_x, _y, getWidth(), getHeight());
//    }

    @Override
    protected void drawEdges(NodeBlock node, Graphics g) {
        // do nothing here; the edges will be drawn elsewhere
    }

    @Override
    protected void updateMinDistanceValues() {
        MIN_HDIST = getPanelStyle().getMinLrsTreeNodesHorizontalDistance();
        MIN_VDIST = getPanelStyle().getMinLrsTreeNodesVerticalDistance();
    }
}
