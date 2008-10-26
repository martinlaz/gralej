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

public class OM2XMLVisitor extends AbstractVisitor {
    StringBuffer _out;
    // PrintWriter _out;
    Set<Integer> _reentrancies = new TreeSet<Integer>();

    // OM2XMLVisitor(PrintWriter out) {
    // _out = out;
    // }

    OM2XMLVisitor() {
        // this(new PrintWriter(System.out, true));
    }

    public String output(IVisitable root) {
        _out = new StringBuffer();
        root.accept(this);
        return _out.toString();
    }

    public void visit(IVisitable visitable) {
        throw new RuntimeException("unknown visitable: " + visitable);
    }

    public void visit(IEntity entity) {
        throw new RuntimeException("unknown entity: " + entity);
    }

    public void visit(IFeatureValuePair featVal) {
        _out.append("<f name='" + featVal.feature() + "'>\n");
        featVal.value().accept(this);
        _out.append("</f>\n");
    }

    public void visit(IList ls) {
        _out.append("<ls>\n");
        for (IEntity e : ls.elements())
            e.accept(this);
        _out.append("</ls>\n");
    }

    public void visit(ITag tag) {
        _out.append("<tag ");
        if (_reentrancies.add(tag.number())) {
            _out.append("id='" + tag.number() + "'>");
            tag.target().accept(this);
            _out.append("</tag>\n");
        } else {
            _out.append("ref='" + tag.number() + "'/>\n");
        }
    }

    public void visit(IAny any) {
        _out.append("<any>" + any.value() + "</any>\n");
    }

    public void visit(ITypedFeatureStructure tfs) {
        _out.append("<tfs type='" + tfs.typeName() + "'>\n");
        for (IFeatureValuePair featVal : tfs.featureValuePairs())
            featVal.accept(this);
        _out.append("</tfs>\n");
    }

    public void visit(ITree tree) {
        _out.append("<tree label='" + tree.label() + "'>\n");
        _out.append("<content>\n");
        tree.content().accept(this);
        _out.append("</content>\n");
        for (ITree child : tree.children())
            child.accept(this);
        _out.append("</tree>\n");

    }
}
