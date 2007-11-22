package gralej.gui.blocks;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JPanel;

public class BlockPanel extends JPanel
    implements IBlock {
    
    private static final long serialVersionUID = -2434960385455011813L;
    
    private IBlock _content;
    private int _marginSize = 10;
    private LabelFactory _labfac;
    private java.util.List<ContentLabel> _contentLabels;
    Cursor _defaultCursor, _handCursor, _currentCursor;
    
    public BlockPanel() {
        _contentLabels = new LinkedList<ContentLabel>();
        setOpaque(true);
        
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
        _content.setPosition(_marginSize, _marginSize);
        repaint();
    }
    
    public void setPosition(int x, int y) {}
    
    // hierarchy
    public Iterable<IBlock> getChildren()
        { return Collections.singleton(_content); }
    
    public IBlock getParentBlock() { return null; }
    
    public void setParentBlock(IBlock parent) { }
    
    public void paint(Graphics2D g) {}
    
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
        _content.paint(g);
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension d = new Dimension(
            _content.getWidth() + 2*_marginSize,
            _content.getHeight() + 2*_marginSize);
        //System.err.println("pref size: " + d);
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
            0, 0,
            _content.getWidth() + 2*_marginSize,
            _content.getHeight() + 2*_marginSize
            );
        //System.err.println("-- get bounds: " + rv);
        return rv;
    }
    
    private void onMousePressed(MouseEvent e) {
        ContentLabel target = findContainingBlock(e.getX(), e.getY());
        if (target != null) {
            target.flipContentVisibility();
            updateCursorForPoint(e.getX(), e.getY());
        }
    }
    
    ContentLabel findContainingBlock(int x, int y) {
        //
        // Linear search in several hundreds
        // of items, a few times a second???
        // Not really nice but it works fine for now.
        // If it turns out too slow, we'll figure
        // a better way to do it.
        //
        for (ContentLabel label : _contentLabels) {
            if (label.contains(x, y)) {
                // FIXME: bang! some major design flaw here
                // "deep visibility"
                if (isItReallyVisible(label)) 
                    return label;
            }
        }
        return null;
    }
    
    private static boolean isItReallyVisible(IBlock block) {
        do {
            if (!block.isVisible())
                return false;
            block = block.getParentBlock();
        }
        while (block != null);
        
        return true;
    }

    void addContentLabel(ContentLabel contentLabel) {
        _contentLabels.add(contentLabel);
        if (_contentLabels.size() > 1000)
            log("ContentLabel list size: " + _contentLabels.size());
    }
    
    void log(Object message) {
        System.err.println(message);
    }
    
    private void onMouseMoved(MouseEvent ev) {
        if (ev.getID() != MouseEvent.MOUSE_MOVED)
            return;
        updateCursorForPoint(ev.getX(), ev.getY());
    }
    
    private void initCursors() {
        _defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        _handCursor= Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        _currentCursor = _defaultCursor; 
    }
    
    private void updateCursorForPoint(int x, int y) {
        if (_defaultCursor == null)
            initCursors();
        
        IBlock target = findContainingBlock(x, y);
        
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
