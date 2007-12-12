package gralej.om;

public interface IVisitor {
    void visit(IVisitable visitable);

    void visit(IEntity entity);

    void visit(IFeatureValuePair featVal);

    void visit(IList ls);

    void visit(ITag tag);

    void visit(IAny any);

    void visit(ITypedFeatureStructure tfs);

    void visit(ITree tree);
}
