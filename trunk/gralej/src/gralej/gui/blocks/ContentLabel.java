package gralej.gui.blocks;

import java.awt.Color;
import java.awt.Font;

class ContentLabel extends Label {
    public ContentLabel(
        ContentOwningBlock  parent,
        String  text,
        Font    font,
        Color   textColor,
        int     hm,
        int     vm,
        int     frameWidth,
        Color   frameColor
        )
    {
        super(parent, text, font, textColor, hm, vm, frameWidth, frameColor);
        getPanel().addContentLabel(this);
    }
    
    void flipContentVisibility() {
        IBlock content = ((IContentOwner) getParentBlock()).getContent();
        content.setVisible(!content.isVisible());
        getPanel().repaint();
    }
}
