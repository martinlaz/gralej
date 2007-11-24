package gralej.gui.blocks;

public class Node extends ContentOwningBlock {
    private java.util.List<Node> _childNodes;
    
    Node(Tree parent) {
        super(parent);
        setLayout(LayoutFactory.getNodeLayout());
    }
    
    void init(Label label, IBlock content, java.util.List<Node> childNodes) {
        addChild(label);
        addChild(content);
        
        setContent(content);
        
        _childNodes = childNodes;
    }
    
    Iterable<Node> getChildNodes() {
        return _childNodes;
    }
    
    boolean isLeafNode() {
        return _childNodes.isEmpty();
    }
}
