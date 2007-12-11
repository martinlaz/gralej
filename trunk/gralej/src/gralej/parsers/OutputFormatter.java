package gralej.parsers;

import gralej.gui.blocks.BlockPanel;
import gralej.om.ITree;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.RepaintManager;

public class OutputFormatter {
	
	public final static int TRALEFormat = 0;
	public final static int LaTeXFormat = 1;
	public final static int SVGFormat = 2;
	public final static int PostscriptFormat = 3;
	public final static int JPGFormat = 4;
	public final static int XMLFormat = 5;
	
	private File file;
	
	public OutputFormatter(File f) {
		this.file = f;
	}
	
	public void save (IDataPackage data, int format) {
		
		try {
			PrintStream p = new PrintStream(new FileOutputStream(file));


			switch (format) {
			case TRALEFormat:      toTRALE(data, p); break;
			case LaTeXFormat:      toLaTeX(data, p); break;
			case SVGFormat:        toSVG(data, p); break;
			case PostscriptFormat: toPostscript(data, p); break;
			case JPGFormat:        toJPG(data, p); break;
			case XMLFormat:        toXML(data, p); break;
			}

			p.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	
	public void toTRALE (IDataPackage data, PrintStream p) {
		p.print(data.getCharacters());
	}
	
	public void toLaTeX (IDataPackage data, PrintStream p) {
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

		p.print(output);
	}

	public void toSVG (IDataPackage data, PrintStream p) {
		System.err.println("SVG format ain't implemented yet.");
		// TODO implement SVG
	}
	
	public void toPostscript (IDataPackage data, PrintStream p) {
		System.err.println("Postscript format ain't implemented yet.");
		// TODO implement Postscript
	}

	public void toJPG (IDataPackage data, PrintStream p) {
		
		BlockPanel bp = data.createView();
//		JComponent comp = data.createView();
		
        Dimension imgSize = bp.getScaledSize();
//		Dimension componentSize = comp.getPreferredSize();
        BufferedImage img = new BufferedImage(imgSize.width, imgSize.height,
                                            BufferedImage.TYPE_INT_RGB);
        Graphics2D grap = img.createGraphics();
//		grap.fillRect(0,0,img.getWidth(),img.getHeight());
        bp.paint(grap);
        grap.dispose();

        try {
			ImageIO.write(img, "jpg", p);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void toXML (IDataPackage data, PrintStream p) {
		OM2XMLVisitor visitor = new OM2XMLVisitor();
		p.print(visitor.output(data.getModel()));
	}
	
	public void print (IDataPackage data) {
		
			

		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(new DataPrinter(data.createView()));


		if (printJob.printDialog())
			try { 
				printJob.print();
			} catch(PrinterException pe) {
				System.out.println("Error printing: " + pe);
			}



	}

    class DataPrinter implements Printable {
    	
    	BlockPanel view;
    	
    	DataPrinter (BlockPanel view) {
    		this.view = view;
    	}
    	
    	@Override
    	public int print(Graphics g, PageFormat pageFormat, int pageIndex)
    			throws PrinterException {
            if (pageIndex > 0) {
                return(NO_SUCH_PAGE);
              } else {
                Graphics2D g2d = (Graphics2D)g;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                // Turn off double buffering
        		RepaintManager currentManager = 
      			  RepaintManager.currentManager(view);
      			currentManager.setDoubleBufferingEnabled(false);

      			view.paint(g2d);
                // Turn double buffering back on
				currentManager.setDoubleBufferingEnabled(true);

				return(PAGE_EXISTS);
              }

    	}

    	
    }



}
