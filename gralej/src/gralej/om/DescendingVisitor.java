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


package gralej.om;

/**
 *
 * @author Martin
 */
public abstract class DescendingVisitor extends AbstractVisitor {
    public void visit(IList ls) {
        for (IEntity e : ls.elements())
            e.accept(this);
        if (ls.tail() != null)
            ls.tail().accept(this);
    }

    public void visit(ITag tag) {
        if (tag.target() != null)
            tag.target().accept(this);
    }

    public void visit(ITypedFeatureStructure tfs) {
        if (tfs.type() != null)
            tfs.type().accept(this);
        for (IFeatureValuePair fv : tfs.featureValuePairs())
            fv.accept(this);
    }

    public void visit(ITree tree) {
        if (tree.content() != null)
            tree.content().accept(this);
        for (ITree t : tree.children())
            t.accept(this);
    }

    public void visit(IRelation rel) {
        for (IEntity arg : rel.args())
            arg.accept(this);
    }

    public void visit(IFeatureValuePair featVal) {
        featVal.value().accept(this);
    }

    public void visit(IAny any) { }
}
