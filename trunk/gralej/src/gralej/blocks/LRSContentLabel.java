// $Id$
//
// Copyright (C) 2009, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.blocks;

import java.util.LinkedList;

/**
 *
 * @author Martin
 */
public class LRSContentLabel extends ContentLabel {
    LinkedList<LRSNodeBlock> _lrsNodes = new LinkedList<LRSNodeBlock>();

    LRSContentLabel(BlockPanel panel, LabelStyle style, String text) {
        super(panel, style, text);
    }

    void addChildNode(LRSNodeBlock child) {
        _lrsNodes.add(child);
    }

    @Override
    public void flipContentVisibility() {
        if (_lrsNodes.isEmpty())
            return;
        boolean newCollapsedValue = !_lrsNodes.getFirst().isCollapsed();
        for (LRSNodeBlock node : _lrsNodes)
            node.setCollapsed(newCollapsedValue);
    }

    @Override
    protected boolean useTextAltColor() {
        return _lrsNodes.isEmpty() || _lrsNodes.getFirst().isCollapsed();
    }

    @Override
    public boolean isVisible() {
        if (!super.isVisible())
            return false;
        for (Block b = getParent(); b instanceof LRSNodeBlock; b = b.getParent()) {
            LRSNodeBlock lrsNode = (LRSNodeBlock) b;
            if (lrsNode.isCollapsed())
                return false;
            b = lrsNode._parentLabel;
            if (b == null)
                break;
        }
        return true;
    }
}
