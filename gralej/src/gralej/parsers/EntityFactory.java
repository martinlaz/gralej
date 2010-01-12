package gralej.parsers;

import gralej.om.IAny;
import gralej.om.IEntity;
import gralej.om.IFeatureValuePair;
import gralej.om.IList;
import gralej.om.IRelation;
import gralej.om.ITag;
import gralej.om.ITree;
import gralej.om.IType;
import gralej.om.ITypedFeatureStructure;
import gralej.om.lrs.ILRSExpr;
import java.util.LinkedList;
import java.util.List;

public class EntityFactory extends gralej.om.EntityFactory {

    @Override
    public IAny newAny(String value) {
        return new OM.Any(OM.DEFAULT_FLAGS, value);
    }

    @Override
    public IFeatureValuePair newFeatVal(String feat, IEntity val) {
        return new OM.FeatVal(OM.DEFAULT_FLAGS, feat, val);
    }

    @Override
    public IRelation newRelation(String name, int arity) {
        java.util.List<IEntity> args = new java.util.ArrayList<IEntity>(arity);
        for (int i = 0; i < arity; ++i) {
            args.add(null);
        }
        return new OM.Relation(OM.DEFAULT_FLAGS, name, args);
    }

    @Override
    public IRelation newRelation(String name, java.util.List<IEntity> args) {
        return new OM.Relation(OM.DEFAULT_FLAGS, name, args);
    }

    @Override
    public ITag newTag(int number) {
        return new OM.Tag(number);
    }

    @Override
    public ITag newTag(int number, IEntity target) {
        OM.Tag t = new OM.Tag(number);
        t.setTarget(target);
        return t;
    }

    @Override
    public ITree newTree(String label) {
        return new OM.Tree(label, new LinkedList<ITree>());
    }

    @Override
    public ITree newTree(String label, java.util.List<ITree> children) {
        return new OM.Tree(label, children);
    }

    @Override
    public IType newType(String typeName) {
        return new OM.Type(OM.DEFAULT_FLAGS, typeName);
    }

    @Override
    public ITypedFeatureStructure newTFS(IType type) {
        return new OM.TFS(OM.DEFAULT_FLAGS, type, new LinkedList<IFeatureValuePair>());
    }

    @Override
    public ITypedFeatureStructure newTFS(IType type, java.util.List<IFeatureValuePair> featVals) {
        return new OM.TFS(OM.DEFAULT_FLAGS, type, featVals);
    }

    @Override
    public IList newList() {
        return new OM.List(OM.DEFAULT_FLAGS, new LinkedList<IEntity>(), null);
    }

    @Override
    public IList newList(List<IEntity> elements) {
        return new OM.List(OM.DEFAULT_FLAGS, elements, null);
    }

    @Override
    public ILRSExpr newLRSExpr(String expr) {
        return LRSExpr.parse(expr);
    }
}
