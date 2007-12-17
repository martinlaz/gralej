package gralej.parsers;

import gralej.gui.blocks.BlockPanel;
import gralej.om.ITree;
import gralej.prefs.GralePreferences;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.SimpleDoc;
import javax.print.StreamPrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import sun.print.PSStreamPrinterFactory;

/**
 * 
 * 
 * 
 * @author Armin
 *
 */
public class OutputFormatter {

    public final static int TRALEFormat = 0;
    public final static int LaTeXFormat = 1;
    public final static int SVGFormat = 2;
    public final static int PostscriptFormat = 3;
    public final static int JPGFormat = 4;
    public final static int XMLFormat = 5;
    public final static int PNGFormat = 6;


    private static OutputFormatter instance = null;

    private OutputFormatter() {}

    public static OutputFormatter getInstance() {
        if (instance == null) 
            instance = new OutputFormatter();
        return instance;
    }

    public FileFilter getFilter(int format) {
        return new FormatFilter(format);
    }

    class FormatFilter extends javax.swing.filechooser.FileFilter {

        private String extension;
        private String description;

        FormatFilter(int format) {
            switch (format) {
            case TRALEFormat:
                extension = "grale";
                description = ".grale files";
                break;
            case LaTeXFormat:
                extension = "tex";
                description = ".tex files";
                break;
            case SVGFormat:
                extension = "svg";
                description = ".svg files";
                break;
            case PostscriptFormat:
                extension = "ps";
                description = ".ps files";
                break;
            case JPGFormat:
                extension = "jpg";
                description = ".jpg files";
                break;
            case PNGFormat:
                extension = "png";
                description = ".png files";
                break;
            case XMLFormat:
                extension = "xml";
                description = ".xml files";
                break;
            }
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) return true;
            return f.getName().toLowerCase().endsWith(extension);
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

    public void save(PrintStream p, IDataPackage data, JComponent view, int format) {

        switch (format) {
        case TRALEFormat:
            if (data != null) toTRALE(data, p);
            else System.err.println("Bad function call (no data).");
            break;
        case LaTeXFormat: 
            if (data != null) toLaTeX(data, p);
            else System.err.println("Bad function call (no data).");
            break;
        case SVGFormat:
            if (view != null) toSVG(view, p);
            else System.err.println(
                    "Bad function call (SVG rendering needs a Swing JComponent as input).");
            break;
        case PostscriptFormat:
            if (view != null) toPostscript(view, p);
            else System.err.println(
                    "Bad function call (postscript rendering needs a Swing JComponent as input).");
            break;
        case JPGFormat:
            if (view != null) {
                try {
                    toPixelGraphic(view, p, "jpg");
                } catch (OutOfMemoryError e) {
                    System.err.println("The image is too large to be rendered.");
                    // e.printStackTrace();
                }
            }
            else System.err.println(
            "Bad function call (image rendering needs a Swing JComponent as input).");
            break;
        case PNGFormat:
            if (view != null) toPixelGraphic(view, p, "png");
            else System.err.println(
            "Bad function call (image rendering needs a Swing JComponent as input).");
            break;
        case XMLFormat:
            if (data != null) toXML(data, p);
            else System.err.println("Bad function call (no data).");
            break;
        }


    }

    private void toTRALE(IDataPackage data, PrintStream p) {
        p.print(data.getCharacters());
    }

    private void toLaTeX(IDataPackage data, PrintStream p) {
        GralePreferences gp = GralePreferences.getInstance();
        if (gp.getBoolean("output.latex.snippet")) {
            toLaTeXSnippet (data, p);
        } else {
            toLaTeXFile(data, p);
        }
    }

    private void toLaTeXFile(IDataPackage data, PrintStream p) {
        OM2LaTeXVisitor visitor = new OM2LaTeXVisitor();
        String output = "% AVM output by GraleJ\n"
                + "\\documentclass{article}\n" + "\\usepackage{avm+}\n"
                + "\\usepackage{ecltree+}\n" + "\\avmoptions{center}\n"
                + "\\begin{document}\n";
        if (data.getModel() instanceof ITree) {
            output += visitor.output(data.getModel());
        } else {
            output += "\\begin{Avm}{" + data.getTitle() + "}\n"
                    + visitor.output(data.getModel()) + "\\end{Avm}\n";
        }
        output += "\\end{document}";

        p.print(output);
    }

