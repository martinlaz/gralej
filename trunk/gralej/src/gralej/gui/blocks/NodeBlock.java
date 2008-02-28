package gralej.gui.blocks;

class NodeBlock extends ContentOwningBlock {
    private java.util.List<NodeBlock> _childNodes;

    NodeBlock(Label label, Block content, java.util.List<NodeBlock> childNodes) {
        addChild(label);
        addChild(content);

        setContent(content);

        _childNodes = childNodes;
    }
    
    @Override
    public void init() {
        setLayout(getPanel().getLayoutFactory().getNodeLayout());
        super.init();
    }

    Iterable<NodeBlock> getChildNodes() {
        return _childNodes;
    }

    boolean isLeafNode() {
        return _childNodes.isEmpty();
    }
}
