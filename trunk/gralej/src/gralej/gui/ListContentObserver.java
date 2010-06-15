package gralej.gui;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gralej.controller.*;
import gralej.parsers.IDataPackage;
import java.awt.Cursor;

/**
 * The ListContentObserver displays the file list,
 * and handles its events.
 * 
 * @author Armin
 * @version $Id$
 * 
 */
public class ListContentObserver extends ContentObserver {

    //private static Logger logger = Logger.getAnonymousLogger();
    
    private DefaultListModel listmodel;
    private JList list;
    private MainGUI gui;

    /**
     * @param m: the content model
     */
    public ListContentObserver(ContentModel m, MainGUI gui) {
        super(m);
        m.setLCO(this);
        this.gui = gui;
        listmodel = new DefaultListModel();
        list = new JList(listmodel) {
            // tooltip with name
            public String getToolTipText(MouseEvent e) {
                if (!getScrollableTracksViewportWidth()) {
                    int p = locationToIndex(e.getPoint());
                    if (p != -1)
                        return getModel().getElementAt(p).toString();
                }
                return null;
            }
        };

        // open new window instance on double-click
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) {
                    open();
                }
            }
        });

        // open new window instance on ENTER
        list.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent arg0) {
                switch (arg0.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        open();
                        break;
                    case KeyEvent.VK_DELETE:
                    case KeyEvent.VK_BACK_SPACE:
                        model.deleteSelected();
                        break;
                    case KeyEvent.VK_D:
                        if (arg0.isControlDown()) {
                            model.doDiff();
                        }
                        break;
                }
            }

            public void keyReleased(KeyEvent arg0) {}

            public void keyTyped(KeyEvent arg0) {}
        });

        display = new JScrollPane(list);
        display.setBorder(BorderFactory.createEmptyBorder());

        // Monitor all list selections
        list.addListSelectionListener(new ListUpdater());
        

        display.setVisible(true);

    }
    
    private void open() {
        list.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            model.open();
        }
        finally {
            list.setCursor(Cursor.getDefaultCursor());
        }
    }

    @Override
    public void add(IDataPackage data) {
        listmodel.addElement(data.getTitle());
        gui.notifyOfListElements(listmodel.size());
    }

    @Override
    public void close() {
        for (int i = list.getSelectedIndices().length - 1; i >= 0; i--) {
            listmodel.remove(list.getSelectedIndices()[i]);
        }
        gui.notifyOfSelection(list.getSelectedIndex() != -1);
        gui.notifyOfListElements(listmodel.size());
    }

    public void clear() {
        listmodel.clear();
        gui.notifyOfSelection(false);
        gui.notifyOfListElements(0);
    }

    public int[] getFocus() {
        return list.getSelectedIndices();
    }

    /**
     * Inner class that responds to selection
     */ 
    class ListUpdater implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            gui.notifyOfSelection(list.getSelectedIndex() != -1);
        }
    }
}
