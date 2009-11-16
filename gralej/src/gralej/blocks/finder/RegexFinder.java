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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Martin
 */
class RegexFinder extends Finder {
    
    private Matcher _matcher;
    
    RegexFinder(FinderOptions opts) {
        super(opts);
        if (opts.isCaseSensitive)
            _matcher = Pattern.compile(opts.text).matcher("");
        else
            _matcher = Pattern.compile(opts.text, Pattern.CASE_INSENSITIVE).matcher("");
    }

    @Override
    protected boolean matches(Label label) {
        _matcher.reset(label.getText());
        if (_opts.isCompleteMatch)
            return _matcher.matches();
        return _matcher.find();
    }

}
