package gralej.blocks;

import gralej.prefs.GPrefsChangeListener;
import gralej.prefs.GralePreferences;
import java.util.Map;
import java.util.TreeMap;

public class LabelFactory {

    final String DEFAULT_FONT_SPEC = Config.get("label._default.font.spec");
    final String DEFAULT_TEXT_COLOR = Config.get("label._default.text.color");
    final String DEFAULT_TEXT_ALT_COLOR = Config
            .get("label._default.text.color.alt");
    final int DEFAULT_HM = Config.getInt("label._default.margin.horizontal");
    final int DEFAULT_VM = Config.getInt("label._default.margin.vertical");
    final int DEFAULT_FRAME_WIDTH = Config.getInt("label._default.frame.width");
    final String DEFAULT_FRAME_COLOR = Config.get("label._default.frame.color");

    final String LIST_LBRACKET_TEXT = Config.get("label.list.text.left");
    final String LIST_RBRACKET_TEXT = Config
            .get("label.list.text.right");
    final String LIST_SEPARATOR_TEXT = Config
            .get("label.list.text.separator");

    Map<String, LabelStyle> _cachedLabelStyles = new TreeMap<String, LabelStyle>();

    static private LabelFactory _instance;
    
    public LabelFactory() {}

    public static LabelFactory getInstance() {
        if (_instance == null)
            _instance = new LabelFactory();
        return _instance;
    }
    
    static {
        GPrefsChangeListener l = new GPrefsChangeListener() {
            public void preferencesChange() {
                _instance = null;
            }
        };
        GralePreferences.getInstance().addListener(l, "label.");
    }

    synchronized void clearCache() {
        _cachedLabelStyles.clear();
    }

    synchronized LabelStyle getLabelStyle(String type_) {
        String type = "label." + type_;
        LabelStyle lp = _cachedLabelStyles.get(type);
        if (lp == null) {
            lp = new LabelStyle(type_);
            lp.setFont(Config.getFont(type + ".font.spec",
                    DEFAULT_FONT_SPEC));
            lp.setTextColor(Config.getColor(type + ".text.color",
                    DEFAULT_TEXT_COLOR));
            lp.setTextAltColor(Config.getColor(type + ".text.color.alt",
                    DEFAULT_TEXT_ALT_COLOR));
            int hm = Config.getInt(type + ".margin.horizontal", DEFAULT_HM);
            int vm = Config.getInt(type + ".margin.vertical", DEFAULT_VM);
            lp.setMargins(vm, hm, vm, hm);
            lp.setFrameWidth(Config.getInt(type + ".frame.width",
                    DEFAULT_FRAME_WIDTH));
            lp.setFrameColor(Config.getColor(type + ".frame.color",
                    DEFAULT_FRAME_COLOR));
            _cachedLabelStyles.put(type, lp);
        }
        return lp;
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
