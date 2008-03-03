package gralej.blocks;

public class ContentLabel extends Label {
    ContentLabel(BlockPanel panel, LabelStyle style, String text) {
        super(panel, style, text);
    }
    
    public void flipContentVisibility() {
        Block content = ((ContentOwner) getParent()).getContent();
        if (content == null)
            return;

        boolean visible = !content.isVisible();
        _useTextAltColor = !visible;
        content.setVisible(visible);

        getPanel().getCanvas().repaint();
    }

    public void flip() {
        flipContentVisibility();
    }
}
