package gralej.gui.blocks;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class BlockPanel extends JPanel implements IBlock {

    private static final long serialVersionUID = -2434960385455011813L;
    private static final double SCALE_DELTA = 1.2; // scaling factor
    
    // AVM bracket info
    // stored here to reduce the memory footprint
    // of the AVMBlock class
    final private AVMBlock.BracketInfo _avmBracketInfo
            = new AVMBlock.BracketInfo();
    
    private LabelFactory _labfac;
    private LayoutFactory _layfac;

    private IBlock _content;
    private int _marginSize;
    private Cursor _defaultCursor, _handCursor, _currentCursor;
    private double _scaleFactor = 1;
    private JPanel _drawingPane;
    private boolean _autoResize;
    private boolean _compactAvmLayout;
    private boolean _autoExpandTags;
    private Set<Integer> _expandedTags = new TreeSet<Integer>();

    private class DrawingPane extends JPanel {
        @Override
        protected void paintComponent(Graphics g_) {
            super.paintComponent(g_);
            Graphics2D g = (Graphics2D) g_;
            AffineTransform savedTransform = null;
            if (_scaleFactor != 1) {
                savedTransform = g.getTransform();
                g.transform(AffineTransform.getScaleInstance(_scaleFactor,
                        _scaleFactor));
            }
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // g.drawRect(getX(), getY(), getWidth()-1, getHeight()-1);
            _content.paint(g);
            if (savedTransform != null)
                g.setTransform(savedTransform);
        }
    }

    public BlockPanel(IBlock content) {
        super(new BorderLayout());
        
        _labfac = LabelFactory.getInstance();
        _layfac = LayoutFactory.getInstance();

        _drawingPane = new DrawingPane();
        _drawingPane
                .setBackground(Config.getColor("panel.background"));

        JScrollPane scrollPane = new JScrollPane(_drawingPane);
        int vUnitIncrement = Config
                .getInt("panel.scrollbar.vertical.unitIncrement");
        scrollPane.getVerticalScrollBar().setUnitIncrement(vUnitIncrement);
        add(scrollPane, BorderLayout.CENTER);

        _marginSize = Config.getInt("panel.margins.all");
        _scaleFactor = Double.parseDouble(Config.get("panel.scaleFactor"));
        _autoResize = Boolean.parseBoolean(Config.get("panel.autoResize"));
        _autoExpandTags = Boolean.parseBoolean(Config.get("panel.autoExpandTags"));
        _compactAvmLayout = Boolean.parseBoolean(Config.get("layout.avm.compact"));
        
        final BlockPanel thisPanel = this;

        _drawingPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!thisPanel.hasFocus())
                    thisPanel.requestFocus(true);
                onMousePressed(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // System.out.println("mouse clicked " + e.getClickCount() + "
                // times");
            }
        });

        _drawingPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                onMouseMoved(e);
            }
        });

        setContent(content);
    }
    
    public JPanel getDrawingPane() {
        return _drawingPane;
    }

    public boolean getAutoResize() {
        return _autoResize;
    }

    public void setAutoResize(boolean newValue) {
        _autoResize = newValue;
    }

    public void init() {
        if (_content != null)
            _content.init();
    }
    
    public boolean isAvmLayoutCompact() {
        return _compactAvmLayout;
    }

    private void setContent(IBlock block) {
        assert _content == null;
        ((Block) block).setParentBlock(this);
        _content = block;
        _content.init();
        _expandedTags.clear(); // needed only during init
    }

    IBlock getContent() {
        return _content;
    }

    LabelFactory getLabelFactory() {
        return _labfac;
    }
    
    LayoutFactory getLayoutFactory() {
        return _layfac;
    }
    
    AVMBlock.BracketInfo getBracketInfo() {
        return _avmBracketInfo;
    }

    public BlockPanel getPanel() {
        return this;
    }

    public void setPanel(BlockPanel panel) {
    }

    public void updateSize() {
        _content.setPosition(_marginSize, _marginSize);

        Dimension prefSize = getScaledSize();

        _drawingPane.setPreferredSize(prefSize);
        _drawingPane.setSize(prefSize);
        _drawingPane.revalidate();
        _drawingPane.repaint();

        if (_autoResize)
            pack(getParent());
    }

    public Dimension getScaledSize() {
        Dimension size = new Dimension(scale(_content.getWidth() + 2
                * _marginSize), scale(_content.getHeight() + 2 * _marginSize));
        return size;
    }

    private static void pack(Component c) {
        if (c == null)
            return;
        if (c instanceof JFrame)
            ((JFrame) c).pack();
        else if (c instanceof JInternalFrame)
            ((JInternalFrame) c).pack();
        else
            pack(c.getParent());
    }

    public void setPosition(int x, int y) {
    }

    // hierarchy
    public Iterable<IBlock> getChildren() {
        return Collections.singleton(_content);
    }

    public boolean isLeaf() {
        return false;
    }

    public IBlock getParentBlock() {
        return null;
    }

    public void setParentBlock(IBlock parent) {
    }

    public void paint(Graphics2D g) {
        _drawingPane.paint(g);
    }

    public void setScaleFactor(double newValue) {
        if (newValue == _scaleFactor)
            return;
        if (newValue <= 0.0)
            throw new IllegalArgumentException("Scale factor must be positive");
        _scaleFactor = newValue;
        updateSize();
    }

    public double getScaleFactor() {
        return _scaleFactor;
    }

    public void increaseScaleFactor() {
        setScaleFactor(getScaleFactor() * SCALE_DELTA);
    }

    public void decreaseScaleFactor() {
        setScaleFactor(getScaleFactor() / SCALE_DELTA);
    }

    private int scale(int n) {
        return (int) (n * _scaleFactor);
    }

    private int unscale(int n) {
        return (int) (n / _scaleFactor);
    }
    
    boolean _isShowingCollapsedFeatures = true;
    //Set<IBlock> _collapsedFeatures = new HashSet<IBlock>();
    
    public void showCollapsedFeatures(boolean doShow) {
        if (_isShowingCollapsedFeatures == doShow)
            return;
        _isShowingCollapsedFeatures = doShow;
    }

    private void onMousePressed(MouseEvent e) {
        // log(e);
        int x = unscale(e.getX());
        int y = unscale(e.getY());
        ContentLabel target = findContainingContentLabel(x, y);
        if (target != null) {
            target.flipContentVisibility();
            // feature hiding
            if (!_isShowingCollapsedFeatures) {
                if (target.getParentBlock() instanceof AVPairBlock)
                    target.getParentBlock().setVisible(false);
            }
            updateCursorForPoint(x, y);
        }
    }

    private static boolean blockContainsPoint(IBlock block, int x, int y) {
        int X = block.getX();
        int Y = block.getY();
        int W = block.getWidth();
        int H = block.getHeight();

        return (x >= X) && (x < X + W) && (y >= Y) && (y < Y + H);
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
        updateCursorForPoint(unscale(ev.getX()), unscale(ev.getY()));
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
            updateCursor(_defaultCursor);
        else
            updateCursor(_handCursor);
    }

    void updateCursor(Cursor newCursor) {
        if (newCursor == _currentCursor)
            return;
        _drawingPane.setCursor(newCursor);
        _currentCursor = newCursor;
    }
    
    public boolean getAutoExpandTags() {
        return _autoExpandTags;
    }
    
    Set<Integer> getExpandedTags() {
        return _expandedTags;
    }
}
