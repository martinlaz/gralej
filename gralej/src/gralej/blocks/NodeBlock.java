package gralej.blocks;

import java.util.List;

public class NodeBlock extends ContentOwningBlock {
    private List<NodeBlock> _childNodes;

    NodeBlock(BlockPanel panel, Label label, Block content, List<NodeBlock> childNodes) {
        setPanel(panel);
        setLayout(getPanelStyle().getLayoutFactory().getNodeLayout());
        
        addChild(label);
        addChild(content);

        setContent(content);

        _childNodes = childNodes;
    }
    
    public ContentLabel getLabel() {
        return (ContentLabel) _children.get(0);
    }
    
    public Iterable<NodeBlock> getChildNodes() {
        return _childNodes;
    }

    public boolean isLeafNode() {
        return _childNodes.isEmpty();
    }
}
