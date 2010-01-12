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

import gralej.om.lrs.ILRSExpr;

public abstract class AbstractVisitor implements IVisitor {
    public void visit(IVisitable visitable) {
        throw new RuntimeException("unknown visitable: " + visitable);
    }

    public void visit(IEntity entity) {
        throw new RuntimeException("unknown entity: " + entity);
    }

    public abstract void visit(IList ls);

    public abstract void visit(ITag tag);

    public abstract void visit(IAny any);

    public abstract void visit(ITypedFeatureStructure tfs);

    public abstract void visit(ITree tree);

    public void visit(ITable table) {}

    public void visit(IRelation rel) {}

    public void visit(ILRSExpr expr) {}

    // most classes won't need to implement these two methods
    public void visit(IFeatureValuePair featVal) { }
    public void visit(IType type) { }
}
