package gralej.blocks;

import gralej.prefs.GPrefsChangeListener;
import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class BlockPanelStyle implements GPrefsChangeListener {
    
    static private BlockPanelStyle _instance;
    
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
        
        //GralePreferences.getInstance().addListener(this, "block.");
    }

    public void updatePreferences() {
        //GralePreferences.getInstance().removeListener(this);
        //Log.debug("removing", this, "as prefs change listener. thread:", Thread.currentThread());
        _labfac.updatePreferences();
        _layfac.updatePreferences();
        
        for (Field f : getClass().getDeclaredFields()) {
            Key k = (Key) f.getAnnotation(Key.class);
            if (k == null)
                continue;
            String s = k.value();
            try {
                if (f.getType() == String.class)
                    Config.set(s, (String) f.get(this));
                else if (f.getType() == int.class)
                    Config.set(s, (Integer) f.get(this));
                else if (f.getType() == boolean.class)
                    Config.set(s, (Boolean) f.get(this));
                else if (f.getType() == Color.class)
                    Config.set(s, (Color) f.get(this));
                else
                    throw new Exception(
                            "Unsupported field type: [" + f.getType() + "] for field: " + f);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        getInstance().preferencesChange();
        
        //Log.debug("adding", this, "as prefs change listener");
        //GralePreferences.getInstance().addListener(this, "block.");
        //Log.debug("done adding", this, "as prefs change listener");
    }
    
    private void initFields() {
        for (Field f : getClass().getDeclaredFields()) {
            Key k = (Key) f.getAnnotation(Key.class);
            if (k == null)
                continue;
            String s = k.value();
            try {
                if (f.getType() == String.class)
                    f.set(this, Config.get(s));
                else if (f.getType() == int.class)
                    f.set(this, Config.getInt(s));
                else if (f.getType() == boolean.class)
                    f.set(this, Boolean.parseBoolean(Config.get(s)));
                else if (f.getType() == Color.class)
                    f.set(this, Config.getColor(s));
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
    @Key("block.panel.selectedBlockColor")
    Color selectedBlockColor;
    
    // different labels
    @Key("block.label._diff.text.color")
    Color differentTextColor;
    @Key("block.label._diff.strikethroughline.color")
    Color strikethroughLineColor;

    public Color getSelectedBlockColor() {
        return selectedBlockColor;
    }

    public void setSelectedBlockColor(Color selectedBlockColor) {
        this.selectedBlockColor = selectedBlockColor;
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

    public void preferencesChange() {
        //Log.debug("updating blockpanelstyle", this, "from prefs. thread:", Thread.currentThread());
        _labfac.updateSelf();
        _layfac.updateSelf();
        initFields();
        fireStyleChanged();
    }
}
