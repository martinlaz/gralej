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

import gralej.blocks.AVPairBlock;
import gralej.blocks.Block;
import gralej.blocks.BlockPanel;
import gralej.blocks.ContentLabel;
import gralej.blocks.Label;
import gralej.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 *
 * @author Martin
 */
public abstract class Finder {
    private Stack<Iterator<Block>> _stack = new Stack<Iterator<Block>>();
    private BlockPanel _panel;
    
    static Finder newInstance(FinderOptions opts, BlockPanel panel) {
        Finder f;
        if (opts.isRegex)
            f = new RegexFinder(opts.text, opts.isCaseSensitive);
        else if (opts.isCaseSensitive)
            f = new CaseSensitiveStringFinder(opts.text);
        else
            f = new StringFinder(opts.text);
        f.setPanel(panel);
        //prepareReentrancies(panel.getContent(), new TreeSet<Integer>());
        return f;
    }
    
    private void setPanel(BlockPanel panel) {
        _panel = panel;
    }
    
    protected abstract boolean matches(Label label);
    
    public boolean find() {
        _stack.clear();
        _stack.push(_panel.getContent().getChildren().iterator());
        return findNext();
    }
    
    public boolean findNext() {
        while (!_stack.isEmpty()) {
            Iterator<Block> iter = _stack.peek();
            if (!iter.hasNext()) {
                _stack.pop();
                continue;
            }
            Block b = iter.next();
            if (b.isLeaf()) {
                Label lab = (Label) b;
                if (matches(lab)) {
                    // ensure the label is visible
                    _panel.ensureVisible(b);
                    _panel.centerBlock(b);
                    _panel.setSelectedBlock(b);
                    return true;
                }
            }
            else if (_panel.isDisplayingModelHiddenFeatures() || !b.isModelHidden())
                _stack.push(new ArrayList<Block>(b.getChildren()).iterator());
        }
        //_panel.setSelectedBlock(null);
        return false;
    }
}
