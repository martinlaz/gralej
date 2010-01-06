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

package gralej.blocks.configurator;

import gralej.Config;
import gralej.blocks.*;
import gralej.controller.StreamInfo;
import gralej.om.IVisitable;
import gralej.om.IneqsAndResidue;
import gralej.parsers.GraleParserFactory;
import gralej.parsers.IDataPackage;
import gralej.parsers.IGraleParser;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.InputStream;

/**
 *
 * @author Martin
 */
public class BlockConfigurator extends BlockPanel {
    public interface Handler {
        void modifyLabelStyle(LabelStyle style);
        void modifyBlockLayout(BlockLayout layout);
        void modifyMiscSettings();
        void updateMessage(String message);
        void clearMessage();
    }
    
    Handler _handler;
    
    public BlockConfigurator(Handler handler) {
        this(handler, Config.currentConfig());
    }
    public BlockConfigurator(Handler handler, Config cfg) {
        super(getSample(), IneqsAndResidue.EMPTY, new BlockPanelStyle(cfg), false);
        setDisplayingModelHiddenFeatures(true);
        showNodeContents(false);
        
        _handler = handler;
        
        String[] flipPaths = new String[] {
            "/0/0/0",               // expand 'phrase:[peter,likes,mary]'
            "/0/1/0",               // expand 'word:[peter]'
            "/0/3/0",               // expand 'word:[likes]'
            "/0/3/1/0",             // collapse 'word'
            "/0/0/1/1/1/1/1/0/0",   // collapse 'LOC'
            "/0/0/1/1/2/0",         // collapse 'DAUGHTERS'
            "/0/1/1/1/0/1/1/0/0"    // expand 'peter'
        };
        
        for (String path : flipPaths) {
            ContentLabel cl = (ContentLabel) getContent().getDescendant(path);
            cl.flip();
        }
    }
    
    private static IVisitable _SAMPLE;
    
    private static IVisitable getSample() {
        if (_SAMPLE != null)
            return _SAMPLE;
        try {
            StreamInfo streamInfo = new StreamInfo("grisu");
            IGraleParser parser = GraleParserFactory.createParser(streamInfo);
            InputStream is = BlockConfigurator.class.getResourceAsStream(
                    "/gralej/resource/sample.GRALE");
            IDataPackage dpak = parser.parseAll(is, streamInfo).get(0);
            return dpak.getModel();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private String _lastMessage = null;
    private void updateMessage(String msg) {
        if (msg == null) {
            if (_lastMessage == null)
                return;
            _lastMessage = null;
            _handler.clearMessage();
        }
        else if (msg.equals(_lastMessage))
            return;
        _lastMessage = msg;
        _handler.updateMessage(msg);
    }

    @Override
    protected void onMousePressed(MouseEvent e) {  }

    @Override
    protected void onMouseReleased(MouseEvent e) {  }

    @Override
    protected void onKeyPressed(KeyEvent ev) { }
    
    @Override
    protected void onMouseClicked(MouseEvent ev) {
        setSelectedBlock(null);
        
        Block target = findContainingBlock(getContent(), ev.getX(), ev.getY());
        if (target == null)
            ;
        else if (target instanceof ContainerBlock
                || (target instanceof Label && ev.isControlDown())) {
            if (target instanceof Label)
                target = target.getParent();
            BlockLayout layout = ((ContainerBlock)target).getLayout();
            if (layout != null && layout.getName() != null)
                _handler.modifyBlockLayout(layout);
            else
                _handler.modifyMiscSettings();
        }
        else if (target instanceof Label) {
            Label lab = (Label) target;
            _handler.modifyLabelStyle(lab.getStyle());
        }
        else
            _handler.modifyMiscSettings();
        
        updateMessage(null);
    }
    
    @Override
    protected void onMouseMoved(MouseEvent ev) {
        if (ev.getID() != MouseEvent.MOUSE_MOVED)
            return;
        Block target = findContainingBlock(getContent(), ev.getX(), ev.getY());
        String msg = "<html>Click to modify miscellaneous <b>BlockPanel</b>,"
                + " <b>AVMBlock</b> and <b>TreeBlock</b> settings.";
        if (target == null)
            ;
        else if (target instanceof ContainerBlock
                || (target instanceof Label && ev.isControlDown())) {
            if (target instanceof Label)
                target = target.getParent();
            BlockLayout layout = ((ContainerBlock)target).getLayout();
            if (layout != null && layout.getName() != null)
                msg = "<html>Click to modify the <b>" + layout.getName() + "</b> layout.";
            else if (!(target instanceof TreeBlock)) {
                target = null;
                msg = null;
            }
        }
        else if (target instanceof Label) {
            Label label = (Label) target;
            LabelStyle style = label.getStyle();
            msg = "<html>Click to modify the <b>" + style.getName() + "</b> style. ";
            msg += "Press <i>Control</i> and click to modify the <b>"
                    + label.getParent().getLayout().getName() + "</b> layout.";
        }
        else {
            target = null;
            msg = null;
        }
        setSelectedBlock(target);
        updateMessage(msg);
    }
    
    @Override
    protected void onMouseExited(MouseEvent evt) {
        setSelectedBlock(null);
        updateMessage(null);
    }
}
