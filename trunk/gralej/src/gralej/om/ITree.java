package gralej.om;

public interface ITree extends IVisitable {
    String label();
    IEntity content();
    Iterable<ITree> children();
    boolean isLeaf();
}
