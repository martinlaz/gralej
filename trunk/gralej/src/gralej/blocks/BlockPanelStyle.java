package gralej.blocks;

import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class BlockPanelStyle {
    
    static private BlockPanelStyle _instance;
    
    public static BlockPanelStyle getInstance() {
        if (_instance == null)
            _instance = new BlockPanelStyle(
                    LabelFactory.getInstance(),
                    LayoutFactory.getInstance()
                    );
        return _instance;
    }
    
    
    public BlockPanelStyle() {
        this(new LabelFactory(), new LayoutFactory());
    }
    
    public BlockPanelStyle(LabelFactory labfac, LayoutFactory layfac) {
        _labfac = labfac;
        _layfac = layfac;
        
        initFields();
    }
    
    public void updatePreferences() {
        javax.swing.JOptionPane.showMessageDialog(
                null, "BlockPanelStyle.updatePreferences() is not implemented yet!");
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
    @Key("avm.bracket.color")
    Color avmBracketColor;
    @Key("avm.bracket.edge.length")
    int avmBracketEdgeLength;
    @Key("avm.bracket.style.rounded")
    boolean avmBracketRounded;
    @Key("layout.avm.compact")
    boolean avmLayoutCompact;
    
    //
    @Key("panel.displayModelHiddenFeatures")
    boolean displayingModelHiddenFeatures;
    @Key("panel.autoExpandTags")
    boolean autoExpandingTags;
    
    // long labels
    @Key("label.continuation.text")
    String longLabelTextContinuation;
    @Key("label.text.maxLength")
    int maxLabelTextLength;
    
    // tree layout
    @Key("tree.minDistance.horizontal")
    int minTreeNodesHorizontalDistance;
    @Key("tree.minDistance.vertical")
    int minTreeNodesVerticalDistance;
    @Key("tree.node.content.isInitiallyVisible")
    boolean nodeContentInitiallyVisible;    // don't update on that
    @Key("tree.edge.color")
    Color treeEdgeColor;
    
    // panel
    @Key("panel.margins.all")
    int margin;
    @Key("panel.background")
    Color backgroundColor;

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

    public boolean isAutoExpandingTags() {
        return autoExpandingTags;
    }

    public LabelFactory getLabelFactory() {
        return _labfac;
    }

    public LayoutFactory getLayoutFactory() {
        return _layfac;
    }

    public boolean isNodeContentInitiallyVisible() {
        return nodeContentInitiallyVisible;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isDisplayingModelHiddenFeatures() {
        return displayingModelHiddenFeatures;
    }

    public void setDisplayingModelHiddenFeatures(boolean displayingModelHiddenFeatures) {
        this.displayingModelHiddenFeatures = displayingModelHiddenFeatures;
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
    
    public void addStyleChangeListener(StyleChangeListener l) {
        _changeListeners.add(l);
    }
    
    public void removeStyleChangeListener(StyleChangeListener l) {
        _changeListeners.remove(l);
    }
    
    public void fireStyleChanged() {
        for (StyleChangeListener l : _changeListeners)
            l.styleChanged(this);
    }
}
