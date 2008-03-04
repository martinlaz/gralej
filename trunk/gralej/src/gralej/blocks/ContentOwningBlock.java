package gralej.blocks;

public abstract class ContentOwningBlock
    extends ContainerBlock
    implements ContentOwner
{
    protected Block _content;

    protected void setContent(Block content) {
        _content = content;
    }

    public Block getContent() {
        return _content;
    }
}
