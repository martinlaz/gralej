package gralej.gui.blocks;

public class VerticalCenteredLayout extends VerticalLayout {

    VerticalCenteredLayout() {
    }

    VerticalCenteredLayout(int lead, int intra, int trail) {
        super(lead, intra, trail);
    }

    @Override
    protected int computeChildX(IBlock parent, IBlock child) {
        return parent.getX() + (parent.getWidth() - child.getWidth()) / 2;
    }
}
