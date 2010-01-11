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
import java.util.LinkedList;
import java.util.List;

public class EntityFactory extends gralej.om.EntityFactory {

    public IAny createAny(String value) {
        return new OM.Any(OM.DEFAULT_FLAGS, value);
    }

    public IFeatureValuePair createFeatVal(String feat, IEntity val) {
        return new OM.FeatVal(OM.DEFAULT_FLAGS, feat, val);
    }

    public IRelation createRelation(String name, int arity) {
        java.util.List<IEntity> args = new java.util.ArrayList<IEntity>(arity);
        for (int i = 0; i < arity; ++i) {
            args.add(null);
        }
        return new OM.Relation(OM.DEFAULT_FLAGS, name, args);
    }

    public IRelation createRelation(String name, java.util.List<IEntity> args) {
        return new OM.Relation(OM.DEFAULT_FLAGS, name, args);
    }

    public ITag createTag(int number) {
        return new OM.Tag(number);
    }

    public ITag createTag(int number, IEntity target) {
        OM.Tag t = new OM.Tag(number);
        t.setTarget(target);
        return t;
    }

    public ITree createTree(String label) {
        return new OM.Tree(label, new LinkedList<ITree>());
    }

    public ITree createTree(String label, java.util.List<ITree> children) {
        return new OM.Tree(label, children);
    }

    public IType createType(String typeName) {
        return new OM.Type(OM.DEFAULT_FLAGS, typeName);
    }

    public ITypedFeatureStructure createTFS(IType type) {
        return new OM.TFS(OM.DEFAULT_FLAGS, type, new LinkedList<IFeatureValuePair>());
    }

    public ITypedFeatureStructure createTFS(IType type, java.util.List<IFeatureValuePair> featVals) {
        return new OM.TFS(OM.DEFAULT_FLAGS, type, featVals);
    }

    public IList createList() {
        return new OM.List(OM.DEFAULT_FLAGS, new LinkedList<IEntity>(), null);
    }

    public IList createList(List<IEntity> elements) {
        return new OM.List(OM.DEFAULT_FLAGS, elements, null);
    }
}
