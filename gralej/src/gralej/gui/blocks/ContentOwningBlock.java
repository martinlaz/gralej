package gralej.gui.blocks;

abstract class ContentOwningBlock
    extends ContainerBlock
    implements IContentOwner 
{
    ContentOwningBlock(IBlock parent)
        { super(parent); }
}
