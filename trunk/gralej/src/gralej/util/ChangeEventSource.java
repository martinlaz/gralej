package gralej.util;

import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Martin
 * @version $Id$
 */
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
