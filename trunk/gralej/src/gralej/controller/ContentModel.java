package gralej.controller;

import gralej.gui.ContentObserver;
import gralej.gui.ListContentObserver;
import gralej.gui.WindowsContentObserver;
import gralej.parsers.IDataPackage;
import gralej.parsers.OutputFormatter;
import gralej.prefs.GralePreferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
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
        for (int i = 0; i < list.getFocus().length; i++) {
            observer.add(files.get(list.getFocus()[i]));
        }
    }

    /**
     * Close all windows belonging to data items selected
     */
    public void close() {
        if (files.size() == 0)
            return; // don't close nothing
        observer.close();
        int[] selected = list.getFocus();
        for (int i = selected.length - 1; i >= 0; i--) {
            files.remove(selected[i]);
        }
        list.close();
    }

    /**
     * Clear the list, close all windows.
     */
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
        for (int i = 0; i < list.getFocus().length; i++) {
            save(f, files.get(list.getFocus()[i]), null, format);
        }
    }

    /**
     * Method to save all items in the list. Accepts all formats, but only
     * TRALE and XML are treated meaningfully.
     * 
     * @param f:
     *            The file to save to
     * @param format:
     *            TRALEFormat: mindless append
     *            XML: wrapped by <parses>
     */
    public void saveAll(File f, int format) {
        try {
            PrintStream p ;
            if (format == OutputFormatter.XMLFormat) {
            	// for XML, enforce UTF8 encoding
            	p = new PrintStream(new FileOutputStream(f), false, "UTF8");
                p.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            } else {
            	// not XML, use system encoding
            	p = new PrintStream(new FileOutputStream(f));
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
        } catch (UnsupportedEncodingException e) {
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
            PrintStream p;
            
            if (format == OutputFormatter.XMLFormat) {
            	// for XML, enforce UTF8 encoding
            	p = new PrintStream(new FileOutputStream(f), false, "UTF8");
                p.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            } else {
            	// not XML, use system encoding
            	p = new PrintStream(new FileOutputStream(f));
            }
            of.save(p, dataItem, display, format);
            p.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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

    public int[] getFocus() {
        return list.getFocus();
    }

    public void notifyOfServerConnection(boolean isConnected) {
        ((WindowsContentObserver) observer).notifyOfServerConnection(isConnected);
        
    }

}
