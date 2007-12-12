package gralej.controller;

import gralej.gui.*;
import gralej.gui.blocks.BlockPanel;
import gralej.parsers.*;

import java.io.File;
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
        observer.add(parse);
    }

    /**
     * Opening a new window for the selected data item.
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

    public void save(File f, int format) {
        save(f, files.get(list.getFocus()), format);
    }

    /**
     * 
     * 
     * @param f:
     *            The file to save to
     * @param format:
     *            only TRALEFormat is supported for saving into a single file.
     *            However this method simply appends all AVMs regardless of
     *            whether the format is meant to understand that.
     */
    public void saveAll(File f, int format) {
        for (int i = 0; i < files.size(); i++) {
            of.save(f, files.get(i), format);
        }

    }

    public void save(File f, IDataPackage dataItem, int format) {
        of.save(f, dataItem, format);
    }

    public void save(File f, BlockPanel view, int format) {
        of.save(f, view, format);
    }

    public void print() {
        // print(files.get(list.getFocus()));
        // TODO access the windowscontentmodel to find the according view...
        // impossible
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

}
