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
    
    public IBlock createBlock(IBlock parent, IVisitable vob) {
        _parent = parent;
        vob.accept(this);
        return _result;
    }
    
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
    public void visit(ITag tag) {
        Reentrancy r = new Reentrancy(_parent, tag.number());
        r.init(getContentCreator(tag.target()));
        _result = r;
    }
    
    public void visit(IAny any) {
        _result = new Species(_parent, any.value());
    }
    
    public void visit(ITypedFeatureStructure tfs) {
        AVM avm = new AVM(_parent);
        Label sortLabel = _labfac.createSortLabel(tfs.typeName(), avm);
        AVPairList avPairs = new AVPairList(avm);
        
        for (IFeatureValuePair featVal : tfs.featureValuePairs()) {
            AVPair av = new AVPair(avPairs);
            Label alab = _labfac.createAttributeLabel(
                    featVal.feature().toUpperCase(), av);
            _parent = av;
            featVal.value().accept(this);
            av.init(alab, _result);
            avPairs.addChild(av);
        }
        
        avm.init(sortLabel, avPairs);
        _result = avm;
    }
    
    public void visit(ITree tree) {
        throw new UnsupportedOperationException("visit tree");
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
