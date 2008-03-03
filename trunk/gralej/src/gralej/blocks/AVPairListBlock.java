package gralej.blocks;

public class AVPairListBlock extends ContainerBlock {

    AVPairListBlock(BlockPanel panel, Iterable<AVPairBlock> avPairs) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getAVPairListLayout());
        
        for (AVPairBlock av : avPairs)
            addChild(av);
    }

    @Override
    public void update() {
        if (getPanelStyle().isAVMLayoutCompact()) {
            super.update();
            return;
        }
        
        _isUpdatingChildren = true;
        try {
            // max attribute width
            int maw = 0;
            
            for (Block child : getChildren()) {
                AVPairBlock av = (AVPairBlock) child;
                av.update();
                maw = Math.max(maw, av.getAttribute().getWidth());
            }
            
            for (Block child : getChildren()) {
                AVPairBlock av = (AVPairBlock) child;
                Block a = av.getAttribute();
                a.setSize(maw, a.getHeight());
            }
        }
        finally {
            _isUpdatingChildren = false;
        }
        updateSelf();
    }
}
