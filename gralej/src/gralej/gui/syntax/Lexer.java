// $Id$
//
// Copyright (C) 2011, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.gui.syntax;

import java.util.Collection;

/**
 *
 * @author Martin Lazarov
 */
public interface Lexer {
    Collection<Token> getTokens(String text);
}
