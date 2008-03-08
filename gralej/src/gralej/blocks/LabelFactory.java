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
        ls.setFont(Config.getFont(type + ".fontSpec",
                    DEFAULT_FONT_SPEC));
        ls.setTextColor(Config.getColor(type + ".text.color",
                DEFAULT_TEXT_COLOR));
        ls.setTextAltColor(Config.getColor(type + ".text.colorAlt",
                DEFAULT_TEXT_ALT_COLOR));
        ls.setMarginTop(Config.getInt(type + ".margin.top", DEFAULT_MARGIN_TOP));
        ls.setMarginLeft(Config.getInt(type + ".margin.left", DEFAULT_MARGIN_LEFT));
        ls.setMarginRight(Config.getInt(type + ".margin.right", DEFAULT_MARGIN_RIGHT));
        ls.setMarginBottom(Config.getInt(type + ".margin.bottom", DEFAULT_MARGIN_BOTTOM));
        ls.setFrameThickness(Config.getInt(type + ".frame.thickness", DEFAULT_FRAME_THICKNESS));
        ls.setFrameColor(Config.getColor(type + ".frame.color", DEFAULT_FRAME_COLOR));
        ls.setFrameDashed(Boolean.parseBoolean(Config.get(type + ".frame.isDashed", DEFAULT_FRAME_IS_DASHED)));
    }
    
    void updatePreferences() {
        for (LabelStyle s : _labelStyles.values()) {
            //<entry key="log.message.info"/>
            /*
            System.err.println("<entry key=\"block.label." + s.getName() + ".fontSpec\"/>");
            System.err.println("<entry key=\"block.label." + s.getName() + ".text.color\"/>");
            System.err.println("<entry key=\"block.label." + s.getName() + ".text.colorAlt\"/>");
            System.err.println("<entry key=\"block.label." + s.getName() + ".margin.top\"/>");
            System.err.println("<entry key=\"block.label." + s.getName() + ".margin.left\"/>");
            System.err.println("<entry key=\"block.label." + s.getName() + ".margin.right\"/>");
            System.err.println("<entry key=\"block.label." + s.getName() + ".margin.bottom\"/>");
            System.err.println("<entry key=\"block.label." + s.getName() + ".frame.thickness\"/>");
            System.err.println("<entry key=\"block.label." + s.getName() + ".frame.color\"/>");
            System.err.println("<entry key=\"block.label." + s.getName() + ".frame.isDashed\"/>");
            */
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
