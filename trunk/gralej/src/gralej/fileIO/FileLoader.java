package gralej.fileIO;

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
		String type;
		
		public BackgroundFileLoader(InputStream is, String type) {
			super();
			this.setName("BackgroundFileLoader ("
					+ file.getAbsolutePath() + ")");
			this.is = is;
			this.type = type;
		}
		
		public void run() {
			notifyListeners(is, type);
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
		
		String type = extension2type(file);
		
		// open
		FileInputStream is = new FileInputStream(file);
		
		// either call handlers or go into thread to do so
		if ( ! threaded) {
			notifyListeners(is, type);
		} else {
			// anonymous thread class
			new BackgroundFileLoader(is,type).start();
		}
		
	}
	
	
	

}
