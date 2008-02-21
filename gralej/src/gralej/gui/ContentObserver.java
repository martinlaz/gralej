package gralej.gui;

import javax.swing.*;
import gralej.controller.*;
import gralej.parsers.IDataPackage;

/**
 * Superclass for observers.
 * 
 * @author Armin
 * @version $Id$
 */
public abstract class ContentObserver {

    protected ContentModel model;

    protected JComponent display;

    public JComponent getDisplay() {
        return display;
    }

    public abstract void add(IDataPackage data);

    public abstract void close();

    public abstract void clear();

    public ContentObserver(ContentModel m) {
        model = m;
    }

}
