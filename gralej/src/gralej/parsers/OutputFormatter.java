package gralej.parsers;

import java.util.ArrayList;

public class OutputFormatter {
	
	public final static int TRALEFormat = 0;
	public final static int LaTeXFormat = 1;
	public final static int SVGFormat = 2;
	public final static int PostscriptFormat = 3;
	public final static int TiffFormat = 4;
	public final static int XMLFormat = 5;
	
	private ArrayList<String> formats;
		
	public OutputFormatter() {

	}

	
	public boolean supports (String format) {
		if (formats.contains(format)) return true;
		else return false;
	}
	
	// TRALE format
	public String toTRALE (IDataPackage data) {
		return new String (data.getCharacters());
	}
	
	public String toLaTeX (IDataPackage data) {
		OM2LaTeXVisitor visitor = new OM2LaTeXVisitor();
		return "% AVM output by GraleJ\n"
			+"\\documentclass{article}\n"
			+"\\usepackage{avm+}\n"
			+"\\avmoptions{center}\n"
			+"\\begin{document}\n"
			+"\\begin{Avm}{"
			+data.getTitle()
			+"}\n"
			+visitor.output(data.getModel())
			+"\\end{Avm}\n"
			+"\\end{document}";
	}

	public String toSVG (IDataPackage data) {
		System.err.println("SVG format ain't implemented yet. Returning an empty file.");
		return ""; // TODO implement SVG
	}
	
	public String toPostscript (IDataPackage data) {
		System.err.println("Postscript format ain't implemented yet. Returning an empty file.");
		return ""; // TODO implement Postscript
	}

	public String toTiff (IDataPackage data) {
		System.err.println("Tiff format ain't implemented yet. Returning an empty file.");
		return ""; // TODO implement Tiff
	}

	public String toXML (IDataPackage data) {
		OM2XMLVisitor visitor = new OM2XMLVisitor();
		return visitor.output(data.getModel());
	}

	public String convertToString(IDataPackage data, int format) {
		switch (format) {
		case TRALEFormat: return toTRALE(data); 
		case LaTeXFormat: return toLaTeX(data);
		case SVGFormat: return toSVG(data);
		case PostscriptFormat: return toPostscript(data);
		case TiffFormat: return toTiff(data);
		case XMLFormat: return toXML(data);
		}
		// TODO Auto-generated method stub
		return null;
	}




}
