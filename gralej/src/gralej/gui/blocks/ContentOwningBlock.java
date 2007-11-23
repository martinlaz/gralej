package gralej.gui.blocks;

abstract class ContentOwningBlock
    extends ContainerBlock
    implements IContentOwner 
{
    protected IBlock _content;
    
    ContentOwningBlock(IBlock parent)
        { super(parent); }
    
    protected void setContent(IBlock content) {
        _content = content;
    }
    
    public IBlock getContent() {
        return _content;
    }
}
