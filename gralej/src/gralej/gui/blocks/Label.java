package gralej.gui.blocks;

import java.awt.*;

public class Label extends Block {
    int     _frameWidth;
    Color   _frameColor;
    
    String  _text;
    int     _tw;    // text width
    int     _th;    // text height
    
    Font    _font;
    Color   _color;
    int     _ascent;
    
    int     _hm;    // horizontal margin
    int     _vm;    // vertical margin
    
    public Label(IBlock parent) {
        super(parent);
    }
    
    public Label(
        IBlock  parent,
        String  text,
        Font    font,
        Color   color,
        int     margins
        )
    {
        this(parent, text, font, color,
            margins, margins, 0);
    }
    
    public Label(
        IBlock  parent,
        String  text,
        Font    font,
        Color   color,
        int     horizontalMargin,
        int     verticalMargin
        )
    {
        this(parent, text, font, color,
            horizontalMargin, verticalMargin, 0);
    }
    
    public Label(
        IBlock  parent,
        String  text,
        Font    font,
        Color   color,
        int     horizontalMargin,
        int     verticalMargin,
        int     frameWidth
        )
    {
        this(parent, text, font, color,
            horizontalMargin, verticalMargin, frameWidth, Color.BLACK);
    }
    
    public Label(
        IBlock  parent,
        String  text,
        Font    font,
        Color   color,
        int     horizontalMargin,
        int     verticalMargin,
        int     frameWidth,
        Color   frameColor
        )
    {
        super(parent);
        _text = text;
        _font = font;
        _color = color;
        _hm = horizontalMargin;
        _vm = verticalMargin;
        setFrame(frameWidth);
        updateTextMetrics();
    }
    
    public void setFrame(int width) {
        setFrame(width, Color.BLACK);
    }
    
    public void setFrame(int width, Color color) {
        _frameWidth = width;
        _frameColor = color;
    }
    
    public String getText() {
        return _text;
    }
    
    public void setText(String text) {
        if (_text.equals(text))
            return;
        _text = text;
        updateTextMetrics();
        updateSize();
    }
    
    public void setMargins(int m) {
        setMargins(m, m);
    }
    
    public void setMargins(int hm, int vm) {
        if (hm < 0 || vm < 0)
            throw new IllegalArgumentException(
                "a margin must be a non-negative int");
        if (_hm == hm && _vm == vm)
            return;
        _hm = hm;
        _vm = vm;
        updateSize();
    }
    
    public void setFont(Font font) {
        _font = font;
    }
    
    public void setColor(Color color) {
        _color = color;
    }
    
    public void updateSize() {
        int w = _tw + 2 * _hm;
        int h = _th + 2 * _vm;
        setSize(w, h);
        getParentBlock().updateSize();
    }
    
    private void updateTextMetrics() {
        FontMetrics fm = getPanel().getFontMetrics(_font);
        _tw = fm.stringWidth(_text);
        _th = fm.getHeight();
        _ascent = fm.getAscent();
    }
    
    public void paint(Graphics2D g) {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();
        
        if (_frameWidth > 0) {
            // draw frame
            Rectangle frame = new Rectangle(x, y, w, h);
            g.setColor(_frameColor);
            g.fill(frame);
            g.clearRect(
                    x + _frameWidth,
                    y + _frameWidth,
                    w - 2 * _frameWidth,
                    h - 2 * _frameWidth
                    );
        }
        g.setColor(_color);
        g.setFont(_font);
        x += _hm;
        y += _vm + _ascent;
        //System.err.println("x: " + x + "; y: " + y);
        g.drawString(_text, x, y);
    }
}
