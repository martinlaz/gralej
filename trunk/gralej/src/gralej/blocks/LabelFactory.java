package gralej.blocks;

import gralej.Config;
import java.util.Map;
import java.util.TreeMap;

public class LabelFactory {

    // list
    String LIST_LBRACKET;
    String LIST_RBRACKET;
    String LIST_SEPARATOR;
    String LIST_TAIL_SEPARATOR;

    Map<String, LabelStyle> _labelStyles = new TreeMap<String, LabelStyle>();

    static private LabelFactory _instance;
    
    public LabelFactory() {
        init();
    }
    
    public static LabelFactory getInstance() {
        if (_instance == null)
            _instance = new LabelFactory();
        return _instance;
    }
    
    private void init() {
        LIST_LBRACKET       = Config.s("block.label.list.text.left");
        LIST_RBRACKET       = Config.s("block.label.list.text.right");
        LIST_SEPARATOR      = Config.s("block.label.list.text.separator");
        LIST_TAIL_SEPARATOR = Config.s("block.label.list.text.tailSeparator");
        
        String[] types = new String[] {
            "tag",
            "sort",
            "attribute",
            "list",
            "any",
            "species",
            "node.internal",
            "node.leaf"
        };
        
        // instantiante all label styles
        for (String type : types)
            getLabelStyle(type);
    }

    LabelStyle getLabelStyle(String type_) {
        String type = "block.label." + type_;
        LabelStyle ls = _labelStyles.get(type);
        if (ls == null) {
            ls = new LabelStyle(type_);
            initLabelStyle(ls);
            _labelStyles.put(type, ls);
        }
        return ls;
    }
    
    private void initLabelStyle(LabelStyle ls) {
        String type = "block.label." + ls.getName();
        ls.setFont(
                Config.font(type + ".fontSpec"));
        ls.setTextColor(
                Config.color(type + ".text.color"));
        ls.setTextAltColor(
                Config.color(type + ".text.colorAlt"));
        ls.setMarginTop(
                Config.i(type + ".margin.top"));
        ls.setMarginLeft(
                Config.i(type + ".margin.left"));
        ls.setMarginRight(
                Config.i(type + ".margin.right"));
        ls.setMarginBottom(
                Config.i(type + ".margin.bottom"));
        ls.setFrameThickness(
                Config.i(type + ".frame.thickness"));
        ls.setFrameColor(
                Config.color(type + ".frame.color"));
        ls.setFrameDashed(
                Config.bool(type + ".frame.isDashed"));
    }
    
    void updateConfig(Config cfg) {
        for (LabelStyle s : _labelStyles.values()) {
            cfg.put("block.label." + s.getName() + ".fontSpec",      s.getFont());
            cfg.put("block.label." + s.getName() + ".text.color",    s.getTextColor());
            cfg.put("block.label." + s.getName() + ".text.colorAlt", s.getTextAltColor());
            cfg.put("block.label." + s.getName() + ".margin.top",    s.getMarginTop());
            cfg.put("block.label." + s.getName() + ".margin.left",   s.getMarginLeft());
            cfg.put("block.label." + s.getName() + ".margin.right",  s.getMarginRight());
            cfg.put("block.label." + s.getName() + ".margin.bottom",   s.getMarginBottom());
            cfg.put("block.label." + s.getName() + ".frame.thickness", s.getFrameThickness());
            cfg.put("block.label." + s.getName() + ".frame.color",     s.getFrameColor());
            cfg.put("block.label." + s.getName() + ".frame.isDashed",  s.isFrameDashed());
        }
    }
    
    void updateSelf() {
        init();
        for (LabelStyle ls : _labelStyles.values())
            initLabelStyle(ls);
    }

    public ContentLabel createContentLabel(String text, LabelStyle style, BlockPanel panel) {
        return new ContentLabel(panel, style, text);
    }

    public Label createLabel(String text, LabelStyle style, BlockPanel panel) {
        return new Label(panel, style, text);
    }

    public ContentLabel createTagLabel(String text, BlockPanel panel) {
        return createContentLabel(text, getLabelStyle("tag"), panel);
    }

    public ContentLabel createSortLabel(String text, BlockPanel panel) {
        return createContentLabel(text, getLabelStyle("sort"), panel);
    }

    public ContentLabel createAttributeLabel(String text, BlockPanel panel) {
        return createContentLabel(text, getLabelStyle("attribute"), panel);
    }

    public Label createListLBracketLabel(BlockPanel panel) {
        return createLabel(LIST_LBRACKET, getLabelStyle("list"), panel);
    }

    public Label createListRBracketLabel(BlockPanel panel) {
        return createLabel(LIST_RBRACKET, getLabelStyle("list"), panel);
    }

    public Label createListSeparatorLabel(BlockPanel panel) {
        return createLabel(LIST_SEPARATOR, getLabelStyle("list"), panel);
    }
    
    public Label createListTailSeparatorLabel(BlockPanel panel) {
        return createLabel(
            LIST_TAIL_SEPARATOR, getLabelStyle("list"), panel);
    }

    public Label createAnyLabel(String text, BlockPanel panel) {
        return createLabel(text, getLabelStyle("any"), panel);
    }

    public Label createSpeciesLabel(String text, BlockPanel panel) {
        return createLabel(text, getLabelStyle("species"), panel);
    }

    public Label createInternalNodeLabel(String text, BlockPanel panel) {
        return createContentLabel(text, getLabelStyle("node.internal"), panel);
    }

    public Label createLeafNodeLabel(String text, BlockPanel panel) {
        return createContentLabel(text, getLabelStyle("node.leaf"), panel);
    }
}
