/*
 *  $Id: $
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

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Martin
 */
public final class IneqsAndResidue {
    public final static IneqsAndResidue EMPTY = new IneqsAndResidue();
    
    List<IRelation> _ineqs, _residue;

    public IneqsAndResidue() {
        this(null, null);
    }
    public IneqsAndResidue(List<IRelation> ineqs, List<IRelation> residue) {
        if (ineqs == null)
            ineqs = Collections.EMPTY_LIST;
        if (residue == null)
            residue = Collections.EMPTY_LIST;
        _ineqs = ineqs;
        _residue = residue;
    }

    public List<IRelation> ineqs() { return _ineqs; }
    public List<IRelation> residue() { return _residue; }
}
