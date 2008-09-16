package gralej.blocks;

import gralej.Config;
import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BlockPanelStyle {
    
    private static BlockPanelStyle _instance;
    private ChangeListener _configChangeListener;
    
    public static BlockPanelStyle getInstance() {
        if (_instance == null) {
            _instance = new BlockPanelStyle(
                    LabelFactory.getInstance(),
                    LayoutFactory.getInstance()
                    );
            _instance.addStyleChangeListener(new StyleChangeListener() {
                public void styleChanged(Object sender) {
                    // just to keep the singleton alive
                }
            });
        }
        return _instance;
    }
    
    
    public BlockPanelStyle() {
        this(new LabelFactory(), new LayoutFactory());
    }
    
    public BlockPanelStyle(LabelFactory labfac, LayoutFactory layfac) {
        _labfac = labfac;
        _layfac = layfac;
        
        initFields();
        _configChangeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                configChanged();
            }
        };
        Config.currentConfig().addChangeListener(_configChangeListener);
    }

    public void updateConfig() {
        Config cfg = new Config(Config.currentConfig());
        
        _labfac.updateConfig(cfg);
        _layfac.updateConfig(cfg);
        
        for (Field f : getClass().getDeclaredFields()) {
            Key k = (Key) f.getAnnotation(Key.class);
            if (k == null)
                continue;
            String s = k.value();
            try {
                if (f.getType() == String.class)
                    cfg.put(s, (String) f.get(this));
                else if (f.getType() == int.class)
                    cfg.put(s, (Integer) f.get(this));
                else if (f.getType() == boolean.class)
                    cfg.put(s, (Boolean) f.get(this));
                else if (f.getType() == Color.class)
                    cfg.put(s, (Color) f.get(this));
                else
                    throw new Exception(
                            "Unsupported field type: [" + f.getType() + "] for field: " + f);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        if (Config.currentConfig().updateFrom(cfg))
            Config.currentConfig().fireStateChanged(_configChangeListener);
    }
    
    private void initFields() {
        for (Field f : getClass().getDeclaredFields()) {
            Key k = (Key) f.getAnnotation(Key.class);
            if (k == null)
                continue;
            String s = k.value();
            try {
                if (f.getType() == String.class)
                    f.set(this, Config.s(s));
                else if (f.getType() == int.class)
                    f.set(this, Config.i(s));
                else if (f.getType() == boolean.class)
                    f.set(this, Config.bool(s));
                else if (f.getType() == Color.class)
                    f.set(this, Config.color(s));
                else
                    throw new Exception(
                            "Unsupported field type: [" + f.getType() + "] for field: " + f);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    // style listeners
    Set<StyleChangeListener> _changeListeners = new HashSet();
    // labels
    LabelFactory _labfac;
    
    // layouts
    LayoutFactory _layfac;
    
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Key {
        String value();
    }
    
    // avm related stuff
    @Key("block.avm.bracket.color")
    Color avmBracketColor;
    @Key("block.avm.bracket.edge.length")
    int avmBracketEdgeLength;
    @Key("block.avm.bracket.style.rounded")
    boolean avmBracketRounded;
    @Key("block.layout.avm.compact")
    boolean avmLayoutCompact;
    
    // long labels
    @Key("block.label.continuation.text")
    String longLabelTextContinuation;
    @Key("block.label.text.maxLength")
    int maxLabelTextLength;
    
    // tree layout
    @Key("block.tree.minDistance.horizontal")
    int minTreeNodesHorizontalDistance;
    @Key("block.tree.minDistance.vertical")
    int minTreeNodesVerticalDistance;
    @Key("block.tree.edge.color")
    Color treeEdgeColor;
    
    // panel
    @Key("block.panel.margins.all")
    int margin;
    @Key("block.panel.background")
    Color backgroundColor;
    @Key("block.panel.selection.background.color")
    Color selectionBackgroundColor;
    @Key("block.panel.selection.frame.color")
    Color selectionFrameColor;
    
    // different labels
    @Key("block.label._diff.text.color")
    Color differentTextColor;
    @Key("block.label._diff.strikethroughline.color")
    Color strikethroughLineColor;

    public Color getSelectionBackgroundColor() {
        return selectionBackgroundColor;
    }

    public void setSelectionBackgroundColor(Color selectedBlockColor) {
        this.selectionBackgroundColor = selectedBlockColor;
    }

    public Color getSelectionFrameColor() {
        return selectionFrameColor;
    }

    public void setSelectionFrameColor(Color selectionFrameColor) {
        this.selectionFrameColor = selectionFrameColor;
    }

    public Color getAVMBracketColor() {
        return avmBracketColor;
    }

    public void setAVMBracketColor(Color avmBracketColor) {
        this.avmBracketColor = avmBracketColor;
    }

    public int getAVMBracketEdgeLength() {
        return avmBracketEdgeLength;
    }

    public void setAVMBracketEdgeLength(int avmBracketEdgeLength) {
        this.avmBracketEdgeLength = avmBracketEdgeLength;
    }

    public boolean isAVMBracketRounded() {
        return avmBracketRounded;
    }

    public void setAVMBracketRounded(boolean avmBracketRounded) {
        this.avmBracketRounded = avmBracketRounded;
    }

    public boolean isAVMLayoutCompact() {
        return avmLayoutCompact;
    }

    public void setAVMLayoutCompact(boolean avmLayoutCompact) {
        this.avmLayoutCompact = avmLayoutCompact;
    }

    public LabelFactory getLabelFactory() {
        return _labfac;
    }

    public LayoutFactory getLayoutFactory() {
        return _layfac;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getLongLabelTextContinuation() {
        return longLabelTextContinuation;
    }

    public void setLongLabelTextContinuation(String longLabelTextContinuation) {
        this.longLabelTextContinuation = longLabelTextContinuation;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public int getMaxLabelTextLength() {
        return maxLabelTextLength;
    }

    public void setMaxLabelTextLength(int maxLabelTextLength) {
        this.maxLabelTextLength = maxLabelTextLength;
    }

    public int getMinTreeNodesHorizontalDistance() {
        return minTreeNodesHorizontalDistance;
    }

    public void setMinTreeNodesHorizontalDistance(int newValue) {
        this.minTreeNodesHorizontalDistance = newValue;
    }

    public int getMinTreeNodesVerticalDistance() {
        return minTreeNodesVerticalDistance;
    }

    public void setMinTreeNodesVerticalDistance(int newValue) {
        this.minTreeNodesVerticalDistance = newValue;
    }

    public Color getTreeEdgeColor() {
        return treeEdgeColor;
    }

    public void setTreeEdgeColor(Color treeEdgeColor) {
        this.treeEdgeColor = treeEdgeColor;
    }

    public Color getDifferentTextColor() {
        return differentTextColor;
    }

    public void setDifferentTextColor(Color differentTextColor) {
        this.differentTextColor = differentTextColor;
    }

    public Color getStrikethroughLineColor() {
        return strikethroughLineColor;
    }

    public void setStrikethroughLineColor(Color strikethroughLineColor) {
        this.strikethroughLineColor = strikethroughLineColor;
    }
    
    public void addStyleChangeListener(StyleChangeListener l) {
        _changeListeners.add(l);
    }
    
    public void removeStyleChangeListener(StyleChangeListener l) {
        _changeListeners.remove(l);
        //if (_changeListeners.isEmpty())
          //  GralePreferences.getInstance().removeListener(this);
    }
    
    public void fireStyleChanged() {
        for (StyleChangeListener l : _changeListeners)
            l.styleChanged(this);
    }

    private void configChanged() {
        _labfac.updateSelf();
        _layfac.updateSelf();
        initFields();
        fireStyleChanged();
    }
}
