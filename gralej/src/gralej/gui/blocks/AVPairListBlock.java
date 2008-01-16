package gralej.gui.blocks;

class AVPairListBlock extends ContainerBlock {

    AVPairListBlock(Iterable<AVPairBlock> avPairs) {
        setLayout(LayoutFactory.getAVPairListLayout());

        for (AVPairBlock av : avPairs)
            addChild(av);
    }

    @Override
    public void init() {
        if (getPanel().isAvmLayoutCompact()) {
            super.init();
            return;
        }
        // max attribute width
        int maw = 0;

        for (IBlock child : getChildren()) {
            AVPairBlock av = (AVPairBlock) child;
            av.init();
            maw = Math.max(maw, av.getAttribute().getWidth());
        }

        for (IBlock child : getChildren()) {
            AVPairBlock av = (AVPairBlock) child;
            IBlock a = av.getAttribute();
            a.setSize(maw, a.getHeight());
        }

        setVisible(true);
    }
}
