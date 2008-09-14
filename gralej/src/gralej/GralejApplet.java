package gralej;

import gralej.controller.StreamInfo;
import gralej.blocks.BlockPanel;
import gralej.blocks.BlockPanelContainer;
import gralej.blocks.BlockPanelStyle;
import gralej.parsers.GraleParserFactory;
import gralej.parsers.IDataPackage;
import gralej.parsers.IGraleParser;
import gralej.parsers.UnsupportedProtocolException;
import gralej.prefs.GralePreferences;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.CardLayout;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Collections;
import javax.swing.JApplet;
import netscape.javascript.JSObject;

/**
 *
 * @author Martin
 */
public class GralejApplet extends JApplet implements BlockPanelContainer {
    IGraleParser _parser;
    List<IDataPackage> _parseResults = Collections.emptyList();
    BlockPanel[] _views = new BlockPanel[0];
    JSObject _win;
    CardLayout _layout;

    @Override
    public void init() {
        _win = JSObject.getWindow(this);
        _layout = new CardLayout();
        setLayout(_layout);
        try {
            _parser = GraleParserFactory.createParser(new StreamInfo("grisu"));
        }
        catch (UnsupportedProtocolException e) {
            throw new RuntimeException(e);
        }
        log("initialized");
    }
    
    @Override
    public void start() {
        _win.eval("gralejAppletStarted()");
        log("started");
    }
    
    public int parseTraleMessage(String s) {
        try {
            _parseResults = _parser.parseAll(
                new ByteArrayInputStream(s.getBytes("utf-8")),
                new StreamInfo("grisu"));
            _views = new BlockPanel[_parseResults.size()];
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        log("parsed '" + s + "'");
        return _parseResults.size();
    }
    
    public int getNumViews() {
        return _parseResults.size();
    }
    
    public BlockPanel getView(int i) {
        if (i >= _views.length)
            return null;
        if (_views[i] == null) {
            _views[i] = _parseResults.get(i).createView();
            add("_bp" + i, _views[i].getCanvas());
        }
        return _views[i];
    }
    
    public void showView(int i) {
        BlockPanel view = getView(i);
        if (view == null) return;
        _layout.show(getContentPane(), "_bp" + i);
        //validate();
        log("switching to view: " + i);
        panelResized(view);
    }
    
    public void panelResized(BlockPanel bp) {
        Dimension d = bp.getScaledSize();
        _win.eval("resizeGralejApplet(" +
            d.width + "," +
            d.height + ")");
    }
    
    public static BlockPanelStyle getBlockPanelStyle() {
        return BlockPanelStyle.getInstance();
    }
    
    public static GralePreferences getPreferences() {
        return GralePreferences.getInstance();
    }
    
    public static Color decodeColor(String s) {
        return Color.decode(s);
    }
    
    public static Font decodeFont(String s) {
        return Font.decode(s);
    }
    
    private void log(String msg) {
        System.out.println("GralejApplet says: " + msg);
    }
}
