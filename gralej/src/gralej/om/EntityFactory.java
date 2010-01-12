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
import java.util.List;

/**
 *
 * @author Martin
 */
public abstract class EntityFactory {
    private static EntityFactory _INSTANCE;

    public static EntityFactory getInstance() {
        if (_INSTANCE == null)
            _INSTANCE = new gralej.parsers.EntityFactory();
        return _INSTANCE;
    }
    public static void setInstance(EntityFactory fac) {
        _INSTANCE = fac;
    }

    public abstract IAny newAny(String value);
    public abstract IList newList();
    public abstract IList newList(List<IEntity> elements);

    public abstract IFeatureValuePair newFeatVal(String feat, IEntity val);

    public abstract IRelation newRelation(String name, int arity);
    public abstract IRelation newRelation(String name, List<IEntity> args);

    public abstract ITag newTag(int number);
    public abstract ITag newTag(int number, IEntity target);

    public abstract ITree newTree(String label);
    public abstract ITree newTree(String label, List<ITree> children);
    
    public abstract IType newType(String typeName);

    public abstract ITypedFeatureStructure newTFS(IType type);
    public abstract ITypedFeatureStructure newTFS(IType type, List<IFeatureValuePair> featVals);

    public abstract ILRSExpr newLRSExpr(String expr);
}
