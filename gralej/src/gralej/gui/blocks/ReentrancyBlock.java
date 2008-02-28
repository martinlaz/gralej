package gralej.gui.blocks;

import java.util.Set;
import java.util.TreeSet;

class ReentrancyBlock extends ContentOwningBlock {
    IContentCreator _contentCreator;
    int _tag;

    ReentrancyBlock(int tag, IContentCreator contentCreator) {
        _tag = tag;
        _contentCreator = contentCreator;
    }
    
    @Override
    public void init() {
        setLayout(getPanel().getLayoutFactory().getReentrancyLayout());
        Label tagLabel = getPanel().getLabelFactory().createTagLabel(
                Integer.toString(_tag));
        addChild(tagLabel);
        
        super.init();
        
        if (!getPanel().getAutoExpandTags())
            return;
        if (getPanel().getExpandedTags().add(_tag))
            getContent().setVisible(true);
    }

    Label getTagLabel() {
        return (Label) _children.get(0);
    }

    @Override
    public Block getContent() {
        if (_content == null) {
            _content = _contentCreator.createContent();
            // don't make it visible,
            // the content label will do so
            addChild(_content, false);
            _content.init();
            _content.setVisible(false);
        }
        return _content;
    }
}
