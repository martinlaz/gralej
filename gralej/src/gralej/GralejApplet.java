/*
 *  $Id$
 *
 *  Author:
 *     Martin Lazarov [mlazarov at sfs.uni-tuebingen.de]
 *     
 *  This file is part of the Gralej system
 *     http://code.google.com/p/gralej/
 *
 *  Gralej is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gralej is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package gralej;

import gralej.controller.StreamInfo;
import gralej.blocks.BlockPanel;
import gralej.blocks.BlockPanelContainer;
import gralej.blocks.BlockPanelStyle;
import gralej.parsers.GraleParserFactory;
import gralej.parsers.IDataPackage;
import gralej.parsers.IGraleParser;
import gralej.parsers.UnsupportedProtocolException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.CardLayout;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Collections;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import netscape.javascript.JSObject;

/**
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
            _parser = GraleParserFactory.createParser(StreamInfo.GRISU);
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
                StreamInfo.GRISU);
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
        view.setAutoResize(true);
        _layout.show(getContentPane(), "_bp" + i);
        log("switching to view: " + i);
        panelResized(view);
    }
    
    public void openViewInFrame(int i) {
        if (_parseResults == null || i >= _parseResults.size())
            return;
        IDataPackage datapak = _parseResults.get(i);
        JFrame f = new JFrame();
        f.setTitle(datapak.getTitle());
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setLocationByPlatform(true);
        BlockPanel blockPanel = new BlockPanel(datapak.getModel());
        blockPanel.setAutoResize(false);
        JPanel view = blockPanel.getUI();
        f.add(view);
        f.pack();
        f.setVisible(true);
        return;
    }
    
    public void panelResized(BlockPanel bp) {
        Dimension d = bp.getScaledSize();
        _win.eval("resizeGralejApplet(" +
            d.width + "," +
            d.height + ")");
        log("panel resized");
    }
    
    public static BlockPanelStyle getBlockPanelStyle() {
        return BlockPanelStyle.getInstance();
    }
    
    public static Config getConfig() {
        return Config.currentConfig();
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
