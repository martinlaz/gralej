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

    public abstract IAny createAny(String value);
    public abstract IList createList();
    public abstract IList createList(List<IEntity> elements);

    public abstract IFeatureValuePair createFeatVal(String feat, IEntity val);

    public abstract IRelation createRelation(String name, int arity);
    public abstract IRelation createRelation(String name, List<IEntity> args);

    public abstract ITag createTag(int number);
    public abstract ITag createTag(int number, IEntity target);

    public abstract ITree createTree(String label);
    public abstract ITree createTree(String label, List<ITree> children);
    
    public abstract IType createType(String typeName);

    public abstract ITypedFeatureStructure createTFS(IType type);
    public abstract ITypedFeatureStructure createTFS(IType type, List<IFeatureValuePair> featVals);

    public abstract ILRSExpr createLRSExpr(String expr);
}
