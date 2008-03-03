package gralej.blocks;

class VerticalCenteredBlockLayout extends VerticalBlockLayout {
    
    @Override
    protected int computeChildX(ContainerBlock parent, Block child) {
        return parent.getX() + (parent.getWidth() - child.getWidth()) / 2;
    }
}
