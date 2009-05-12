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
    
    BlockPanel _panel;
    LabelFactory _labfac;
    Map<IEntity, ContentCreator> _contentCreatorCache;
    
    public BlockCreator(BlockPanel panel) {
        _panel = panel;
        _labfac = _panel.getStyle().getLabelFactory();
    }

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
    public void visit(ITypedFeatureStructure tfs) {
        if (tfs.isSpecies()) {
            _result = _labfac.createSpeciesLabel(tfs.typeName(), _panel);
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

        NodeBlock nodeBlock = new NodeBlock(_panel, label, content, childNodes);
        nodeBlock.setModel(u);
        for (NodeBlock childNode : childNodes)
            childNode.setParentNode(nodeBlock);
        return nodeBlock;
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
