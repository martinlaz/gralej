package gralej.gui.prefsdialog;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * A cell renderer that allows {@link javax.swing.JList} to dislay
 * icons. This is mostly a copy from 
 * {@link http://java.sun.com/docs/books/tutorial/uiswing/components/combobox.html#renderer}
 * @author no
 * @version $Id$
 */
class ImageListCellRenderer extends JLabel implements ListCellRenderer {
	
	private static final int spacing = 10;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1876262123513787955L;
	ImageIcon[] images;
	String[] labels;
	
	public ImageListCellRenderer(ImageIcon[] images, String[] labels) {
		this.images = images;
		this.labels = labels;

	
		setOpaque(true);
		setBorder(BorderFactory.createEmptyBorder(spacing, spacing, spacing, spacing));
		setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		setHorizontalTextPosition(JLabel.CENTER);
		
	}

	/*
	 * This method finds the image and text corresponding
	 * to the selected value and returns the label, set up
	 * to display the text and image.
	 */
	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {
//		Get the selected index. (The index param isn't
//		always valid, so just use the value.)
		//System.err.println(value);
		//int selectedIndex = ((JList)value).getSelectedIndex();
		int selectedIndex = Integer.parseInt(value.toString());

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

//		Set the icon and text.  If icon was null, say so.
		ImageIcon icon = images[selectedIndex];
		String text = labels[selectedIndex];
		setIcon(icon);
		setText(text);
		setFont(list.getFont());
		

		return this;
	}
}