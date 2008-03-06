package gralej.blocks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;

public class LabelStyle {
    String name;
    Font font;
    Color textColor;
    Color textAltColor;
    int marginTop, marginRight, marginBottom, marginLeft;
    int frameThickness;
    Color frameColor;
    boolean frameDashed;
    Stroke _stroke;
    
    LabelStyle(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getFrameColor() {
        return frameColor;
    }

    public void setFrameColor(Color frameColor) {
        this.frameColor = frameColor;
    }

    public int getFrameThickness() {
        return frameThickness;
    }

    public void setFrameThickness(int n) {
        if (frameThickness == n)
            return;
        frameThickness = n;
        updateStroke();
    }
    
    public boolean isFrameDashed() {
        return frameDashed;
    }
    
    public void setFrameDashed(boolean b) {
        if (frameDashed == b)
            return;
        frameDashed = b;
        updateStroke();
    }
    
    Stroke getStroke() {
        return _stroke;
    }
    
    private void updateStroke() {
        if (frameThickness <= 0)
            _stroke = null;
        else if (!frameDashed)
            _stroke = new BasicStroke(frameThickness);
        else
            _stroke = new BasicStroke(frameThickness, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_ROUND, 0,  new float[]{2}, 0);
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }
    
    public void setMargins(int top, int right, int bottom, int left) {
        marginTop = top;
        marginRight = right;
        marginBottom = bottom;
        marginLeft = left;
    }

    public Color getTextAltColor() {
        return textAltColor;
    }

    public void setTextAltColor(Color textAltColor) {
        this.textAltColor = textAltColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }
}
