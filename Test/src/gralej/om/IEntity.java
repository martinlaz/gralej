package gralej.om;

public interface IEntity extends IVisitable {
    // flags
    boolean isHidden();
    boolean isDifferent();
    boolean isStruckout();
    boolean isMultiline();
    boolean isExpanded();
}
