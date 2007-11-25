package gralej.gui.blocks;

import gralej.om.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BlockCreatingVisitor extends AbstractVisitor {
    IBlock _parent;
    IBlock _result;
    LabelFactory _labfac = LabelFactory.getInstance();
    Map<IEntity,IContentCreator> _contentCreatorCache;
    Tree _tree;
    
    public IBlock createBlock(IBlock parent, IVisitable vob) {
        _parent = parent;
        vob.accept(this);
        return _result;
    }
    
    @Override
    public void visit(IList ls) {
        List lsBlock = new List(_parent);
        ListContent lsContentBlock = new ListContent(lsBlock);
        LinkedList<IBlock> ll = new LinkedList<IBlock>();
        
        for (IEntity entity : ls.elements()) {
            _parent = lsContentBlock;
            entity.accept(this);
            ll.add(_result);
        }
        
        lsContentBlock.init(ll);
        lsBlock.init(lsContentBlock);
        _result = lsBlock;
    }
    
    @Override
    public void visit(ITag tag) {
        Reentrancy r = new Reentrancy(_parent, tag.number());
        r.init(getContentCreator(tag.target()));
        _result = r;
    }
    
    @Override
    public void visit(IAny any) {
        _result = new Any(_parent, any.value());
    }
    
    @Override
    public void visit(ITypedFeatureStructure tfs) {
        if (tfs.isSpecies()) {
            _result = _labfac.createSpeciesLabel(tfs.typeName(), _parent);
            return;
        }
        
        AVM avm = new AVM(_parent);
        AVPairList avPairs = new AVPairList(avm);
        java.util.List<AVPair> avls = new LinkedList<AVPair>();
        
        for (IFeatureValuePair featVal : tfs.featureValuePairs()) {
            AVPair av = new AVPair(avPairs);
            Label alab = _labfac.createAttributeLabel(
                    featVal.feature().toUpperCase(), av);
            _parent = av;
            featVal.value().accept(this);
            av.init(alab, _result);
            avls.add(av);
        }
        
        avPairs.init(avls);
        
        Label sortLabel;
        
        if (avls.isEmpty())
            sortLabel = _labfac.createSpeciesLabel(tfs.typeName(), avm);
        else
            sortLabel = _labfac.createSortLabel(tfs.typeName(), avm);
        
        avm.init(sortLabel, avPairs);
        
        _result = avm;
    }
    
    @Override
    public void visit(ITree tree) {
        _tree = new Tree(_parent);
        Node root = createTree(tree);
        _tree.init(root);
        _result = _tree;
    }
    
    private Node createTree(ITree u) {
        Node node = new Node(_tree);
        java.util.List<Node> childNodes = new LinkedList<Node>();
        Label label;
        if (u.isLeaf())
            label = _labfac.createLeafNodeLabel(u.label(), node);
        else {
            for (ITree v : u.children())
                childNodes.add(createTree(v));
            label = _labfac.createInternalNodeLabel(u.label(), node);
        }
        
        _parent = node;
        u.content().accept(this);
        IBlock content = _result;
        
        node.init(label, content, childNodes);
        return node;
    }
    
    private IContentCreator getContentCreator(final IEntity entity) {
        if (_contentCreatorCache == null)
            _contentCreatorCache = new HashMap<IEntity,IContentCreator>();
        IContentCreator cc = _contentCreatorCache.get(entity);
        if (cc == null) {
            cc = new IContentCreator() {
                BlockCreatingVisitor bcv;
                public IBlock createContent(IBlock parent) {
                    if (bcv == null)
                        bcv = new BlockCreatingVisitor();
                    return bcv.createBlock(parent, entity);
                }
            };
            _contentCreatorCache.put(entity, cc);
        }
        return cc;
    }
}
