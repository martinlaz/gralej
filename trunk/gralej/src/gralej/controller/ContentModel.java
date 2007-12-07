package gralej.controller;

import gralej.gui.*;
import gralej.parsers.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.prefs.*;


/**
 * The content model stores the semantics. 
 * it keeps track of open files, which one's focused etc.
 * 
 * @author Armin
 *
 */
public class ContentModel {
	
	private ListContentObserver list;
	private ContentObserver observer;
	private ArrayList<IDataPackage> files = new ArrayList<IDataPackage>(); 
    
    // TODO delete old preference handling once Niels' new one is in
    Preferences prefs;
    
    public Preferences getPrefs () { return prefs; }

    public void resetPreferences () {
		prefs.put("color", "white");
		prefs.put("font size", "11");
	}
	
	public void open (IDataPackage parse) {
		files.add(parse);
		list.add(parse);
		observer.add(parse);
	}
	
	/**
	 * Opening a new window for the selected data item.
	 * 
	 */
	public void open () {
//		observer.add(files.get(list.getFocus()).createView(), 
//				files.get(list.getFocus()).getTitle());
		observer.add(files.get(list.getFocus()));
	}

	public void close () {
		if (files.size() == 0) return; // don't close nothing
		observer.close();
		files.remove(list.getFocus());
		list.close();
	}
	
	public void closeAll () {
		files.clear();
		list.clear();
		observer.clear();
	}
	
	public void save (File f, int format) {
		save(f, files.get(list.getFocus()), format);
	}
	
	
	/**
	 * 
	 * 
	 * @param f: The file to save to
	 * @param format: only TRALEFormat is supported for saving into
	 * 		a single file. However this method simply appends all AVMs
	 * 		regardless of whether the format is meant to understand that.
	 */
	public void saveAll (File f, int format) {
		try {
			OutputFormatter of = new OutputFormatter();
			PrintStream p = new PrintStream(new FileOutputStream(f));
			for (int i = 0; i < files.size(); i++) {
				String output = of.convertToString(files.get(i), format);
				p.print(output);
			}
			p.close();
		} catch (IOException e) {
			System.err.println("Saving failed.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void save (File f, IDataPackage dataItem, int format) {
		System.err.println("Attempting to save data item "+dataItem.getTitle()
				+" to file "+f.getName());
		try {
			OutputFormatter of = new OutputFormatter();
			PrintStream p = new PrintStream(new FileOutputStream(f));
			String output = of.convertToString(dataItem, format);
			p.print(output);
			p.close();
		} catch (IOException e) {
			System.err.println("Saving failed.");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// TODO finally, generalize this to multi-format exporting
	}
	
	public void tile () {
		// TODO to be changed when other observers are allowed
		((WindowsContentObserver) observer).tile();
	}

	public void cascade () {
		((WindowsContentObserver) observer).cascade();
	}

	public ContentModel() {
		
		prefs = Preferences.userNodeForPackage(this.getClass());
		resetPreferences();
	}
	
	public void setLCO (ListContentObserver l) {
		if (list != null) return; // instead: finalizing?
		list = l;
	}
	
	public void setObserver (ContentObserver o) {
		if (observer != null) return;
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
