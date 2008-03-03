package gralej.gui.blocks;

class VerticalCenteredLayout extends VerticalLayout {
    
    VerticalCenteredLayout(String name) {
        super(name);
    }

    @Override
    protected int computeChildX(IBlock parent, IBlock child) {
        return parent.getX() + (parent.getWidth() - child.getWidth()) / 2;
    }
}
