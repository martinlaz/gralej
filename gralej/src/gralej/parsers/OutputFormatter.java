package gralej.parsers;

import gralej.om.ITree;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class OutputFormatter {
	
	public final static int TRALEFormat = 0;
	public final static int LaTeXFormat = 1;
	public final static int SVGFormat = 2;
	public final static int PostscriptFormat = 3;
	public final static int JPGFormat = 4;
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
		String output = "% AVM output by GraleJ\n"
			+"\\documentclass{article}\n"
			+"\\usepackage{avm+}\n"
			+"\\usepackage{ecltree+}\n"
			+"\\avmoptions{center}\n"
			+"\\begin{document}\n";
		if (data.getModel() instanceof ITree) {
			output += visitor.output(data.getModel());
		} else {
			output += "\\begin{Avm}{"
			+data.getTitle()
			+"}\n"
			+visitor.output(data.getModel())
			+"\\end{Avm}\n";
		}
		output += "\\end{document}";
		return output;
	}

	public String toSVG (IDataPackage data) {
		System.err.println("SVG format ain't implemented yet. Returning an empty file.");
		return null; // TODO implement SVG
	}
	
	public String toPostscript (IDataPackage data) {
		System.err.println("Postscript format ain't implemented yet. Returning an empty file.");
		return null; // TODO implement Postscript
	}

	public String toJPG (IDataPackage data) {
		
		JComponent comp = data.createView();
//		comp.setDoubleBuffered(false);
		
		Dimension componentSize = comp.getPreferredSize();
//		comp.setSize(componentSize); 
		System.err.println("size "+componentSize.width+" "+
                  componentSize.height);
		BufferedImage img = new BufferedImage(componentSize.width,
	                                            componentSize.height,
	                                            BufferedImage.TYPE_INT_RGB);
		Graphics2D grap = img.createGraphics();
		grap.fillRect(0,0,img.getWidth(),img.getHeight());
		comp.paint(grap);
		//grap.dispose();
		
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(img,"jpg",baos);
			baos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String output = baos.toString();
//		System.err.println(output);
		return output;
		        			

//		System.err.println("JPG format ain't implemented yet. Returning an empty file.");
//		return ""; // TODO implement JPG
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
		case JPGFormat: return toJPG(data);
		case XMLFormat: return toXML(data);
		}
		// TODO Auto-generated method stub
		return null;
	}




}
