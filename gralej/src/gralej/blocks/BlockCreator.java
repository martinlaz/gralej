/*
 *  $Id$
 *
 *  Author:
 *     Martin Lazarov [mlazarov at sfs.uni-tuebingen.de]
 *     
 *  This file is part of the Gralej system
 *     http://code.google.com/p/gralej/
 *
 *  Gralej is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gralej is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package gralej.blocks;

import gralej.om.AbstractVisitor;
import gralej.om.IAny;
import gralej.om.IEntity;
import gralej.om.IFeatureValuePair;
import gralej.om.IList;
import gralej.om.IRelation;
import gralej.om.ITag;
import gralej.om.ITree;
import gralej.om.ITypedFeatureStructure;
import gralej.om.IVisitable;

import gralej.om.lrs.IExCont;
import gralej.om.lrs.IFunctor;
import gralej.om.lrs.IInCont;
import gralej.om.lrs.ILRSExpr;
import gralej.om.lrs.IMetaVar;
import gralej.om.lrs.INamedTerm;
import gralej.om.lrs.ISqCont;
import gralej.om.lrs.ITerm;
import gralej.om.lrs.IVar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BlockCreator extends AbstractVisitor {
    Block _result;
    
    BlockPanel _panel;
    LabelFactory _labfac;
    Map<IEntity, ContentCreator> _contentCreatorCache;
    
    public BlockCreator(BlockPanel panel) {
        _panel = panel;
        _labfac = _panel.getStyle().getLabelFactory();
    }

    public Block createBlock(IVisitable vob) {
        return createBlock(vob, null);
    }

    public Block createBlock(IVisitable vob, List<IRelation> residue) {
        vob.accept(this);
        if (residue != null && !residue.isEmpty()) {
            VerticalListBlock vl = new VerticalListBlock(_panel);
            vl.addChild(_result);

            vl.addChild(_labfac.createRelationNameLabel(" ", _panel));

            ContentLabel residueSwitchLabel = _labfac.createRelationNameLabel("--", _panel);
            vl.addChild(residueSwitchLabel);

            VerticalListBlock relList = new VerticalListBlock(_panel);
            for (IRelation rel : residue) {
                visit(rel);
                relList.addChild(_result);
            }
            relList.sealChildren();

            vl.addChild(relList);
            vl.sealChildren();

            residueSwitchLabel.setContent(relList);

            _result = vl;
        }
        return _result;
    }

    @Override
    public void visit(IList ls) {
        List<Block> ll = new LinkedList<Block>();

        for (IEntity entity : ls.elements()) {
            entity.accept(this);
            ll.add(_result);
        }
        
        Block tail = null;
        if (ls.tail() != null) {
            ls.tail().accept(this);
            tail = _result;
        }

        _result = new ListBlock(_panel, new ListContentBlock(_panel, ll, tail));
        _result.setModel(ls);
    }

    @Override
    public void visit(ITag tag) {
        if (tag.target() != null) {
            ContentLabel lab = _labfac.createTagLabel(tag.number() + "", _panel);
            lab.setModel(tag);
            _result = new ReentrancyBlock(
                    _panel,
                    lab,
                    tag.number(),
                    getContentCreator(tag.target()));
        }
        else {
            Label lab = _labfac.createUnboundVarLabel(tag.number() + "", _panel);
            lab.setModel(tag);
            final Label unboundVarLabel = lab;
            _result = new ContainerBlock() {
                {
                    setPanel(_panel);
                    setLayout(LayoutFactory.getInstance().getReentrancyLayout());
                    addChild(unboundVarLabel);
                }
            };
        }
    }

    @Override
    public void visit(IAny any) {
        _result = _labfac.createAnyLabel(any.value(), _panel);
        _result.setModel(any);
    }

    @Override
    public void visit(ILRSExpr lrs) {
        LRSTreeBlock lrsTree = new LRSTreeBlock(_panel, createLRSTree(lrs.subTerms()));
        lrsTree.setModel(lrs);

        _result = new LRSBlock(_panel, lrsTree);
        _result.setModel(lrs);
    }

    private LRSNodeBlock createLRSTree(List<ITerm> terms) {
        List<NodeBlock> childNodes = new LinkedList<NodeBlock>();
        List<Block> labels = new LinkedList<Block>();
        return createLRSNode(terms, labels, childNodes);
    }

    private LRSNodeBlock createLRSNode(List<Block> labels, List<NodeBlock> childNodes) {
        return new LRSNodeBlock(_panel, labels, childNodes);
    }

    private LRSNodeBlock createLRSNode(ITerm term) {
        List<Block> labels = new LinkedList<Block>();
        List<NodeBlock> childNodes = new LinkedList<NodeBlock>();
        processLRSTerm(term, labels, childNodes);
        return createLRSNode(labels, childNodes);
    }

    private LRSNodeBlock createLRSNode(List<ITerm> terms, List<Block> labels, List<NodeBlock> childNodes) {
        for (ITerm term : terms) {
            if (!labels.isEmpty())
                labels.add(_labfac.createLRSLabel(",", _panel));
            processLRSTerm(term, labels, childNodes);
        }
        return createLRSNode(labels, childNodes);
    }

    private void processLRSTerm(ITerm term, List<Block> labels, List<NodeBlock> childNodes) {
        if (term instanceof INamedTerm) {
            if (term.isLeafTerm())
                labels.add(_labfac.createLRSLabel(term.uiName(), _panel));
            else {
                LRSContentLabel lab = _labfac.createLRSContentLabel(term.uiName(), _panel);
                labels.add(lab);
                for (ITerm subTerm : term.subTerms()) {
                    LRSNodeBlock node = createLRSNode(subTerm);
                    node.setParentLabel(lab);
                    childNodes.add(node);
                    lab.addChildNode(node);
                }
            }   
        }

        if (term instanceof IFunctor) {
            labels.add(_labfac.createLRSLabel("(", _panel));

            boolean firstDone = false;
            for (ITerm arg : ((IFunctor) term).args()) {
                if (firstDone)
                    labels.add(_labfac.createLRSLabel(",", _panel));
                else
                    firstDone = true;
                processLRSTerm(arg, labels, childNodes);
            }

            labels.add(_labfac.createLRSLabel(")", _panel));
        }
        else if (term instanceof IMetaVar) {
            // name done
        }
        else if (term instanceof IVar) {
            // name done
        }
        else if (term instanceof IInCont) {
            labels.add(_labfac.createLRSLabel("{", _panel));
            for (ITerm subTerm : term.subTerms())
                processLRSTerm(subTerm, labels, childNodes);
            labels.add(_labfac.createLRSLabel("}", _panel));
        }
        else if (term instanceof IExCont) {
            labels.add(_labfac.createLRSLabel("^", _panel));
            for (ITerm subTerm : term.subTerms())
                processLRSTerm(subTerm, labels, childNodes);
        }
        else if (term instanceof ISqCont) {
            labels.add(_labfac.createLRSLabel("[", _panel));
            for (ITerm subTerm : term.subTerms())
                processLRSTerm(subTerm, labels, childNodes);
            labels.add(_labfac.createLRSLabel("]", _panel));
        }
        else {
            throw new AssertionError("unknown lrs term: " + term + " [" + term.getClass() + "]");
        }
        if (term.hasConstraints()) {
            labels.add(_labfac.createLRSLabel("/", _panel));
                labels.add(_labfac.createLRSLabel("(", _panel));
                processConstraints(labels, term.positiveConstraints());
                labels.add(_labfac.createLRSLabel(")~(", _panel));
                processConstraints(labels, term.negativeConstraints());
                labels.add(_labfac.createLRSLabel(")", _panel));
        }
    }

    private void processConstraints(Collection<Block> labels, Iterable<ITag> tags) {
        boolean comma = false;
        for (ITag tag : tags) {
            if (comma)
                labels.add(_labfac.createLRSLabel(",", _panel));
            else
                comma = true;
            if (tag.number() != -1) {
                tag.accept(this);
                labels.add(_result);
            }
            else {
                labels.add(_labfac.createLRSLabel("[*]", _panel));
            }
        }
    }

    @Override
    public void visit(ITypedFeatureStructure tfs) {
        if (tfs.isSpecies()) {
            _result = _labfac.createSpeciesLabel(tfs.typeName(), _panel);
            if (tfs.isDifferent())
                tfs.type().setDifferent(true);
            _result.setModel(tfs.type());
            return;
        }

        List<AVPairBlock> ll = new LinkedList<AVPairBlock>();

        for (IFeatureValuePair featVal : tfs.featureValuePairs()) {
            ContentLabel alab = _labfac.createAttributeLabel(
                    featVal.feature().toUpperCase(), _panel);
            alab.setModel(featVal);
            featVal.value().accept(this);
            ll.add(new AVPairBlock(_panel, alab, _result, featVal.isHidden()));
            if (featVal.isHidden())
                alab.flip();
        }

        ContentLabel sortLabel = _labfac.createSortLabel(tfs.typeName(), _panel);
        sortLabel.setModel(tfs.type());
        _result = new AVMBlock(
                _panel,
                sortLabel,
                new AVPairListBlock(_panel, ll)
                );
        _result.setModel(tfs);
    }

    @Override
    public void visit(ITree tree) {
        _result = new TreeBlock(_panel, createTree(tree));
        _result.setModel(tree);
    }

    private NodeBlock createTree(ITree u) {
        Label label;
        java.util.List<NodeBlock> childNodes = new LinkedList<NodeBlock>();
        if (u.isLeaf())
            label = _labfac.createLeafNodeLabel(u.label(), _panel);
        else {
            for (ITree v : u.children())
                childNodes.add(createTree(v));
            label = _labfac.createInternalNodeLabel(u.label(), _panel);
        }
        label.setModel(u);

        u.content().accept(this);
        Block content = _result;

        NodeBlock nodeBlock = new AVMNodeBlock(_panel, label, content, childNodes);
        nodeBlock.setModel(u);
        for (NodeBlock childNode : childNodes)
            childNode.setParentNode(nodeBlock);
        return nodeBlock;
    }

    @Override
    public void visit(IRelation rel) {
        List<Block> argBlocks = new LinkedList<Block>();
        for (IEntity arg : rel.args()) {
            arg.accept(this);
            argBlocks.add(_result);
        }
        _result = new RelationBlock(_panel, rel.name(), argBlocks);
        _result.setModel(rel);
    }

    private ContentCreator getContentCreator(final IEntity entity) {
        if (_contentCreatorCache == null)
            _contentCreatorCache = new HashMap<IEntity, ContentCreator>();
        ContentCreator cc = _contentCreatorCache.get(entity);
        if (cc == null) {
            cc = new ContentCreator() {
                BlockCreator bcv;

                public Block createContent() {
                    if (bcv == null)
                        bcv = new BlockCreator(_panel);
                    return bcv.createBlock(entity);
                }
            };
            _contentCreatorCache.put(entity, cc);
        }
        return cc;
    }
}
