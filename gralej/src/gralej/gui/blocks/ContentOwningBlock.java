package gralej.gui.blocks;

abstract class ContentOwningBlock extends ContainerBlock implements
        IContentOwner {
    protected Block _content;

    protected void setContent(Block content) {
        _content = content;
    }

    public IBlock getContent() {
        return _content;
    }
}
