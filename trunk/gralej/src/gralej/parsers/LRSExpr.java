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

import gralej.om.IVisitor;
import gralej.om.lrs.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tomato.CharLexer;
import tomato.Grammar;
import tomato.GrammarHandler;
import tomato.LRTable;
import tomato.Parser;
import static java.util.Collections.EMPTY_LIST;

/**
 *
 * @author Martin
 */
public final class LRSExpr implements ILRSExpr {
    private List<ITerm> _subs;
    private String _text;

    private LRSExpr() {
    }

    public List<ITerm> subTerms() {
        return _subs;
    }

    public String text() {
        return _text;
    }

    public void accept(IVisitor v) {
        v.visit(this);
    }

    private static CharLexer _lexer;
    private static Parser _parser;

    synchronized
    public static LRSExpr parse(String text) {
        if (_lexer == null) {
            LRTable lrt = Parsers.loadLRTable("lrs-expr.g");
            Grammar g = lrt.grammar();
            GrammarHandler.bind("gralej.parsers.LRSExprHandler", g);
            _parser = new Parser(lrt);
            _lexer = new CharLexer(g);
        }

        _lexer.reset(new StringReader(text));
        List<ITerm> terms;
        try {
            terms = (List<ITerm>) _parser.parse(_lexer);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            System.err.println("charpos: " + _lexer.charPos());
            System.err.println("after: " + text.substring(0, _lexer.charPos()));
            throw ex;
        }

        LRSExpr expr = new LRSExpr();
        expr._subs = terms;
        expr._text = text;
        
        return expr;
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public boolean isDifferent() {
        return false;
    }

    @Override
    public void setDifferent(boolean flag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isStruckout() {
        return false;
    }

    @Override
    public boolean isMultiline() {
        return false;
    }

    @Override
    public boolean isExpanded() {
        return false;
    }
    

    static class Term implements ITerm {
        List<ITerm> _subTerms;
        List<Integer> _posConstraints = EMPTY_LIST;
        List<Integer> _negConstraints = EMPTY_LIST;

        Term() {
            this(EMPTY_LIST);
        }
        Term(List<ITerm> subTerms) {
            _subTerms = seal(subTerms);
        }
        void setConstraints(List<Integer> pos, List<Integer> neg) {
            _posConstraints = seal(pos);
            _negConstraints = seal(neg);
        }
        public Iterable<ITerm> subTerms() {
            return _subTerms;
        }
        public Iterable<Integer> positiveConstraints() {
            return _posConstraints;
        }
        public Iterable<Integer> negativeConstraints() {
            return _negConstraints;
        }
        protected <T> List<T> seal(List<T> ls) {
            return Collections.unmodifiableList(new ArrayList<T>(ls));
        }
        public String name() {
            throw new UnsupportedOperationException();
        }
    }

    static class ExCont extends Term {
        ExCont(ITerm t) {
            super(Collections.singletonList(t));
        }
    }

    static class InCont extends Term {
        InCont(java.util.List<ITerm> ts) {
            super(ts);
        }
    }

    static class SqCont extends Term {
        SqCont(java.util.List<ITerm> ts) {
            super(ts);
        }
    }

    static class NamedTerm extends Term {
        String _name;
        NamedTerm(String name) {
            _name = name;
        }
        NamedTerm(String name, List<ITerm> subTerms) {
            super(subTerms);
            _name = name;
        }
        @Override
        public String name() {
            return _name;
        }
    }

    static class Var extends NamedTerm {
        Var(String name) { super(name); }
    }

    static class MetaVar extends NamedTerm {
        MetaVar(String name, List<ITerm> subTerms) {
            super(name, subTerms);
        }
    }

    static class Functor extends NamedTerm {
        List<ITerm> _args;

        Functor(String name, List<ITerm> args, List<ITerm> subTerms) {
            super(name, subTerms);
            _args = seal(args);
        }

        public Iterable<ITerm> args() { return _args; }
        public ITerm arg(int i) { return _args.get(i); }
        public int arity() { return _args.size(); }
    }
}
