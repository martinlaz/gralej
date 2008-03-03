/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.blocks;

import gralej.controller.StreamInfo;
import gralej.om.IVisitable;
import gralej.parsers.GraleParserFactory;
import gralej.parsers.IDataPackage;
import gralej.parsers.IGraleParser;
import java.awt.event.MouseEvent;
import java.io.InputStream;

/**
 *
 * @author Martin
 */
public class BlockConfigurator extends BlockPanel {
    public interface Handler {
        boolean modifyLabelStyle(LabelStyle style, String labelText);
        boolean modifyBlockLayout(BlockLayout layout);
        void updateMessage(String message);
        void clearMessage();
    }
    
    Handler _handler;
    
    public BlockConfigurator(Handler handler) {
        super(getSample(), makeStyle());
        _handler = handler;
        
        String[] flipPaths = new String[] {
            "/0/0/0",               // expand 'phrase:[peter,likes,mary]'
            "/0/1/0",               // expand 'word:[peter]'
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
    
    private static BlockPanelStyle makeStyle() {
        BlockPanelStyle s = new BlockPanelStyle();
        s.displayingModelHiddenFeatures = true;
        s.autoExpandingTags = false;
        s.nodeContentInitiallyVisible = false;
        return s;
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
    protected void onMousePressed(MouseEvent ev) {
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
                if (_handler.modifyBlockLayout(layout))
                    styleChanged(null);
        }
        else if (target instanceof Label) {
            Label lab = (Label) target;
            if (_handler.modifyLabelStyle(lab.getStyle(), lab.getVisibleText()))
                styleChanged(null);
        }
        
        updateMessage(null);
    }
    
    @Override
    protected void onMouseMoved(MouseEvent ev) {
        if (ev.getID() != MouseEvent.MOUSE_MOVED)
            return;
        Block target = findContainingBlock(getContent(), ev.getX(), ev.getY());
        String msg = null;
        if (target == null)
            ;
        else if (target instanceof ContainerBlock
                || (target instanceof Label && ev.isControlDown())) {
            if (target instanceof Label)
                target = target.getParent();
            BlockLayout layout = ((ContainerBlock)target).getLayout();
            if (layout != null && layout.getName() != null)
                msg = "Modify the '" + layout.getName() + "' layout";
            else
                target = null;
        }
        else if (target instanceof Label) {
            LabelStyle style = ((Label)target).getStyle();
            msg = "Modify the '" + style.getName() + "' label style";
        }
        else {
            target = null;
        }
        setSelectedBlock(target);
        updateMessage(msg);
    }
}
