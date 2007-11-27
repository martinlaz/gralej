package gralej.gui.blocks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class TreeBlock extends ContainerBlock {
    final static int MIN_HDIST = Config.getInt("tree.minDistance.horizontal");
    final static int MIN_VDIST = Config.getInt("tree.minDistance.vertical");
    final static Color EDGE_COLOR = Color.decode(Config.get("tree.edge.color"));
    final static boolean IS_NODE_CONTENT_INITIALLY_VISIBLE = Boolean.parseBoolean(Config.get("tree.node.content.isInitiallyVisible"));
    
    private NodeBlock _root;
    
    TreeBlock(NodeBlock root) {
        _root = root;
        addNode(root);
    }
    
    private void addNode(NodeBlock node) {
        addChild(node);
        for (NodeBlock child : node.getChildNodes())
            addNode(child);
    }
    
    @Override
    public void init() {
        if (IS_NODE_CONTENT_INITIALLY_VISIBLE) {
            super.init();
            return;
        }
        // init all nodes and hide their contents 
        initNode(_root);
        setVisible(true);
    }
    
    private static void initNode(NodeBlock node) {
        node.init();
        for (NodeBlock childNode : node.getChildNodes())
            initNode(childNode);
        node.getContent().setVisible(false);
    }
    
    @Override
    public void updateSize() {
        if (!isVisible())
            return;
        // To compute the size of the tree
        // we first need to layout the nodes;
        // so we do the two things at the same time:
        // size computation and node positioning.
        
        layoutNode(_root, getX(), getY());
        
        int w = 0;
        int h = 0;
        
        for (IBlock node : getChildren()) {
            w = Math.max(w, node.getX() + node.getWidth());
            h = Math.max(h, node.getY() + node.getHeight());
        }
        
        setSize(w - getX(), h - getY());
    }
    
    @Override
    public void setPosition(int x, int y) {
        if (x == getX() && y == getY())
            return;
        
        moveNode(_root, x - getX(), y - getY());
        super.setPosition(x, y);
    }
    
    @Override
    protected void layoutChildren() {
        getPanel().repaint();
    }
    
    private int layoutNode(NodeBlock u, final int x, final int y) {
        if (u.isLeafNode()) {
            u.setPosition(x, y);
            return x + u.getWidth();
        }
        
        int nextX = x;
        int nextY = y + u.getHeight() + MIN_VDIST;
        NodeBlock fc = null; // first child
        NodeBlock lc = null; // last child
        
        for (NodeBlock child : u.getChildNodes()) {
            if (fc == null)
                fc = child;
            else
                nextX += MIN_HDIST;

            nextX = layoutNode(child, nextX, nextY);
            lc = child;
        }
        
        int m = (int) (
                fc.getX() + fc.getWidth() / 2.0
            + (lc.getX() + lc.getWidth() / 2.0 - (fc.getX() + fc.getWidth() / 2.0)) / 2.0
            );
        u.setPosition(m - (int)(u.getWidth() / 2.0), y);
        
        int n = u.getX() + u.getWidth();
        if (n > nextX)
            nextX = n;
        
        if (u.getX() < x) {
            int offX = x - u.getX();
            nextX += offX;
            moveNode(u, offX, 0);
        }
        
        return nextX;
    }
    
    private static void moveNode(NodeBlock node, int offX, int offY) {
        node.setPosition(node.getX() + offX, node.getY() + offY);
        for (NodeBlock childNode : node.getChildNodes())
            moveNode(childNode, offX, offY);
    }
    
    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        g.setColor(EDGE_COLOR);
        drawEdges(_root, g);
    }
    
    private void drawEdges(NodeBlock node, Graphics g) {
        int x1 = node.getX() + node.getWidth() / 2;
        int y1 = node.getY() + node.getHeight();
        
        for (NodeBlock child : node.getChildNodes()) {
            int x2 = child.getX() + child.getWidth() / 2;
            int y2 = child.getY();
            g.drawLine(x1, y1, x2, y2);
            drawEdges(child, g);
        }
    }
}
