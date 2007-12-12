package gralej.gui.blocks;

interface ILayout {
    void layoutBlockChildren(IBlock block);

    void updateBlockSize(IBlock block);

    int getLeadingSpace();

    void setSetLeadingSpace(int newLeadingSpace);

    int getIntraSpace();

    void setIntraSpace(int newIntraSpace);

    int getTrailingSpace();

    void setTrailingSpace(int newTrailingSpace);
}
