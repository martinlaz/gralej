package gralej.gui.blocks;

abstract class Block implements IBlock {
    private boolean _visible;
    private int _w = -1, _h = -1;
    private int _x, _y;
    private IBlock _parent;
    
    public Block(IBlock parent) { _parent = parent; }
    
    public BlockPanel getPanel() {
        return getParentBlock().getPanel();
    }

    // visibility
    public boolean isVisible() { return _visible; }
    public void setVisible(boolean visible) {
        if (_visible == visible)
            return;
        _visible = visible;
        if (_w == -1)
            updateSize();
        getParentBlock().updateSize();
    }
    
    // size
    public int getWidth() {
        if (!_visible)
            return 0;
        return _w;
    }
    
    public int getHeight() {
        if (!_visible)
            return 0;
        return _h;
    }
    
    public void setSize(int w, int h) {
        if (_w == w && _h == h) {
            layoutChildren();
            return;
        }
        _w = w;
        _h = h;
        getParentBlock().updateSize();
    }
    // abstract protected void updateSize();
    
    // position
    public int getX() { return _x; }
    public int getY() { return _y; }
    public void setPosition(int x, int y) {
        _x = x;
        _y = y;
    }
    
    protected void layoutChildren() {}
    
    // hierarchy
    public IBlock getParentBlock() { return _parent; }
    public Iterable<IBlock> getChildren()
        { return java.util.Collections.emptyList(); }
    public boolean isLeaf() { return true; }
    
    // abstract void paint(Graphics2D g);
}
