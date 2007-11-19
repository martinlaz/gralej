package gralej.controller;

/**
 * Class holding meta information about some
 * input stream
 * @author Niels
 * @version $Id$
 */
public class StreamInfo {
	
	private String type;
	private String name;

	public StreamInfo(String type, String name) {
		this.type = new String(type);
		this.name = new String(name);
	}
	
	public StreamInfo(String type) {
		this(type, "");
	}

	/**
	 * Attenzione! this may return null.
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = new String(name);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = new String(type);
	}
	
	

}
