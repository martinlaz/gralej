package gralej.fileIO;

import gralej.controller.StreamInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A file loader service class that
 * takes care of loading a file in a seperated
 * thread.
 * @author Niels
 * @version $Id$
  */
public class FileLoader extends FileLoaderBaseImpl {
	
	private File file;
	private boolean threaded;
	
	/**
	 * Seperate thread for calling the stream handler
	 */
	private class BackgroundFileLoader extends Thread {
		
		InputStream is;
		StreamInfo meta;
		
		public BackgroundFileLoader(InputStream is, StreamInfo meta) {
			super();
			this.setName("BackgroundFileLoader ("
					+ file.getAbsolutePath() + ")");
			this.is = is;
			this.meta = meta;
		}
		
		public void run() {
			notifyListeners(is, meta);
		}

	}
	

	/**
	 * @param file the file to load
	 * @param threaded if set to true, file loading happens as background
	 * action (seperated thread).
	 */
	public FileLoader(File file, boolean threaded) {
		super();
		this.file = file;
		this.threaded = threaded;
	}
	
	/**
	 * load file (without background process/threading)
	 * @param file the file to load
	 */
	public FileLoader(File file) {
		this(file, false);
	}
	
	/**
	 * Mapping function that returns a protocol/data type from
	 * the file extension
	 * @param f the file
	 * @return
	 */
	private String extension2type(File f) {

		String lcfilename = file.getName().toLowerCase();
		
		if ( lcfilename.endsWith(".grale")) {
			return "grisu";
		}
		
		// file type unknown
		return "unknown";
		
	}
	
	public void loadFile() throws FileNotFoundException {
		
		StreamInfo info = new StreamInfo(extension2type(file),
				file.getName());
		
		// open
		FileInputStream is = new FileInputStream(file);
		
		// either call handlers or go into thread to do so
		if ( ! threaded) {
			notifyListeners(is,info);
		} else {
			// anonymous thread class
			new BackgroundFileLoader(is,info).start();
		}
		
	}
	
	
	

}