    /**
     * Writes an AVM in LaTeX format to a file, to be used via \include. Thus it
     * relies on correct embedding in a document, which also has to load the
     * avm+ and ecltree+ packages.
     * 
     * @param data
     * @param p
     */
    private void toLaTeXSnippet(IDataPackage data, PrintStream p) {
        OM2LaTeXVisitor visitor = new OM2LaTeXVisitor();
        String output = "% AVM output by GraleJ\n";
        if (data.getModel() instanceof ITree) {
            output += visitor.output(data.getModel());
        } else {
            output += "\\begin{Avm}{" + data.getTitle() + "}\n"
                    + visitor.output(data.getModel()) + "\\end{Avm}\n";
        }
        p.print(output);
    }
    

    private void toSVG(JComponent bp, PrintStream p) {
        
        DOMImplementation domImpl = null;
        try {
            domImpl = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        // Create an instance of org.w3c.dom.Document.
        Document document = domImpl.createDocument(
                "http://www.w3.org/2000/svg", "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        boolean db = ((BlockPanel) bp).getDrawingPane().isDoubleBuffered();
        ((BlockPanel) bp).getDrawingPane().setDoubleBuffered(false);
        ((BlockPanel) bp).getDrawingPane().paint(svgGenerator);
        
        try {
            Writer out = new OutputStreamWriter(p, "UTF-8");
            boolean useCSS = true;
            svgGenerator.stream(out, useCSS);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SVGGraphics2DIOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // resetting
        ((BlockPanel) bp).getDrawingPane().setDoubleBuffered(db);
        
    }
    
    private void toPostscript(JComponent bp, PrintStream p) {
        PSStreamPrinterFactory factory = new PSStreamPrinterFactory();
        StreamPrintService sps = factory.getPrintService(p);
        DocPrintJob pj = sps.createPrintJob();
        Doc doc = new SimpleDoc(new DataPrinter(bp), 
                DocFlavor.SERVICE_FORMATTED.PRINTABLE, null);

        try { 
            pj.print(doc, new HashPrintRequestAttributeSet());
        } catch(PrintException pe){
            System.err.println("PrintException : "+pe);
        }
    }

    
    /**
     * Saving to a pixel based graphics format. Supports PNG and JPG
     * 
     * @param bp
     * @param p
     * @param format
     */
    private void toPixelGraphic (JComponent bp, PrintStream p, String format) 
            throws OutOfMemoryError {

        Dimension imgSize = ((BlockPanel) bp).getScaledSize();
        BufferedImage img;
            img = new BufferedImage(imgSize.width, imgSize.height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D grap = img.createGraphics();
            bp.paint(grap);
            grap.dispose();

            try {
                ImageIO.write(img, format, p);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    }

    private void toXML(IDataPackage data, PrintStream p) {
        p.print("<parse>\n");
        OM2XMLVisitor visitor = new OM2XMLVisitor();
        p.print(visitor.output(data.getModel()));
        p.print("</parse>\n");
    }

    public void print(JComponent view) {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(new DataPrinter(view));

        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (PrinterException pe) {
                System.out.println("Error printing: " + pe);
            }
        }
    }

    class DataPrinter implements Printable {

        JComponent view;

        DataPrinter(JComponent view) {
            this.view = view;
        }

        public int print(Graphics g, PageFormat pageFormat, int pageIndex)
                throws PrinterException {
            if (pageIndex > 0) {
                return (NO_SUCH_PAGE);
            } else {
                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pageFormat.getImageableX(), pageFormat
                        .getImageableY());
                // Turn off double buffering
                RepaintManager currentManager = RepaintManager
                        .currentManager(view);
                currentManager.setDoubleBufferingEnabled(false);

                view.paint(g2d);
                // Turn double buffering back on
                currentManager.setDoubleBufferingEnabled(true);

                return (PAGE_EXISTS);
            }

        }

    }

}
