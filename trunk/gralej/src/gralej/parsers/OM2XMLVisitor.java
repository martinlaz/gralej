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

package gralej.parsers;

//import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

import gralej.om.*;
import gralej.om.lrs.ILRSExpr;

public class OM2XMLVisitor extends AbstractVisitor {
    StringBuffer _out;
    Set<Integer> _reentrancies = new TreeSet<Integer>();

    public String output(IVisitable root) {
        _out = new StringBuffer();
        root.accept(this);
        return _out.toString();
    }

    @Override
    public void visit(IVisitable visitable) {
        throw new RuntimeException("unknown visitable: " + visitable);
    }

    @Override
    public void visit(IEntity entity) {
        throw new RuntimeException("unknown entity: " + entity);
    }

    @Override
    public void visit(IFeatureValuePair featVal) {
        _out.append("<f name='").append(featVal.feature()).append("'>\n");
        featVal.value().accept(this);
        _out.append("</f>\n");
    }

    @Override
    public void visit(IList ls) {
        _out.append("<ls>\n");
        for (IEntity e : ls.elements())
            e.accept(this);
        _out.append("</ls>\n");
    }

    @Override
    public void visit(ITag tag) {
        _out.append("<tag ");
        if (_reentrancies.add(tag.number())) {
            _out.append("id='").append(tag.number()).append("'>");
            if (tag.target() != null)
                tag.target().accept(this);
            _out.append("</tag>\n");
        } else {
            _out.append("ref='").append(tag.number()).append("'/>\n");
        }
    }

    @Override
    public void visit(IAny any) {
        _out.append("<any>").append(any.value()).append("</any>\n");
    }

    @Override
    public void visit(ITypedFeatureStructure tfs) {
        _out.append("<tfs type='").append(tfs.typeName()).append("'>\n");
        for (IFeatureValuePair featVal : tfs.featureValuePairs())
            featVal.accept(this);
        _out.append("</tfs>\n");
    }

    @Override
    public void visit(ITree tree) {
        _out.append("<tree label='").append(tree.label()).append("'>\n");
        _out.append("<content>\n");
        tree.content().accept(this);
        _out.append("</content>\n");
        for (ITree child : tree.children())
            child.accept(this);
        _out.append("</tree>\n");
    }

    @Override
    public void visit(ILRSExpr lrs) {
        _out.append("<cllrs>").append(lrs.text()).append("</cllrs>");
    }
}
