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

public abstract class BlockLayout {
    private String _name;
    private int _leading, _intra, _trailing;
    
    abstract void layoutChildrenOfBlock(ContainerBlock block);
    abstract void updateBlockSize(ContainerBlock block);

    public int getLeadingSpace() {
        return _leading;
    }

    public int getIntraSpace() {
        return _intra;
    }
    
    public int getTrailingSpace() {
        return _trailing;
    }

    public void setAll(int leading, int intra, int trailing) {
        _leading = leading;
        _intra = intra;
        _trailing = trailing;
    }

    public void setIntra(int intra) {
        _intra = intra;
    }

    public void setLeading(int leading) {
        _leading = leading;
    }

    public void setTrailing(int trailing) {
        _trailing = trailing;
    }
    
    public String getName() {
        return _name;
    }
    
    void setName(String name) {
        assert _name == null;
        _name = name;
    }
}
