/*
 *  $Id$
 *
 *  Author:
 *     Martin Lazarov [mlazarov at sfs.uni-tuebingen.de]
 *     
 *  This file is part of the Gralej system
 *     http://code.google.com/p/gralej/
 *
 *  Gralej is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gralej is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package gralej.blocks;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

public class Label extends Block {
    LabelStyle _style;
    
    String _text;
    String _visibleText;
    
    // text metrics
    int _tw; // width
    int _th; // height
    int _ascent;
    
    protected boolean _useTextAltColor;
    
    // diff
    boolean _different;
    boolean _struckOut;
    
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
        _style = getPanelStyle().getLabelFactory().getLabelStyle(_style.getName());
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

    public boolean isDifferent() {
        return _different;
    }

    public void setDifferent(boolean different) {
        _different = different;
    }

    public boolean isStruckOut() {
        return _struckOut;
    }

    public void setStruckOut(boolean strikeThrough) {
        _struckOut = strikeThrough;
    }
    
    @Override
    public void paint(Graphics2D g) {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();

        if (_style.getFrameThickness() > 0) {
            // draw frame
            Stroke oldStroke = g.getStroke();
            g.setStroke(_style.getStroke());
            Rectangle frame = new Rectangle(x, y, w, h);
            g.setColor(_style.getFrameColor());
            g.draw(frame);
            g.setStroke(oldStroke);
        }
        
        Color color;
        if (_different)
            color = getPanelStyle().getDifferentTextColor();
        else if (_useTextAltColor)
            color =_style.getTextAltColor();
        else
            color = _style.getTextColor();

        g.setColor(color);
        g.setFont(_style.getFont());
        x += _style.getMarginLeft();
        y += _style.getMarginTop() + _ascent;
        g.drawString(_visibleText, x, y);
        
        if (_struckOut) {
            g.setColor(getPanelStyle().getStrikethroughLineColor());
            y = getY();
            g.drawLine(
                    x,
                    y + h / 2,
                    x + _tw,
                    y + h / 2);
        }
    }
    
    public String toString() {
        return "[" + getClass().toString() + "@" + hashCode() + ":" + _text + "]";
    }
}
