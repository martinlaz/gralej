package gralej.gui.blocks;

abstract class AbstractLayout implements ILayout {
    private int _leadingSpace, _intraSpace, _trailingSpace;

    AbstractLayout() {
    }

    AbstractLayout(int lead, int intra, int trail) {
        setAll(lead, intra, trail);
    }

    void setAll(int lead, int intra, int trail) {
        _leadingSpace = lead;
        _intraSpace = intra;
        _trailingSpace = trail;
    }

    public int getIntraSpace() {
        return _intraSpace;
    }

    public int getLeadingSpace() {
        return _leadingSpace;
    }

    public int getTrailingSpace() {
        return _trailingSpace;
    }

    public void setIntraSpace(int newIntraSpace) {
        _intraSpace = newIntraSpace;
    }

    public void setSetLeadingSpace(int newLeadingSpace) {
        _leadingSpace = newLeadingSpace;
    }

    public void setTrailingSpace(int newTrailingSpace) {
        _trailingSpace = newTrailingSpace;
    }
}
