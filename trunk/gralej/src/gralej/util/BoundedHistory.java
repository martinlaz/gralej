// $Id$
//
// Copyright (C) 2009, Martin Lazarov (martinlaz at gmail)
// All rights reserved.
//

package gralej.util;

/**
 * @author Martin Lazarov
 */
public final class BoundedHistory<T> {
    private class Link {
        Link prev, next;
        T data;
    }
    private Link _begin, _end, _current;
    private int _currentSize;
    private int _maxItems;
    
    public BoundedHistory(int maxItems) {
        if (maxItems <= 0)
            throw new IllegalArgumentException();
        _maxItems = maxItems;
        _begin = new Link();
        _end = new Link();
        _begin.next = _end;
        _end.prev = _begin;
        _current = _begin;
    }
    
    public void add(T newItem) {
        Link newLink = new Link();
        newLink.data = newItem;
        if (_currentSize == _maxItems) {
            _begin.next.next.prev = _begin;
            _begin.next = _begin.next.next;
        }
        else {
            _currentSize++;
        }
        if (_current == null) {
            newLink.prev = _begin;
            _begin.next = newLink;
            newLink.next = _end;
            _end.prev = newLink;
        }
        else {
            _current.next = newLink;
            newLink.prev = _current;
            _end.prev = newLink;
            newLink.next = _end;
        }
        _current = newLink;
        //Log.debug("new history item:", newItem);
    }
    
    public T prev() {
        if (!hasPrev())
            throw new IllegalStateException();
        _current = _current.prev;
        _currentSize--;
        //Log.debug("retrieved prev history item:", _current.data);
        return _current.data;
    }
    
    public T next() {
        if (!hasNext())
            throw new IllegalStateException();
        _current = _current.next;
        _currentSize++;
        //Log.debug("retrieved next history item:", _current.data);
        return _current.data;
    }
    
    public boolean hasPrev() {
        return _current != _begin && _current.prev != _begin;
    }
    
    public boolean hasNext() {
        return _current != _end && _current.next != _end;
    }
}
