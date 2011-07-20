// $Id$
//
// Copyright (C) 2010, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.parsers;

import gralej.om.DescendingVisitor;
import gralej.om.IEntity;
import gralej.om.ITag;
import gralej.om.IVisitable;
import gralej.util.Log;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Martin
 */
class TagBindingVisitor extends DescendingVisitor {
    private Map<Integer,IEntity> _reents = new TreeMap<Integer,IEntity>();
    private List<ITag> _tags = new LinkedList<ITag>();

    TagBindingVisitor(IVisitable obj) {
        obj.accept(this);
        for (ITag tag : _tags)
            tag.setTarget(_reents.get(tag.number()));
    }

    @Override
    public void visit(ITag tag) {
        if (tag.target() == null)
            _tags.add(tag);
        else {
            IEntity prev = _reents.put(tag.number(), tag.target());
            if (prev != null && prev != tag.target())
                Log.warning("more than one reentrancy for tag", tag.number());
            tag.target().accept(this);
        }
    }
}
