package gralej.controller;

import gralej.gui.*;
import gralej.parsers.*;
import gralej.prefs.GralePreferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * The content model stores the semantics. it keeps track of open files, which
 * one's focused etc.
 * 
 * @author Armin
 * 
 */
public class ContentModel {

    private ListContentObserver list;
    private ContentObserver observer;
    private ArrayList<IDataPackage> files = new ArrayList<IDataPackage>();
    private OutputFormatter of;

    public OutputFormatter getOutputFormatter() {
        return of;
    }

    public void open(IDataPackage parse) {
        files.add(parse);
        list.add(parse);
        
        GralePreferences gp = GralePreferences.getInstance();
        if (gp.getBoolean("behavior.openonload")) {
            observer.add(parse);
        }
    }

    /**
     * Opens a new window for the selected data item.
     * 
     */
    public void open() {
        observer.add(files.get(list.getFocus()));
    }

    public void close() {
        if (files.size() == 0)
            return; // don't close nothing
        observer.close();
        files.remove(list.getFocus());
        list.close();
    }

    public void closeAll() {
        files.clear();
        list.clear();
        observer.clear();
    }

    /**
     * Minimal saving method taking only a file and a format
     * argument. 
     * 
     * @param f
     * @param format
     */
    public void save(File f, int format) {
        save(f, files.get(list.getFocus()), null, format);
    }

    /**
     * Method to save all items in the list.
     * 
     * @param f:
     *            The file to save to
     * @param format:
     *            only TRALEFormat is supported for saving into a single file.
     *            However this method simply appends all AVMs regardless of
     *            whether the format is meant to understand that.
     */
    public void saveAll(File f, int format) {
        try {
            PrintStream p = new PrintStream(new FileOutputStream(f));
            if (format == OutputFormatter.XMLFormat) {
                p.print("<parses>");
            }
            for (int i = 0; i < files.size(); i++) {
                of.save(p, files.get(i), null, format);
            }
            if (format == OutputFormatter.XMLFormat) {
                p.print("</parses>");
            }
            p.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Full saving method with all parameters. These are directly passed to
     * the OutputFormatter's saving method.
     * 
     * @param f
     * @param dataItem
     * @param display
     * @param format
     */
    public void save(File f, IDataPackage dataItem, JComponent display, int format) {
        try {
            PrintStream p = new PrintStream(new FileOutputStream(f));
            of.save(p, dataItem, display, format);
            p.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void print(JComponent view) {
        of.print(view);
    }

    public void tile() {
        // TODO to be changed when other observers are allowed
        ((WindowsContentObserver) observer).tile();
    }

    public void cascade() {
        ((WindowsContentObserver) observer).cascade();
    }

    public ContentModel() {
        of = OutputFormatter.getInstance();
    }

    public void setLCO(ListContentObserver l) {
        if (list != null)
            return; // instead: finalizing?
        list = l;
    }

    public void setObserver(ContentObserver o) {
        if (observer != null)
            return;
        observer = o;
    }

    public int getSize() {
        return files.size();
    }

    public ArrayList<IDataPackage> getFiles() {
        return files;
    }

    public IDataPackage getData(int i) {
        return files.get(i);
    }

    public int getFocus() {
        return list.getFocus();
    }

    public void notifyOfServerConnection(boolean isConnected) {
        ((WindowsContentObserver) observer).notifyOfServerConnection(isConnected);
        
    }

}
