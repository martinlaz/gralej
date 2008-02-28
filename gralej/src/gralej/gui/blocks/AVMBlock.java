package gralej.gui.blocks;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

class AVMBlock extends ContentOwningBlock {
    
    static class BracketInfo {
        final int EDGE_SIZE =
                Config.getInt("avm.bracket.edge.length");
        final Color COLOR =
                Config.getColor("avm.bracket.color");
        final boolean ROUNDED =
                Boolean.parseBoolean(Config.get("avm.bracket.style.rounded"));
    }

    AVMBlock(Label sort, AVPairListBlock avPairs) {
        addChild(sort);

        if (!avPairs.isEmpty()) {
            addChild(avPairs);
            setContent(avPairs);
        }
    }
    
    @Override
    public void init() {
        setLayout(getPanel().getLayoutFactory().getAVMLayout());
        super.init();
    }

    Label getTypeLabel() {
        return (Label) _children.get(0);
    }

    AVPairListBlock getAVPairs() {
        return (AVPairListBlock) getContent();
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        
        BracketInfo bi = getPanel().getBracketInfo();
        
        // paint the brackets
        g.setColor(bi.COLOR);
        final int x = getX(), y = getY(), w = getWidth(), h = getHeight();
        final int e = bi.EDGE_SIZE;

        if (bi.ROUNDED) {
            Path2D path = new Path2D.Float();

            // left bracket
            // draw top down, counter-clockwise
            path.moveTo(x + e, y);
            // same first Bezier control point
            path.curveTo(x, y, x, y, x, y + e);
            path.lineTo(x, y + h - e);
            path.curveTo(x, y + h, x, y + h, x + e, y + h);

            g.draw(path);

            // right bracket
            // top down, clockwise
            path.reset();
            path.moveTo(x + w - e, y);
            path.curveTo(x + w, y, x + w, y, x + w, y + e);
            path.lineTo(x + w, y + h - e);
            path.curveTo(x + w, y + h, x + w, y + h, x + w - e, y + h);

            g.draw(path);

            return;
        }

        // the default [style]

        // left
        g.drawLine(x, y, x, y + h);
        g.drawLine(x, y, x + e, y);
        g.drawLine(x, y + h, x + e, y + h);

        // right
        g.drawLine(x + w, y, x + w, y + h);
        g.drawLine(x + w, y, x + w - e, y);
        g.drawLine(x + w, y + h, x + w - e, y + h);
    }
}
