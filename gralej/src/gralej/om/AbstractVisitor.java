package gralej.om;

public abstract class AbstractVisitor implements IVisitor {
    public void visit(IVisitable visitable) {
        throw new RuntimeException("unknown visitable: visitable");
    }

    public void visit(IEntity entity) {
        throw new RuntimeException("unknown entity: visitable");
    }

    public void visit(IFeatureValuePair featVal) {
        // most classes won't need to implement this method
    }

    public abstract void visit(IList ls);

    public abstract void visit(ITag tag);

    public abstract void visit(IAny any);

    public abstract void visit(ITypedFeatureStructure tfs);

    public abstract void visit(ITree tree);
}
