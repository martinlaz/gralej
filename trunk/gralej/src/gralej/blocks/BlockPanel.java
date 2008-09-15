/**
 * @version $Id$
 */

package gralej.blocks;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Martin
 */
public class BlockPanel implements StyleChangeListener {
    final static int ZOOM_DELTA = 10;
    
    Map<ContainerBlock,Set<Integer>> _expandedTags;
    RootBlock _content;
    BlockPanelStyle _style;
    int _zoom = 100;
    double _scaleFactor = 1;
    JPanel _ui;
    DrawingPane _canvas;
    ScrollPane _scrollPane;
    boolean _autoResize;
    boolean _autoExpandTags;
    boolean _displayHiddenFeatures;
    boolean _selectOnClick;
    private Cursor _defaultCursor, _handCursor, _currentCursor;
    private ContentLabel _lastHit;
    private Block _selectedBlock;
    private static Stroke _dashedStroke = new BasicStroke(1, BasicStroke.CAP_BUTT, 
                            BasicStroke.JOIN_ROUND, 0,  new float[]{2}, 0);
    
    private int _lastMousePressedX;
    private int _lastMousePressedY;
    
    protected Set<ChangeListener> _changeListeners = new HashSet<ChangeListener>();
    
    private class DrawingPane extends JPanel {
        @Override
        protected void paintComponent(Graphics g_) {
            super.paintComponent(g_);
            Graphics2D g = (Graphics2D) g_;
            AffineTransform savedTransform = null;
            if (_zoom != 100) {
                savedTransform = g.getTransform();
                g.transform(AffineTransform.getScaleInstance(
                        _scaleFactor, _scaleFactor));
            }
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            // g.drawRect(getX(), getY(), getWidth()-1, getHeight()-1);
            
            final int N = 2;
            
            boolean hasSelection = _selectedBlock != null
                    && _selectedBlock.isVisible()
                    && !_selectedBlock.isHiddenByAncestor();
            
            if (hasSelection) {
                // fill background
                g.setColor(_style.getSelectionBackgroundColor());
                g.fillRect(
                        _selectedBlock.getX() - N,
                        _selectedBlock.getY() - N,
                        _selectedBlock.getWidth() + N * 2,
                        _selectedBlock.getHeight() + N * 2
                        );
            }
            
            _content.paint(g);
            
            if (hasSelection) {
                // draw the frame
                g.setColor(_style.getSelectionFrameColor());
                g.setStroke(_dashedStroke);
                g.drawRect(
                        _selectedBlock.getX() - N + 1,
                        _selectedBlock.getY() - N + 1,
                        _selectedBlock.getWidth() + N * 2 - 2,
                        _selectedBlock.getHeight() + N * 2 - 2
                        );
            }
            
            if (savedTransform != null)
                g.setTransform(savedTransform);
        }
    }
    
