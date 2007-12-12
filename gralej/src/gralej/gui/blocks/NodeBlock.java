package gralej.gui.blocks;

public class NodeBlock extends ContentOwningBlock {
    private java.util.List<NodeBlock> _childNodes;

    NodeBlock(Label label, Block content, java.util.List<NodeBlock> childNodes) {
        setLayout(LayoutFactory.getNodeLayout());

        addChild(label);
        addChild(content);

        setContent(content);

        _childNodes = childNodes;
    }

    Iterable<NodeBlock> getChildNodes() {
        return _childNodes;
    }

    boolean isLeafNode() {
        return _childNodes.isEmpty();
    }
}
