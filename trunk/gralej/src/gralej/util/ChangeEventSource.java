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

package gralej.util;

import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ChangeEventSource {
    protected Set<ChangeListener> _sinks = new
        HashSet<ChangeListener>();
    
    public void addChangeListener(ChangeListener sink) {
        _sinks.add(sink);
    }
    
    public void removeChangeListener(ChangeListener sink) {
        _sinks.remove(sink);
    }
    
    protected void fireStateChanged(Object sender) {
        ChangeEvent event = new ChangeEvent(sender);
        for (ChangeListener sink : _sinks)
            sink.stateChanged(event);
    }
}
