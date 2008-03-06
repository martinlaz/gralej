package gralej.blocks;

import java.util.Map;
import java.util.TreeMap;

public class LabelFactory {

    String DEFAULT_FONT_SPEC;
    // colors
    String DEFAULT_TEXT_COLOR;
    String DEFAULT_TEXT_ALT_COLOR;
    // margins
    int DEFAULT_MARGIN_TOP;
    int DEFAULT_MARGIN_LEFT;
    int DEFAULT_MARGIN_RIGHT;
    int DEFAULT_MARGIN_BOTTOM;
    // frame
    int DEFAULT_FRAME_THICKNESS;
    String DEFAULT_FRAME_COLOR;
    String DEFAULT_FRAME_IS_DASHED;
    
    // list
    String LIST_LBRACKET_TEXT;
    String LIST_RBRACKET_TEXT;
    String LIST_SEPARATOR_TEXT;

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
        DEFAULT_FONT_SPEC       = Config.get   ("block.label._default.fontSpec");
        DEFAULT_TEXT_COLOR      = Config.get   ("block.label._default.text.color");
        DEFAULT_TEXT_ALT_COLOR  = Config.get   ("block.label._default.text.colorAlt");
        DEFAULT_MARGIN_TOP      = Config.getInt("block.label._default.margin.top");
        DEFAULT_MARGIN_LEFT     = Config.getInt("block.label._default.margin.left");
        DEFAULT_MARGIN_RIGHT    = Config.getInt("block.label._default.margin.right");
        DEFAULT_MARGIN_BOTTOM   = Config.getInt("block.label._default.margin.bottom");
        DEFAULT_FRAME_THICKNESS = Config.getInt("block.label._default.frame.thickness");
        DEFAULT_FRAME_COLOR     = Config.get   ("block.label._default.frame.color");
        DEFAULT_FRAME_IS_DASHED = Config.get   ("block.label._default.frame.isDashed");

        LIST_LBRACKET_TEXT      = Config.get("block.label.list.text.left");
        LIST_RBRACKET_TEXT      = Config.get("block.label.list.text.right");
        LIST_SEPARATOR_TEXT     = Config.get("block.label.list.text.separator");
        
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
        LabelStyle lp = _labelStyles.get(type);
        if (lp == null) {
            lp = new LabelStyle(type_);
            lp.setFont(Config.getFont(type + ".fontSpec",
                    DEFAULT_FONT_SPEC));
            lp.setTextColor(Config.getColor(type + ".text.color",
                    DEFAULT_TEXT_COLOR));
            lp.setTextAltColor(Config.getColor(type + ".text.colorAlt",
                    DEFAULT_TEXT_ALT_COLOR));
            lp.setMarginTop(Config.getInt(type + ".margin.top", DEFAULT_MARGIN_TOP));
            lp.setMarginLeft(Config.getInt(type + ".margin.left", DEFAULT_MARGIN_LEFT));
            lp.setMarginRight(Config.getInt(type + ".margin.right", DEFAULT_MARGIN_RIGHT));
            lp.setMarginBottom(Config.getInt(type + ".margin.bottom", DEFAULT_MARGIN_BOTTOM));
            lp.setFrameThickness(Config.getInt(type + ".frame.thickness", DEFAULT_FRAME_THICKNESS));
            lp.setFrameColor(Config.getColor(type + ".frame.color", DEFAULT_FRAME_COLOR));
            lp.setFrameDashed(Boolean.parseBoolean(Config.get(type + ".frame.isDashed", DEFAULT_FRAME_IS_DASHED)));
            
            _labelStyles.put(type, lp);
        }
        return lp;
    }
    
    void updatePreferences() {
        for (LabelStyle s : _labelStyles.values()) {
            Config.set("block.label." + s.getName() + ".fontSpec",      s.getFont());
            Config.set("block.label." + s.getName() + ".text.color",    s.getTextColor());
            Config.set("block.label." + s.getName() + ".text.colorAlt", s.getTextAltColor());
            Config.set("block.label." + s.getName() + ".margin.top",    s.getMarginTop());
            Config.set("block.label." + s.getName() + ".margin.left",   s.getMarginLeft());
            Config.set("block.label." + s.getName() + ".margin.right",  s.getMarginRight());
            Config.set("block.label." + s.getName() + ".margin.bottom",   s.getMarginBottom());
            Config.set("block.label." + s.getName() + ".frame.thickness", s.getFrameThickness());
            Config.set("block.label." + s.getName() + ".frame.color",     s.getFrameColor());
            Config.set("block.label." + s.getName() + ".frame.isDashed",  s.isFrameDashed());
        }
    }
    
    void updateSelf() {
        
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
        return createLabel(LIST_LBRACKET_TEXT, getLabelStyle("list"), panel);
    }

    public Label createListRBracketLabel(BlockPanel panel) {
        return createLabel(LIST_RBRACKET_TEXT, getLabelStyle("list"), panel);
    }

    public Label createListSeparatorLabel(BlockPanel panel) {
        return createLabel(LIST_SEPARATOR_TEXT, getLabelStyle("list"), panel);
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
