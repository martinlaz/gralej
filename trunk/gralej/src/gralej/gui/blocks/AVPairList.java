package gralej.gui.blocks;

class AVPairList extends ContainerBlock {
    
    AVPairList(IBlock parent) {
        super(parent);
        setLayout(LayoutFactory.getAVPairListLayout());
    }
    
    void init(Iterable<AVPair> avPairs) {
        // max attribute width
        int maw = 0;
        for (AVPair av : avPairs) {
            addChild(av);
            maw = Math.max(maw, av.getAttribute().getWidth());
        }
        
        for (AVPair av : avPairs) {
            IBlock a = av.getAttribute();
            a.setSize(maw, a.getHeight());
        }
    }
}
