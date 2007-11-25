package gralej.gui.blocks;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;
import java.util.TreeMap;

class LabelFactory {
    class LabelParams {
        Font font;
        Color textColor;
        Color textAltColor;
        int hm;
        int vm;
        int frameWidth;
        Color frameColor;
    }
    
    final static String DEFAULT_FONT_SPEC   = Config.get("label.font.spec");
    final static String DEFAULT_TEXT_COLOR  = Config.get("label.text.color");
    final static String DEFAULT_TEXT_ALT_COLOR  = Config.get("label.text.color.alt");
    final static int DEFAULT_HM             = Config.getInt("label.margin.horizontal");
    final static int DEFAULT_VM             = Config.getInt("label.margin.vertical");
    final static int DEFAULT_FRAME_WIDTH    = Config.getInt("label.frame.width");
    final static String DEFAULT_FRAME_COLOR = Config.get("label.frame.color");
    
    final static String LIST_LBRACKET_TEXT  = Config.get("label.list.text.left");
    final static String LIST_RBRACKET_TEXT  = Config.get("label.list.text.right");
    final static String LIST_SEPARATOR_TEXT  = Config.get("label.list.text.separator");
    
    Map<String,LabelParams> _cachedLabelParams = new 
        TreeMap<String,LabelParams>();
    
    static LabelFactory _globalFactory;
    
    static LabelFactory getInstance() {
        if (_globalFactory == null)
            _globalFactory = new LabelFactory();
        return _globalFactory;
    }
    
    void clearCache() {
        _cachedLabelParams.clear();
    }
    
    LabelParams getLabelParams(String type) {
        LabelParams lp = _cachedLabelParams.get(type);
        if (lp == null) {
            lp = new LabelParams();
            lp.font = Font.decode(Config.get(type + ".font.spec", DEFAULT_FONT_SPEC));
            lp.textColor = Color.decode(Config.get(type + ".text.color", DEFAULT_TEXT_COLOR));
            lp.textAltColor = Color.decode(Config.get(type + ".text.color.alt", DEFAULT_TEXT_ALT_COLOR));
            lp.hm = Config.getInt(type + ".margin.horizontal", DEFAULT_HM);
            lp.vm = Config.getInt(type + ".margin.vertical", DEFAULT_VM);
            lp.frameWidth = Config.getInt(type + ".frame.width", DEFAULT_FRAME_WIDTH);
            lp.frameColor = Color.decode(Config.get(type + ".frame.color", DEFAULT_FRAME_COLOR));
        }
        return lp;
    }
    
    ContentLabel createContentLabel(
        String text, ContentOwningBlock parent, LabelParams params
        )
    {
        return new ContentLabel(
            parent,
            text,
            params.font,
            params.textColor,
            params.textAltColor,
            params.hm,
            params.vm,
            params.frameWidth,
            params.frameColor
            );
    }
    
    Label createLabel(
            String text, IBlock parent, LabelParams params
            )
    {
        return new Label(
            parent,
            text,
            params.font,
            params.textColor,
            params.hm,
            params.vm,
            params.frameWidth,
            params.frameColor
            );
    }
    
    ContentLabel createTagLabel(String text, ContentOwningBlock parent) {
        return createContentLabel(
            text,
            parent,
            getLabelParams("label.tag")
            );
    }
    
    ContentLabel createSortLabel(String text, ContentOwningBlock parent) {
        return createContentLabel(
            text,
            parent,
            getLabelParams("label.sort")
            );
    }
    
    ContentLabel createAttributeLabel(String text, ContentOwningBlock parent) {
        return createContentLabel(
            text,
            parent,
            getLabelParams("label.attribute")
            );
    }
    
    Label createListLBracketLabel(ContentOwningBlock parent) {
        return createLabel(
            LIST_LBRACKET_TEXT,
            parent,
            getLabelParams("label.list")
            );
    }
    
    Label createListRBracketLabel(ContentOwningBlock parent) {
        return createLabel(
            LIST_RBRACKET_TEXT,
            parent,
            getLabelParams("label.list")
            );
    }
    
    Label createListSeparatorLabel(IBlock parent) {
        return createLabel(
                LIST_SEPARATOR_TEXT,
                parent,
                getLabelParams("label.list")
                );
    }
    
    Label createAnyLabel(String text, IBlock parent) {
        return createLabel(
            text,
            parent,
            getLabelParams("label.any")
            );
    }
    
    Label createSpeciesLabel(String text, IBlock parent) {
        return createLabel(
            text,
            parent,
            getLabelParams("label.species")
            );
    }
    
    Label createInternalNodeLabel(String text, ContentOwningBlock parent) {
        return createContentLabel(
                text,
                parent,
                getLabelParams("label.node.internal")
                );
    }
    
    Label createLeafNodeLabel(String text, ContentOwningBlock parent) {
        return createContentLabel(
                text,
                parent,
                getLabelParams("label.node.leaf")
                );
    }    
}
