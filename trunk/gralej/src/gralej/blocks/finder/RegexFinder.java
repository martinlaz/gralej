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

package gralej.blocks.finder;

import gralej.blocks.Label;
import java.util.regex.Pattern;

/**
 *
 * @author Martin
 */
class RegexFinder extends Finder {
    
    private Pattern _pat;
    
    RegexFinder(String regex, boolean caseSensitive) {
        if (caseSensitive)
            _pat = Pattern.compile(regex);
        else
            _pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected boolean matches(Label label) {
        return _pat.matcher(label.getText()).matches();
    }

}
