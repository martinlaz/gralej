package gralej.gui.blocks;

import java.awt.Graphics2D;

public interface IBlock {
    // the root of the block object hierarchy
    BlockPanel getPanel();

    void init();

    // visibility
    boolean isVisible();

    void setVisible(boolean visible);

    // size
    int getWidth();

    int getHeight();

    void setSize(int w, int h);

    void updateSize();

    // position
    int getX();

    int getY();

    void setPosition(int x, int y);

    // hierarchy
    Iterable<IBlock> getChildren();

    IBlock getParentBlock();

    boolean isLeaf();

    void paint(Graphics2D g);

    // events
    // mouse click
}