    private class ScrollPane extends JScrollPane {
        ScrollPane(final DrawingPane canvas, final BlockPanel owner) {
            super(canvas);
            
            canvas.addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (!e.isControlDown()) {
                        processMouseWheelEvent(e);
                        return;
                    }
                    
                    Rectangle r = getViewport().getViewRect();
                    int relX = e.getX() - (int) r.getX();
                    int relY = e.getY() - (int) r.getY();

                    // unscale
                    double x = e.getX() / owner.getScaleFactor();
                    double y = e.getY() / owner.getScaleFactor();

                    if (e.getWheelRotation() < 0)
                        owner.zoomIn();
                    else
                        owner.zoomOut();

                    // scale with the updated factor
                    x *= owner.getScaleFactor();
                    y *= owner.getScaleFactor();

                    r.setRect(x - relX, y - relY, r.getWidth(), r.getHeight());
                    canvas.scrollRectToVisible(r);
                }
            });
        }
    }
    
    public BlockPanel(gralej.om.IVisitable contentModel) {
        this(contentModel, BlockPanelStyle.getInstance());
    }
    
    public BlockPanel(gralej.om.IVisitable contentModel, BlockPanelStyle style) {
        this(contentModel, style, Boolean.parseBoolean(Config.get("behavior.autoexpandtags")));
    }
    
    public BlockPanel(gralej.om.IVisitable contentModel, BlockPanelStyle style, boolean autoExpandTags) {
        final BlockPanel thisPanel = this;
        
        _ui = new JPanel() {
            @Override
            public void removeNotify() {
                super.removeNotify();
                _style.removeStyleChangeListener(thisPanel);
            }
        };
        _ui.setLayout(new BorderLayout());
        
        _canvas = new DrawingPane();
        
        _scrollPane = new ScrollPane(_canvas, this);
        int vUnitIncrement = Config.getInt(
                "block.panel.scrollbar.vertical.unitIncrement");
        _scrollPane.getVerticalScrollBar().setUnitIncrement(vUnitIncrement);
        _ui.add(_scrollPane, BorderLayout.CENTER);
        
        _autoResize = Boolean.parseBoolean(Config.get("behavior.alwaysfitsize"));
        _autoExpandTags = autoExpandTags;
        _displayHiddenFeatures = Boolean.parseBoolean(Config.get("behavior.displayModelHiddenFeatures"));
        _selectOnClick = Boolean.parseBoolean(Config.get("behavior.selectOnClick"));
        
        _canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!_ui.hasFocus())
                    _ui.requestFocus(true);
                onMousePressed(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                onMouseReleased(e);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                onMouseExited(e);
            }
            
        });
        _canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                onMouseMoved(e);
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                onMouseDragged(e);
            }
        });
        
        setStyle(style);
        
        _content = new RootBlock(
                this,
                new BlockCreator(this).createBlock(contentModel)
                );
        _content.update();
        if (!Boolean.parseBoolean(Config.get("behavior.nodeContentInitiallyVisible"))) {
            showNodeContents(false);
        }
    }
    
    public void addChangeListener(ChangeListener listener) {
        _changeListeners.add(listener);
    }
    
    public void removeChangeListener(ChangeListener listener) {
        _changeListeners.remove(listener);
    }
    
    protected void fireStateChanged() {
        ChangeEvent ev = new ChangeEvent(this);
        for (ChangeListener cl : _changeListeners)
            cl.stateChanged(ev);
    }
    
    public void setStyle(BlockPanelStyle newStyle) {
        if (_style == newStyle) return;
        if (_style != null) _style.removeStyleChangeListener(this);
        _style = newStyle;
        _style.addStyleChangeListener(this);
        styleChanged(null);
    }
    
    public void showNodeContents(boolean visible) {
        if (!(_content.getContent() instanceof TreeBlock))
            return;
        for (Block b : ((TreeBlock)_content.getContent()).getChildren()) {
            NodeBlock node = (NodeBlock) b;
            node.getContent().setVisible(visible);
        }
    }
    
    void updateSelf() {
        _content.setPosition(_style.getMargin(), _style.getMargin());

        Dimension prefSize = getScaledSize();

        _canvas.setPreferredSize(prefSize);
        _canvas.setSize(prefSize);
        _canvas.revalidate();
        _canvas.repaint();
        
        if (_autoResize)
            pack(_canvas.getParent());
        
        fireStateChanged();
    }
    
    private void pack(Component c) {
        if (c == null)
            return;
        if (c instanceof JFrame)
            ((JFrame) c).pack();
        else if (c instanceof JInternalFrame)
            ((JInternalFrame) c).pack();
        else if (c instanceof BlockPanelContainer)
            ((BlockPanelContainer) c).panelResized(this);
        else
            pack(c.getParent());
    }
    
    public void setSelectedBlock(Block b) {
        if (_selectedBlock == b)
            return;
        _selectedBlock = b;
        _canvas.repaint();
    }
    
    public void setAutoResize(boolean autoResize) {
        _autoResize = autoResize;
    }
    
    boolean isAutoExpandingTags() {
        return _autoExpandTags;
    }
    
    public boolean isDisplayingModelHiddenFeatures() {
        return _displayHiddenFeatures;
    }
    
    public void setDisplayingModelHiddenFeatures(boolean b) {
        if (_displayHiddenFeatures == b)
            return;
        _displayHiddenFeatures = b;
        _content.update();
    }
    
    public void setZoom(int zoom) {
        if (zoom <= 0 || zoom == _zoom)
            return;
        _zoom = zoom;
        _scaleFactor = _zoom / 100.0;
        updateSelf();
    }
    
    public int getZoom() {
        return _zoom;
    }
    
    public double getScaleFactor() {
        return _scaleFactor;
    }
    
    public void zoomIn() {
        setZoom(getZoom() + ZOOM_DELTA);
    }

    public void zoomOut() {
        setZoom(getZoom() - ZOOM_DELTA);
    }
    
    public Dimension getScaledSize() {
        Dimension size = new Dimension(
                scale(_content.getWidth() + 2 * _style.getMargin()),
                scale(_content.getHeight() + 2 * _style.getMargin()));
        return size;
    }
    
    private int scale(int n) {
        return (int) (n * _scaleFactor);
    }
    
    private int unscale(int n) {
        return (int) (n / _scaleFactor);
    }
    
    public void styleChanged(Object sender) {
        _canvas.setBackground(_style.getBackgroundColor());
        if (_content != null)
            _content.update();
        _canvas.repaint();
    }

    public JPanel getCanvas() {
        return _canvas;
    }
    
    public JPanel getUI() {
        return _ui;
    }
    
    public BlockPanelStyle getStyle() {
        return _style;
    }
    
    public RootBlock getContent() {
        return _content;
    }
    
    Set<Integer> getExpandedTags(ContainerBlock block) {
        if (_expandedTags == null)
            _expandedTags = new HashMap<ContainerBlock,Set<Integer>>();
        Set<Integer> tags = _expandedTags.get(block);
        if (tags == null) {
            tags = new TreeSet<Integer>();
            _expandedTags.put(block, tags);
        }
        return tags;
    }
    
    protected void onMousePressed(MouseEvent e) {
        _lastMousePressedX = e.getX();
        _lastMousePressedY = e.getY();
    }
    
    protected void onMouseReleased(MouseEvent e) {
        int x = unscale(e.getX());
        int y = unscale(e.getY());
        if (e.getButton() == MouseEvent.BUTTON1) { // left button
            ContentLabel target = findContainingContentLabel(x, y);
            if (target != null) {
                int relX = e.getX() - scale(target.getX());
                int relY = e.getY() - scale(target.getY());
                target.flipContentVisibility();
                scrollTo(target, e.getX() - relX, e.getY() - relY, e);
                if (_selectOnClick)
                    setSelectedBlock(target);
                //updateCursorForPoint(x, y);
            }
            else {
                setSelectedBlock(null);
            }
        }
        else if (e.getButton() == MouseEvent.BUTTON3) { // right button
            if (e.isControlDown()) {
                ContentLabel target = findContainingContentLabel(x, y);
                if (target != null && target.getParent() instanceof AVPairBlock) {
                    AVPairBlock av = (AVPairBlock) target.getParent();
                    av.setModelHidden(true);
                    updateCursorForPoint(x, y);
                }
            }
            else {
                Block target = findContainingBlock(_content, x, y);
                if (target != null) {
                    AVPairListBlock avs = null;
                    if (target instanceof ContentLabel)
                        target = target.getParent();    // attribute or sort label
                    if (target instanceof AVPairBlock)
                        avs = (AVPairListBlock) target.getParent();
                    else if (target instanceof AVMBlock)
                        avs = (AVPairListBlock) ((AVMBlock) target).getContent();
                    else if (target instanceof AVPairListBlock)
                        avs = (AVPairListBlock) target;

                    if (avs != null) {
                        popupMenu(e.getX(), e.getY(), avs);
                    }
                }
            }
        }
        else {
            updateCursorForPoint(x, y);
        }
    }
    
    protected void onMouseMoved(MouseEvent ev) {
        if (ev.getID() != MouseEvent.MOUSE_MOVED) {
            return;
        }
        updateCursorForPoint(unscale(ev.getX()), unscale(ev.getY()));
    }
    
    protected void onMouseDragged(MouseEvent ev) {
        if (!ev.isControlDown())
            return;
        int dx = _lastMousePressedX - ev.getX();
        int dy = _lastMousePressedY - ev.getY();
        if (dx == 0 && dy == 0)
            return;
        Rectangle rect = _scrollPane.getViewport().getViewRect();
        rect.translate(dx, dy);
        _canvas.scrollRectToVisible(rect);
    }
    
    protected void onMouseExited(MouseEvent ev) {
    }
    
    protected void popupMenu(int x, int y, AVPairListBlock avs) {
        JPopupMenu menu = new JPopupMenu("Visible Features");
        for (Block b : avs.getChildren()) {
            final AVPairBlock av = (AVPairBlock) b;
            final JCheckBoxMenuItem item = new JCheckBoxMenuItem(
                    null,
                    !av.isModelHidden());
            item.setAction(new AbstractAction(av.getAttribute().getVisibleText()) {
                public void actionPerformed(ActionEvent e) {
                    av.setModelHidden(!item.isSelected());
                }
            });
            menu.add(item);
        }
        menu.show(_canvas, x, y);
    }

    private static boolean blockContainsPoint(Block block, int x, int y) {
        int X = block.getX();
        int Y = block.getY();
        int W = block.getWidth();
        int H = block.getHeight();

        return (x >= X) && (x < X + W) && (y >= Y) && (y < Y + H);
    }
    
    protected static Block findContainingBlock(Block block, int x, int y) {
        for (Block child : block.getChildren()) {
            if (child.isVisible() && blockContainsPoint(child, x, y)) {
                return child.isLeaf() ? child : findContainingBlock(child, x, y);
            }
        }
        return block;
    }
    
    ContentLabel findContainingContentLabel(int x, int y) {
        if (_lastHit != null
                && blockContainsPoint(_lastHit, x, y)
                && _lastHit.isVisible()
                && !_lastHit.isHiddenByAncestor()
                )
            return _lastHit;
        Block b = findContainingBlock(_content, x, y);
        if (b != null && b instanceof ContentLabel)
            _lastHit = (ContentLabel) b;
        else
            _lastHit = null;
        return _lastHit;
    }
    
    // cursor handling
    private void initCursors() {
        _defaultCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
        _handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
        _currentCursor = _defaultCursor;
    }

    private void updateCursorForPoint(int x, int y) {
        if (_defaultCursor == null)
            initCursors();

        Block target = findContainingContentLabel(x, y);

        if (target == null)
            updateCursor(_defaultCursor);
        else
            updateCursor(_handCursor);
    }

    void updateCursor(Cursor newCursor) {
        if (newCursor == _currentCursor)
            return;
        _canvas.setCursor(newCursor);
        _currentCursor = newCursor;
    }
    
    public void centerBlock(Block b) {
        Rectangle r = _scrollPane.getViewport().getViewRect();
        int x = scale(b.getX());
        int y = scale(b.getY());
        int w = scale(b.getWidth());
        int h = scale(b.getHeight());
        int x1 = x - (int)(r.getWidth()  / 2.0 - w / 2.0);
        int y1 = y - (int)(r.getHeight() / 2.0 - h / 2.0);
        if (x1 < 0)
            x1 = x;
        if (y1 < 0)
            y1 = y;
        r.setLocation(x1, y1);
        _canvas.scrollRectToVisible(r);
    }
    
    private void scrollTo(Block b, int mouseX, int mouseY, MouseEvent e) {
        Rectangle r = _scrollPane.getViewport().getViewRect();
        int relX = mouseX - (int) r.getX();
        int relY = mouseY - (int) r.getY();
        int x = scale(b.getX());
        int y = scale(b.getY());
        r.setLocation(x - relX, y - relY);
        _canvas.scrollRectToVisible(r);
        //updateCursorForPoint(x, y);
        if (!e.isShiftDown() && !isBlockVisible(b))
            centerBlock(b);
    }
    
    private boolean isBlockVisible(Block b) {
        Rectangle r = _scrollPane.getViewport().getViewRect();
        int x = scale(b.getX()),
                y = scale(b.getY()),
                w = scale(b.getWidth()),
                h = scale(b.getHeight());
        return r.contains(x, y, w, h);
    }
}
