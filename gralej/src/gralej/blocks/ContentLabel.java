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

public class ContentLabel extends Label {
    protected Block _content;
    
    ContentLabel(BlockPanel panel, LabelStyle style, String text) {
        super(panel, style, text);
    }
    
    public void flipContentVisibility() {
        if (_content == null)
            _content = ((ContentOwner) getParent()).getContent();
        if (_content == null)
            return;
        
        boolean newVisible = !_content.isVisible();
        _content.setVisible(newVisible);

        getPanel().getCanvas().repaint();
    }
    
    @Override
    protected boolean useTextAltColor() {
        if (_content == null)
            return false;

        return !_content.isVisible();
    }

    public void flip() {
        flipContentVisibility();
    }

    void setContent(Block content) {
        assert _content == null;
        _content = content;
    }
}
