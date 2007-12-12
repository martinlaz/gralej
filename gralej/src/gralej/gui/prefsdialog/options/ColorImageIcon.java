package gralej.gui.prefsdialog.options;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class ColorImageIcon extends ImageIcon {

    /**
     * 
     */
    private static final long serialVersionUID = 4645654882803725534L;
    private Color color;
    private Dimension size;

    public ColorImageIcon(Color color, Dimension size) {
        super();
        this.size = size;
        setColor(color);

    }

    private void createImage() {
        // create an image
        BufferedImage image = new BufferedImage(size.width, size.height,
                BufferedImage.TYPE_INT_ARGB);

        // drawing object
        Graphics2D g2D = image.createGraphics();

        // draw black frame
        g2D.setColor(Color.BLACK);
        g2D.drawRect(0, 0, size.width - 1, size.height - 1);

        // fill the image with a rectangle
        g2D.setColor(color);
        g2D.fillRect(1, 1, size.width - 2, size.height - 2);

        // insert into this icon
        setImage(image);

    }

    private Color cloneColor(Color c) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
    }

    public void setColor(Color color) {
        this.color = cloneColor(color);
        createImage();
    }

    public Color getColor() {
        return cloneColor(color);
    }

}
