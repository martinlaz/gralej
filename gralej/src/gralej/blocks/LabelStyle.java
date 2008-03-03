package gralej.blocks;

import java.awt.Color;
import java.awt.Font;

public class LabelStyle {
    String name;
    Font font;
    Color textColor;
    Color textAltColor;
    int marginTop, marginRight, marginBottom, marginLeft;
    int frameWidth;
    Color frameColor;
    
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

    public int getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
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
