package gralej.blocks;

public abstract class BlockLayout {
    private String _name;
    private int _leading, _intra, _trailing;
    
    abstract void layoutChildrenOfBlock(ContainerBlock block);
    abstract void updateBlockSize(ContainerBlock block);

    public int getLeadingSpace() {
        return _leading;
    }

    public int getIntraSpace() {
        return _intra;
    }
    
    public int getTrailingSpace() {
        return _trailing;
    }

    public void setAll(int leading, int intra, int trailing) {
        _leading = leading;
        _intra = intra;
        _trailing = trailing;
    }

    public void setIntra(int intra) {
        _intra = intra;
    }

    public void setLeading(int leading) {
        _leading = leading;
    }

    public void setTrailing(int trailing) {
        _trailing = trailing;
    }
    
    public String getName() {
        return _name;
    }
    
    void setName(String name) {
        assert _name == null;
        _name = name;
    }
}
