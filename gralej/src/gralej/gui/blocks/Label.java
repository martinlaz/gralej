package gralej.gui.blocks;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

class Label extends Block {
    final static int MAX_TEXT_LENGTH = Config.getInt("label.text.maxLength");
    final static String TEXT_CONTINUATION = Config
            .get("label.continuation.text");

    int _frameWidth;
    Color _frameColor;

    String _text;
    String _visibleText;
    int _tw; // text width
    int _th; // text height

    Font _font;
    Color _color;
    int _ascent;

    int _hm; // horizontal margin
    int _vm; // vertical margin

    public Label(String text, Font font, Color color, int margins) {
        this(text, font, color, margins, margins, 0);
    }

    public Label(String text, Font font, Color color, int horizontalMargin,
            int verticalMargin) {
        this(text, font, color, horizontalMargin, verticalMargin, 0);
    }

    public Label(String text, Font font, Color color, int horizontalMargin,
            int verticalMargin, int frameWidth) {
        this(text, font, color, horizontalMargin, verticalMargin, frameWidth,
                Color.BLACK);
    }

    public Label(String text, Font font, Color color, int horizontalMargin,
            int verticalMargin, int frameWidth, Color frameColor) {
        _text = text;
        _visibleText = trimText(text);
        _font = font;
        _color = color;
        _hm = horizontalMargin;
        _vm = verticalMargin;
        setFrame(frameWidth, frameColor);
    }

    @Override
    public void init() {
        updateTextMetrics();
        setVisible(true);
    }

    private static String trimText(String s) {
        if (s.length() > MAX_TEXT_LENGTH)
            s = s.substring(0, MAX_TEXT_LENGTH) + TEXT_CONTINUATION;
        return s;
    }

    public void setFrame(int width) {
        setFrame(width, Color.BLACK);
    }

    public void setFrame(int width, Color color) {
        _frameWidth = width;
        _frameColor = color;
    }

    public String getVisibleText() {
        return _visibleText;
    }

    public String getText() {
        return _text;
    }

    public void setText(String text) {
        if (_text.equals(text))
            return;
        _text = text;
        _visibleText = trimText(text);
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
        updateTextMetrics();
        updateSize();
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
        _tw = fm.stringWidth(_visibleText);
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
            g.draw(frame);
        }

        g.setColor(_color);
        g.setFont(_font);
        x += _hm;
        y += _vm + _ascent;
        g.drawString(_visibleText, x, y);
    }
}
