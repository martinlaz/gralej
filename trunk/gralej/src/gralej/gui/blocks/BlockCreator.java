package gralej.gui.blocks;

import gralej.om.AbstractVisitor;
import gralej.om.IAny;
import gralej.om.IEntity;
import gralej.om.IFeatureValuePair;
import gralej.om.IList;
import gralej.om.ITag;
import gralej.om.ITree;
import gralej.om.ITypedFeatureStructure;
import gralej.om.IVisitable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BlockCreator extends AbstractVisitor {
    Block _result;
    LabelFactory _labfac = LabelFactory.getInstance();
    Map<IEntity, IContentCreator> _contentCreatorCache;

    public Block createBlock(IVisitable vob) {
        vob.accept(this);
        return _result;
    }

    @Override
    public void visit(IList ls) {
        List<Block> ll = new LinkedList<Block>();

        for (IEntity entity : ls.elements()) {
            entity.accept(this);
            ll.add(_result);
        }

        _result = new ListBlock(new ListContentBlock(ll));
    }

    @Override
    public void visit(ITag tag) {
        _result = new ReentrancyBlock(tag.number(), getContentCreator(tag
                .target()));
    }

    @Override
    public void visit(IAny any) {
        _result = _labfac.createAnyLabel(any.value());
    }

    @Override
    public void visit(ITypedFeatureStructure tfs) {
        if (tfs.isSpecies()) {
            _result = _labfac.createSpeciesLabel(tfs.typeName());
            return;
        }

        List<AVPairBlock> ll = new LinkedList<AVPairBlock>();

        for (IFeatureValuePair featVal : tfs.featureValuePairs()) {
            Label alab = _labfac.createAttributeLabel(featVal.feature()
                    .toUpperCase());
            featVal.value().accept(this);
            ll.add(new AVPairBlock(alab, _result));
        }

        _result = new AVMBlock(_labfac.createSortLabel(tfs.typeName()),
                new AVPairListBlock(ll));
    }

    @Override
    public void visit(ITree tree) {
        _result = new TreeBlock(createTree(tree));
    }

    private NodeBlock createTree(ITree u) {
        Label label;
        java.util.List<NodeBlock> childNodes = new LinkedList<NodeBlock>();
        if (u.isLeaf())
            label = _labfac.createLeafNodeLabel(u.label());
        else {
            for (ITree v : u.children())
                childNodes.add(createTree(v));
            label = _labfac.createInternalNodeLabel(u.label());
        }

        u.content().accept(this);
        Block content = _result;

        return new NodeBlock(label, content, childNodes);
    }

    private IContentCreator getContentCreator(final IEntity entity) {
        if (_contentCreatorCache == null)
            _contentCreatorCache = new HashMap<IEntity, IContentCreator>();
        IContentCreator cc = _contentCreatorCache.get(entity);
        if (cc == null) {
            cc = new IContentCreator() {
                BlockCreator bcv;

                public Block createContent() {
                    if (bcv == null)
                        bcv = new BlockCreator();
                    return bcv.createBlock(entity);
                }
            };
            _contentCreatorCache.put(entity, cc);
        }
        return cc;
    }
}
