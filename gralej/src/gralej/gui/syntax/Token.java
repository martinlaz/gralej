// $Id$
//
// Copyright (C) 2010, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.gui.syntax;

/**
 *
 * @author Martin Lazarov
 */
public class Token {
    private String type, content;
    private int offset, length;

    public Token(String type, String content, int begin, int length) {
        this.type = type;
        this.content = content;
        this.offset = begin;
        this.length = length;
    }

    public int getOffset() {
        return offset;
    }

    public String getContent() {
        return content;
    }

    public int getLength() {
        return length;
    }

    public String getType() {
        return type;
    }

    
}
