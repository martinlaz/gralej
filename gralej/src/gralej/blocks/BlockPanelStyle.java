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

package gralej.blocks;

import gralej.Config;
import java.awt.Color;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BlockPanelStyle implements ChangeListener {
    
    private static BlockPanelStyle _instance;
    
    public static BlockPanelStyle getInstance() {
        if (_instance == null) {
            _instance = new BlockPanelStyle();
            _instance.addStyleChangeListener(new StyleChangeListener() {
                public void styleChanged(Object sender) {
                    // just to keep the singleton alive
                }
            });
        }
        return _instance;
    }
    
    
    public BlockPanelStyle() {
        this(Config.currentConfig());
    }
    
    public BlockPanelStyle(Config cfg) {
        _cfg = cfg;
        _labfac = new LabelFactory(cfg);
        _layfac = new LayoutFactory(cfg);
        
        initFields();
        Config.currentConfig().addChangeListener(this);
    }
    
    public void stateChanged(ChangeEvent e) {
        if (isUpdatingConfig) return;
        configChanged();
    }
    
    private boolean isUpdatingConfig;

    public void updateConfig() {
        if (isUpdatingConfig) return;
        isUpdatingConfig = true;
        _labfac.updateConfig();
        _layfac.updateConfig();
        
        for (Field f : getClass().getDeclaredFields()) {
            Key k = (Key) f.getAnnotation(Key.class);
            if (k == null)
                continue;
            String s = k.value();
            try {
                if (f.getType() == String.class)
                    _cfg.set(s, (String) f.get(this));
                else if (f.getType() == int.class)
                    _cfg.set(s, (Integer) f.get(this));
                else if (f.getType() == boolean.class)
                    _cfg.set(s, (Boolean) f.get(this));
                else if (f.getType() == Color.class)
                    _cfg.set(s, (Color) f.get(this));
                else
                    throw new Exception(
                            "Unsupported field type: ["
                            + f.getType() + "] for field: " + f);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        isUpdatingConfig = false;
    }
    
    private void initFields() {
        for (Field f : getClass().getDeclaredFields()) {
            Key k = (Key) f.getAnnotation(Key.class);
            if (k == null)
                continue;
            String s = k.value();
            try {
                if (f.getType() == String.class)
                    f.set(this, _cfg.get(s));
                else if (f.getType() == int.class)
                    f.set(this, _cfg.getInt(s));
                else if (f.getType() == boolean.class)
                    f.set(this, _cfg.getBool(s));
                else if (f.getType() == Color.class)
                    f.set(this, _cfg.getColor(s));
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
    private Map<StyleChangeListener,Object> _changeListeners = new WeakHashMap<StyleChangeListener,Object>();
    // labels
    private LabelFactory _labfac;
    // layouts
    private LayoutFactory _layfac;
    // configuration
    private Config _cfg;
    
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
    @Key("block.panel.different.background.color")
    Color differentBackgroundColor;
    
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
    
    public Color getDifferentBackgroundColor() {
        return differentBackgroundColor;
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
        _changeListeners.put(l, null);
    }
    
    public void removeStyleChangeListener(StyleChangeListener l) {
        _changeListeners.remove(l);
    }
    
    public void fireStyleChanged() {
        for (StyleChangeListener l : _changeListeners.keySet()) {
            if (l != null) {
                l.styleChanged(this);
            }
        }
    }

    private void configChanged() {
        _labfac.updateSelf();
        _layfac.updateSelf();
        initFields();
        fireStyleChanged();
    }
}
