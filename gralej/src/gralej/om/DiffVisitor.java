// $Id$
//
// Copyright (C) 2010, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.om;

import gralej.parsers.IDataPackage;
import gralej.util.Log;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author Martin Lazarov
 */
public class DiffVisitor extends AbstractVisitor {

    private static class FoundDiff extends RuntimeException {
        FoundDiff(String diff) { super(diff); }
    }
    private Object _ctx;
    private HashSet<IEntity> _processed = new HashSet<IEntity>();
    public boolean isDiff = true;


    public DiffVisitor(IDataPackage d1, IDataPackage d2) {
        if (!(new DiffVisitor((IEntity) d1.getModel(), (IEntity) d2.getModel()).isDiff)
                && !diffEntities(d1.getInequations(), d2.getInequations())
                && !diffEntities(d1.getResidue(), d2.getResidue())
                )
            isDiff = false;
    }

    public DiffVisitor(IEntity e1, IEntity e2) {
        //_path.addLast(e2);
        _ctx = e2;
        try {
            e1.accept(this);
            isDiff = false;
        }
        catch (Exception ex) { log(ex); }
    }

    private void log(Object obj) {
        Log.debug(obj);
    }

    private void diff(IEntity ent) {
        throw new FoundDiff(ent.text());
    }

    @Override
    public void visit(IList ls1) {
        IList ls2 = (IList) _ctx;
        if (diffEntities(ls1.elements(), ls2.elements()))
            diff(ls1);
    }

    @Override
    public void visit(ITag tag) {
        ITag tag2 = (ITag) _ctx;
        if (tag.number() != tag2.number())
            diff(tag);
        IEntity t1 = tag.target(), t2 = tag2.target();
        if (t1 == null) {
            if (t2 != null)
                diff(tag);
        }
        else if (t2 == null) {
            diff(tag);
        }
        else if (_processed.add(t1)) {
            _ctx = t2;
            t1.accept(this);
        }
    }

    @Override
    public void visit(ITypedFeatureStructure tfs) {
        ITypedFeatureStructure tfs2 = (ITypedFeatureStructure) _ctx;
        if (tfs2 == null)
            diff(tfs);
        if (tfs.type() != null) {
            _ctx = tfs2.type();
            tfs.type().accept(this);
        }
        else if (tfs2.type() != null)
            diff(tfs);
        if (diffEntities(tfs.featureValuePairs(), tfs2.featureValuePairs()))
            diff(tfs);
    }
    
    @Override
    public void visit(IFeatureValuePair fv) {
        IFeatureValuePair fv2 = (IFeatureValuePair) _ctx;
        if (!fv.feature().equals(fv2.feature()))
            diff(fv);
        _ctx = fv2.value();
        fv.value().accept(this);
    }

    @Override
    public void visit(IAny any) {
        IAny any2 = (IAny) _ctx;
        if (!any.value().equals(any2.value()))
            diff(any);
    }

    @Override
    public void visit(IType type) {
        IType type2 = (IType) _ctx;
        if (!type.typeName().equals(type2.typeName()))
            diff(type);
    }

    @Override
    public void visit(ITree tree) {
        ITree tree2 = (ITree) _ctx;
        if (!eqOrBothNull(tree.label(), tree2.label()))
            diff(tree);
        _ctx = tree2.content();
        tree.content().accept(this);
        if (tree.isLeaf() != tree2.isLeaf())
            diff(tree);
        Iterator<ITree> it1 = tree.children().iterator();
        Iterator<ITree> it2 = tree2.children().iterator();

        while (it1.hasNext() && it2.hasNext()) {
            _ctx = it2.next();
            it1.next().accept(this);
        }

        if (it1.hasNext() != it2.hasNext())
            diff(tree);
    }

    @Override
    public void visit(IRelation rel1) {
        IRelation rel2 = (IRelation) _ctx;
        if (!rel1.name().equals(rel2.name()) || rel1.arity() != rel2.arity())
            diff(rel1);
        for (int i = 0; i < rel1.arity(); ++i) {
            _ctx = rel2.arg(i);
            rel1.arg(i).accept(this);
        }
    }

    @Override
    public void visit(ITable tab1) {
        ITable tab2 = (ITable) _ctx;
        if (!eqOrBothNull(tab1.heading(), tab2.heading()))
            diff(tab1);
        if (diffEntities(tab1.rows(), tab2.rows()))
            diff(tab1);
    }
    
    private <T extends IEntity> boolean diffEntities(Iterable<T> i1, Iterable<T> i2) {
        Iterator<T> it1 = i1.iterator();
        Iterator<T> it2 = i2.iterator();

        while (it1.hasNext() && it2.hasNext()) {
            _ctx = it2.next();
            it1.next().accept(this);
        }

        return it1.hasNext() != it2.hasNext();
    }

    private static boolean eqOrBothNull(Object o1, Object o2) {
        if (o1 == null)
            return o2 == null;
        if (o2 == null)
            return false;
        return o1.equals(o2);
    }
}
