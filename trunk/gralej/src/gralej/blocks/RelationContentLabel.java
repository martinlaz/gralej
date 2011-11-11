// $Id$
//
// Copyright (C) 2010, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.blocks;

import gralej.om.IRelation;

/**
 *
 * @author Martin
 */
public class RelationContentLabel extends ContentLabel {

    RelationContentLabel(BlockPanel panel, LabelStyle style, IRelation rel) {
        super(panel, style, rel.name() + (rel.arity() == 0 ? "" : "(...)"));
        setModel(rel);
    }

    public IRelation getRelation() {
        return (IRelation) getModel();
    }

    @Override
    public void flipContentVisibility() {
        super.flipContentVisibility();
        if (_content.isVisible() || getRelation().arity() == 0)
            setText(getRelation().name());
        else
            setText(getRelation().name() + "(...)");
    }

    @Override
    protected boolean useTextAltColor() {
        return _content == null || !_content.isVisible();
    }
}
