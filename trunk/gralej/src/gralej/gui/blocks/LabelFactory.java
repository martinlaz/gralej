package gralej.gui.blocks;

import gralej.prefs.GPrefsChangeListener;
import gralej.prefs.GralePreferences;
import java.awt.Color;
import java.awt.Font;
import java.util.Map;
import java.util.TreeMap;

final class LabelFactory {
    
    class LabelParams {
        Font font;
        Color textColor;
        Color textAltColor;
        int hm;
        int vm;
        int frameWidth;
        Color frameColor;
    }

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

    Map<String, LabelParams> _cachedLabelParams = new TreeMap<String, LabelParams>();

    static private LabelFactory _instance;
    
    private LabelFactory() {}

    static LabelFactory getInstance() {
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
        _cachedLabelParams.clear();
    }

    synchronized LabelParams getLabelParams(String type) {
        LabelParams lp = _cachedLabelParams.get(type);
        if (lp == null) {
            lp = new LabelParams();
            lp.font = Config.getFont(type + ".font.spec",
                    DEFAULT_FONT_SPEC);
            lp.textColor = Config.getColor(type + ".text.color",
                    DEFAULT_TEXT_COLOR);
            lp.textAltColor = Config.getColor(type + ".text.color.alt",
                    DEFAULT_TEXT_ALT_COLOR);
            lp.hm = Config.getInt(type + ".margin.horizontal", DEFAULT_HM);
            lp.vm = Config.getInt(type + ".margin.vertical", DEFAULT_VM);
            lp.frameWidth = Config.getInt(type + ".frame.width",
                    DEFAULT_FRAME_WIDTH);
            lp.frameColor = Config.getColor(type + ".frame.color",
                    DEFAULT_FRAME_COLOR);
            _cachedLabelParams.put(type, lp);
        }
        return lp;
    }

    ContentLabel createContentLabel(String text, LabelParams params) {
        return new ContentLabel(text, params.font, params.textColor,
                params.textAltColor, params.hm, params.vm, params.frameWidth,
                params.frameColor);
    }

    Label createLabel(String text, LabelParams params) {
        return new Label(text, params.font, params.textColor, params.hm,
                params.vm, params.frameWidth, params.frameColor);
    }

    ContentLabel createTagLabel(String text) {
        return createContentLabel(text, getLabelParams("label.tag"));
    }

    ContentLabel createSortLabel(String text) {
        return createContentLabel(text, getLabelParams("label.sort"));
    }

    ContentLabel createAttributeLabel(String text) {
        return createContentLabel(text, getLabelParams("label.attribute"));
    }

    Label createListLBracketLabel() {
        return createLabel(LIST_LBRACKET_TEXT, getLabelParams("label.list"));
    }

    Label createListRBracketLabel() {
        return createLabel(LIST_RBRACKET_TEXT, getLabelParams("label.list"));
    }

    Label createListSeparatorLabel() {
        return createLabel(LIST_SEPARATOR_TEXT, getLabelParams("label.list"));
    }

    Label createAnyLabel(String text) {
        return createLabel(text, getLabelParams("label.any"));
    }

    Label createSpeciesLabel(String text) {
        return createLabel(text, getLabelParams("label.species"));
    }

    Label createInternalNodeLabel(String text) {
        return createContentLabel(text, getLabelParams("label.node.internal"));
    }

    Label createLeafNodeLabel(String text) {
        return createContentLabel(text, getLabelParams("label.node.leaf"));
    }
}
