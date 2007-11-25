package gralej.gui.blocks;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

import javax.swing.JPanel;

public class BlockPanel extends JPanel
    implements IBlock {
    
    private static final long serialVersionUID = -2434960385455011813L;
    
    private IBlock _content;
    private int _marginSize;
    private LabelFactory _labfac;
    Cursor _defaultCursor, _handCursor, _currentCursor;
    
    public BlockPanel() {
        _marginSize = Config.getInt("panel.margins.all");
        
        setOpaque(true);
        setBackground(Color.decode(Config.get("panel.background")));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                onMousePressed(e);
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                onMouseMoved(e);
            }
        });
    }
    
    public void setContent(IBlock block) {
        assert _content == null;
        _content = block;
    }
    
    IBlock getContent() {
        return _content;
    }
    
    LabelFactory getLabelFactory() {
        if (_labfac == null)
            return LabelFactory.getInstance();
        return _labfac;
    }
    
    public BlockPanel getPanel() { return this; }
    public void setPanel(BlockPanel panel) {}
    
    public void updateSize() {
        setSize(_content.getWidth(), _content.getHeight());
        _content.setPosition(getX() + _marginSize, getY() + _marginSize);
        revalidate();
    }
    
    @Override
    public int getWidth() {
        return _content.getWidth() + 2 * _marginSize;
    }
    
    @Override
    public int getHeight() {
        return _content.getHeight() + 2 * _marginSize;
    }
    
    @Override
    public Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }
    
    public void setPosition(int x, int y) {}
    
    // hierarchy
    public Iterable<IBlock> getChildren()
        { return Collections.singleton(_content); }
    public boolean isLeaf() { return false; }
    
    public IBlock getParentBlock() { return null; }
    
    public void setParentBlock(IBlock parent) { }
    
    public void paint(Graphics2D g) { paintComponent(g); }
    
    @Override
    public void paint(Graphics g) { paintComponent(g); }
    
    @Override
    protected void paintComponent(Graphics g_) {
        super.paintComponent(g_);
        Graphics2D g = (Graphics2D) g_;
        g.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
            );
        g.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );
        //g.drawRect(getX() + 2, getY() + 2, getWidth() - 4, getHeight() - 4);
        _content.paint(g);
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension d = new Dimension(
            _content.getWidth() + 2*_marginSize,
            _content.getHeight() + 2*_marginSize);
        return d;
    }
    
    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);
        _content.setPosition(x + _marginSize, y + _marginSize);
    }
    
    @Override
    public Rectangle getBounds(Rectangle rv) {
        if (rv == null)
            rv = new Rectangle();
        rv.setBounds(
            getX(), getY(),
            getWidth(), getHeight()
            );
        return rv;
    }
    
    private void onMousePressed(MouseEvent e) {
        //log(e);
        ContentLabel target = findContainingContentLabel(e.getX(), e.getY());
        if (target != null) {
            target.flipContentVisibility();
            updateCursorForPoint(e.getX(), e.getY());
        }
    }
    
    private static boolean blockContainsPoint(IBlock block, int x, int y) {
        int X = block.getX();
        int Y = block.getY();
        int W = block.getWidth();
        int H = block.getHeight();
        
        return
            (x >= X) && (x < X + W)
            &&
            (y >= Y) && (y < Y + H)
            ;
    }
    
    private static IBlock findContainingLeaf(IBlock block, int x, int y) {
        for (IBlock child : block.getChildren())
            if (child.isVisible() && blockContainsPoint(child, x, y))
                return child.isLeaf() ? child : findContainingLeaf(child, x, y);
        return null;
    }
    
    ContentLabel findContainingContentLabel(int x, int y) {
        IBlock b = findContainingLeaf(_content, x, y);
        if (b != null && b instanceof ContentLabel)
            return (ContentLabel) b;
        return null;
    }
    
    static void log(Object message) {
        System.err.println(message);
    }
    
    private void onMouseMoved(MouseEvent ev) {
        if (ev.getID() != MouseEvent.MOUSE_MOVED)
            return;
        updateCursorForPoint(ev.getX(), ev.getY());
    }
    
    private void initCursors() {
        _defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        _handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        _currentCursor = _defaultCursor; 
    }
    
    private void updateCursorForPoint(int x, int y) {
        if (_defaultCursor == null)
            initCursors();
        
        IBlock target = findContainingContentLabel(x, y);
        
        if (target == null)
            setCursor(_defaultCursor);
        else
            setCursor(_handCursor);
    }
    
    public void setCursor(Cursor newCursor) {
        if (newCursor == _currentCursor)
            return;
        super.setCursor(newCursor);
        _currentCursor = newCursor;
    }
}
