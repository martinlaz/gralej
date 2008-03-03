/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.blocks;

/**
 *
 * @author Martin
 */
public class RootBlock extends ContentOwningBlock {
    RootBlock(BlockPanel panel, Block content) {
        setPanel(panel);
        setLayout(new HorizontalBlockLayout());
        addChild(content);
        setContent(content);
    }
    
    @Override
    protected void updateParent() {
        getPanel().updateSelf();
    }
    
    @Override
    public Block getDescendant(String path) {
        path = path.trim();
        if (path.startsWith("/"))
            path = path.substring(1);
        return super.getDescendant(path);
    }
}
