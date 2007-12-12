package gralej.gui.blocks;

class ReentrancyBlock extends ContentOwningBlock {
    IContentCreator _contentCreator;

    ReentrancyBlock(int tag, IContentCreator contentCreator) {
        setLayout(LayoutFactory.getReentrancyLayout());

        Label tagLabel = LabelFactory.getInstance().createTagLabel(
                Integer.toString(tag));
        addChild(tagLabel);

        _contentCreator = contentCreator;
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
