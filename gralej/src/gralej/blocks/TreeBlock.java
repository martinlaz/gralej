package gralej.blocks;

import java.awt.Graphics;
import java.awt.Graphics2D;

public class TreeBlock extends ContainerBlock {

    private NodeBlock _root;
    private int MIN_HDIST, MIN_VDIST;

    TreeBlock(BlockPanel panel, NodeBlock root) {
        setPanel(panel);
        _root = root;
        addNode(root);
    }

    public NodeBlock getRoot() {
        return (NodeBlock) _children.get(0);
    }

    private void addNode(NodeBlock node) {
        addChild(node);
        for (NodeBlock child : node.getChildNodes())
            addNode(child);
    }

    @Override
    public void updateSelf() {
        if (_isUpdatingChildren)
            return;
        // To compute the size of the tree
        // we first need to layout the nodes;
        // so we do the two things at the same time:
        // size computation and node positioning.
        
        MIN_HDIST = getPanelStyle().getMinTreeNodesHorizontalDistance();
        MIN_VDIST = getPanelStyle().getMinTreeNodesVerticalDistance();

        layoutNode(_root, getX(), getY());

        int w = 0;
        int h = 0;

        for (Block node : getChildren()) {
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
        _x = x; _y = y;
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

        int m = (int) (fc.getX() + fc.getWidth() / 2.0 + (lc.getX()
                + lc.getWidth() / 2.0 - (fc.getX() + fc.getWidth() / 2.0)) / 2.0);
        u.setPosition(m - (int) (u.getWidth() / 2.0), y);

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
        g.setColor(getPanelStyle().getTreeEdgeColor());
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
