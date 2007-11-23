package gralej.gui.blocks;

class AVPairList extends ContainerBlock {
    
    AVPairList(IBlock parent) {
        super(parent);
        setLayout(new VerticalLayout());
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
    
    /*
    @Override
    public void updateSize() {
        if (!isVisible())
            return;
       
        int h = VerticalLayout.SPACE_BEFORE;
        // max attribute width
        int maw = 0;
        // max value width
        int mvw = 0;
        
        int numChildren = 0;
        
        for (IBlock child : getChildren()) {
            AVPair av = (AVPair) child;
            maw = Math.max(maw, av.getAttribute().getWidth()); 
            mvw = Math.max(mvw, av.getValue().getWidth());
            h += av.getHeight();
            numChildren++; 
        }
        
        if (numChildren > 1)
            h += (numChildren - 1) * VerticalLayout.SPACE_BETWEEN;
        h += VerticalLayout.SPACE_AFTER;
        
        _maxAttributeWidth = maw;
        int w = maw + mvw + HorizontalLayout.SPACE_BETWEEN;
        setSize(w, h);
    }
    */
}
