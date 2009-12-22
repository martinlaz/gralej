// $Id$
//
// Copyright (C) 2009, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.blocks;

import java.awt.Graphics2D;

/**
 *
 * @author Martin
 */
public class LRSBlock extends ContentOwningBlock {
    public LRSBlock(BlockPanel panel, LRSTreeBlock lrsTree) {
        setPanel(panel);
        setLayout(panel.getStyle().getLayoutFactory().getLRSBlockLayout());
        setContent(lrsTree);
        addChild(panel.getStyle().getLabelFactory().createLRSBlockLabel(panel));
        addChild(lrsTree);
        sealChildren();
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        g.setColor(getPanelStyle().getLrsTreeFrameColor());
        g.drawRect(_x, _y, getWidth(), getHeight());
    }
}
