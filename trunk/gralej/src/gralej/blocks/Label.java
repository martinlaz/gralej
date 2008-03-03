package gralej.blocks;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Label extends Block {
    LabelStyle _style;
    
    String _text;
    String _visibleText;
    
    // text metrics
    int _tw; // width
    int _th; // height
    int _ascent;
    
    protected boolean _useTextAltColor;
    
    Label(BlockPanel panel, LabelStyle style, String text) {
        _style = style;
        setPanel(panel);
        setText(text);
    }
    
    public LabelStyle getStyle() {
        return _style;
    }
    
    public void setText(String text) {
        _text = text;
        _visibleText = trimText(_text);
        updateSelf();
    }
    
    public String getVisibleText() {
        return _visibleText;
    }

    public String getText() {
        return _text;
    }
    
    private String trimText(String s) {
        int max = getPanelStyle().getMaxLabelTextLength();
        if (s.length() > max)
            s = s.substring(0, max) + getPanelStyle().getLongLabelTextContinuation();
        return s;
    }
    
    @Override
    protected void updateSelf() {
        updateTextMetrics();
        int w = _tw + _style.getMarginLeft() + _style.getMarginRight();
        int h = _th + _style.getMarginTop() + _style.getMarginBottom();
        setSize(w, h);
    }
    
    private void updateTextMetrics() {
        FontMetrics fm = getPanel().getCanvas().getFontMetrics(_style.getFont());
        _tw = fm.stringWidth(_visibleText);
        _th = fm.getHeight();
        _ascent = fm.getAscent();
    }
    
    @Override
    public void paint(Graphics2D g) {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();

        if (_style.getFrameWidth() > 0) {
            // draw frame
            Rectangle frame = new Rectangle(x, y, w, h);
            g.setColor(_style.getFrameColor());
            g.draw(frame);
        }
        
        Color color = _useTextAltColor ? _style.getTextAltColor() : _style.getTextColor();

        g.setColor(color);
        g.setFont(_style.getFont());
        x += _style.getMarginLeft();
        y += _style.getMarginTop() + _ascent;
        g.drawString(_visibleText, x, y);
    }
}
