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

package gralej.parsers;

import java.lang.reflect.Field;
import java.io.Reader;
import java.io.PushbackReader;
import java.io.IOException;

import tomato.Grammar;
import tomato.Lexer;
import tomato.Token;
import tomato.IllegalTokenException;

public class TraleMsgLexer implements Lexer {
    PushbackReader _in;
    Tokens _tokens;
    Token _current;
    StringBuilder _consumed = new StringBuilder();

    public TraleMsgLexer(Grammar g, Reader in) {
        this(g);
        reset(in);
    }

    public TraleMsgLexer(Grammar g) {
        if (g == null)
            throw new IllegalArgumentException();
        _tokens = new Tokens(g);
    }

    public void reset(Reader in) {
        if (in == null)
            throw new IllegalArgumentException();

        if (in instanceof PushbackReader)
            _in = (PushbackReader) in;
        else
            _in = new PushbackReader(in);

        next();
    }

    public Token current() {
        return _current;
    }

    public boolean hasNext() {
        if (_current == null)
            return false;
        return _current != _tokens._EOF;
    }

    StringBuilder getCharBuffer() {
        return _consumed;
    }

    public Token next() {
        try {
            int c;
            int c2;
            while (true) {
                c = read();
                if (!Character.isWhitespace(c))
                    break;
                if (c == '\n')
                    break;
            }
            if (c == -1)
                _current = _tokens._EOF;
            else
                switch ((char) c) {
                case '(':
                    _current = _tokens._LPAR;
                    // check for a _BEGIN
                    switch ((char) (c2 = read())) {
                    case 'A':
                        _current = _tokens._BEGIN_ANY;
                        break;
                    case 'C':
                        _current = _tokens._BEGIN_CONJ;
                        break;
                    case 'O':
                        _current = _tokens._BEGIN_DISJ;
                        break;
                    case 'V':
                        _current = _tokens._BEGIN_FEATVAL;
                        break;
                    case 'F':
                        _current = _tokens._BEGIN_FUNCT;
                        break;
                    case 'L':
                        _current = _tokens._BEGIN_LIST;
                        break;
                    case 'R':
                        _current = _tokens._BEGIN_REENTR;
                        break;
                    case '#':
                        _current = _tokens._BEGIN_REF;
                        break;
                    case 'D':
                        _current = _tokens._BEGIN_REL;
                        break;
                    case 'Y':
                        _current = _tokens._BEGIN_REST;
                        break;
                    case 'M':
                        _current = _tokens._BEGIN_SET;
                        break;
                    case 'S':
                        _current = _tokens._BEGIN_STRUC;
                        break;
                    case 'Z':
                        _current = _tokens._BEGIN_TAIL;
                        break;
                    case 'T':
                        _current = _tokens._BEGIN_TREE;
                        break;
                    default:
                        // _current stays _LPAR
                        unread(c2);
                    }
                    break;
                case '"': // string
                {
                    StringBuilder s = new StringBuilder();
                    while ((c = read()) != -1) {
                        char ch = (char) c;
                        if (ch == '"')
                            break;
                        s.append(ch);
                    }
                    if (c == -1)
                        throw new IllegalTokenException("Unterminated string");
                    _current = _tokens.STRING(s);
                }
                    break;
                case '<':
                    _current = _tokens._LT;
                    break;
                case '-':
                    _current = _tokens._MINUS;
                    break;
                case '|':
                    _current = _tokens._PIPE;
                    break;
                case '+':
                    _current = _tokens._PLUS;
                    break;
                case ')':
                    _current = _tokens._RPAR;
                    break;
                case '*':
                    _current = _tokens._STAR;
                    break;
                case '\n':
                    _current = _tokens._NEWLINE;
                    break;
                case '!': // newdata
                {
                    String s = "newdata";
                    for (int i = 0; i < s.length(); ++i) {
                        if (s.charAt(i) != (char) read())
                            throw new IllegalTokenException(
                                    "Expected 'newdata' after '!'");
                    }
                }
                    _current = _tokens._NEWDATA;
                    break;
                default:
                    if (Character.isDigit(c)) {
                        StringBuilder s = new StringBuilder();
                        do {
                            s.append((char) c);
                            c = read();
                        } while (Character.isDigit(c));
                        unread(c);
                        _current = _tokens.INT(s);
                    } else {
                        throw new IllegalTokenException("Code: 0x"
                                + Integer.toHexString(c));
                    }
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // System.err.println("++ next token: " + _current);
        return _current;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private int read() throws IOException {
        int c = _in.read();
        if (c != -1)
            _consumed.append((char) c);
        return c;
    }

    private void unread(int c) throws IOException {
        _in.unread(c);
        _consumed.deleteCharAt(_consumed.length() - 1);
    }

    static class T implements Token {
        CharSequence _content;
        String _name;
        int _code;

        T(CharSequence content) {
            _content = content;
        }

        T(Token prototype, CharSequence content) {
            _content = content;
            _name = prototype.name();
            _code = prototype.code();
        }

        public CharSequence content() {
            return _content;
        }

        public String name() {
            return _name;
        }

        public int code() {
            return _code;
        }

        public String toString() {
            return _name + "[" + _content + "]";
        }
    }

    static class Tokens {
        // begins
        T _BEGIN_ANY = new T("(A");
        T _BEGIN_CONJ = new T("(C");
        T _BEGIN_DISJ = new T("(O");
        T _BEGIN_FEATVAL = new T("(V");
        T _BEGIN_FUNCT = new T("(F");
        T _BEGIN_LIST = new T("(L");
        T _BEGIN_REENTR = new T("(R");
        T _BEGIN_REF = new T("(#");
        T _BEGIN_REL = new T("(D");
        T _BEGIN_REST = new T("(Y");
        T _BEGIN_SET = new T("(M");
        T _BEGIN_STRUC = new T("(S");
        T _BEGIN_TAIL = new T("(Z");
        T _BEGIN_TREE = new T("(T");
        // single-char
        T _LPAR = new T("(");
        T _LT = new T("<");
        T _MINUS = new T("-");
        T _PIPE = new T("|");
        T _PLUS = new T("+");
        T _RPAR = new T(")");
        T _STAR = new T("*");
        T _NEWLINE = new T("\n");
        // multi-char
        T _NEWDATA = new T("!newdata");
        // mutable
        T _STRING = new T("");
        T _INT = new T("");
        // eof
        T _EOF = new T("{eof}");

        T STRING(CharSequence content) {
            return new T(_STRING, content);
        }

        T INT(CharSequence content) {
            return new T(_INT, content);
        }

        Tokens(Grammar g) {
            // set the name and code for each token
            for (Field f : getClass().getDeclaredFields()) {
                if (f.getType() == T.class) {
                    T t;
                    try {
                        t = (T) f.get(this);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (t == _EOF)
                        continue;

                    t._name = f.getName();
                    t._code = g.lookupTerminal(t.name()).code();
                }
            }
            _EOF._name = g.eofSymbol().string();
            _EOF._code = g.eofSymbol().code();
        }
    }
}
