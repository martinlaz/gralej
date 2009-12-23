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

import gralej.Config;
import gralej.om.ITag;
import gralej.om.IVisitor;
import gralej.om.lrs.*;

import gralej.util.Log;
import gralej.util.Strings;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import java.util.Map;
import java.util.TreeMap;
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

    private LRSExpr(List<ITerm> subs, String text) {
        _subs = subs;
        _text = text;
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
    private static LRSExprHandler _grammarHandler;

    public static LRSExpr parse(String text) {
        return parse(text, new LinkedList<OM.Tag>());
    }

    synchronized
    static LRSExpr parse(String text, List<OM.Tag> tags) {
        if (_lexer == null) {
            LRTable lrt = Parsers.loadLRTable("lrs-expr.g");
            Grammar g = lrt.grammar();
            _grammarHandler = (LRSExprHandler) GrammarHandler.bind("gralej.parsers.LRSExprHandler", g);
            _parser = new Parser(lrt);
            _lexer = new CharLexer(g);
        }

        _lexer.reset(new StringReader(text));
        List<ITerm> terms;
        try {
            _grammarHandler.setTagStore(tags);
            terms = (List<ITerm>) _parser.parse(_lexer);
            _grammarHandler.setTagStore(null);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
            System.err.println("charpos: " + _lexer.charPos());
            System.err.println("after: " + text.substring(0, _lexer.charPos()));
            throw ex;
        }
        
        return new LRSExpr(terms, text);
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
        protected List<ITerm> _subTerms;
        protected List<ITag> _posConstraints = EMPTY_LIST;
        protected List<ITag> _negConstraints = EMPTY_LIST;

        Term() {
            this(EMPTY_LIST);
        }
        Term(List<ITerm> subTerms) {
            _subTerms = seal(subTerms);
        }
        void setConstraints(List<ITag> pos, List<ITag> neg) {
            _posConstraints = seal(pos);
            _negConstraints = seal(neg);
        }
        public Iterable<ITerm> subTerms() {
            return _subTerms;
        }
        public boolean isLeafTerm() {
            return _subTerms.isEmpty();
        }
        public Iterable<ITag> positiveConstraints() {
            return _posConstraints;
        }
        public Iterable<ITag> negativeConstraints() {
            return _negConstraints;
        }
        protected <T> List<T> seal(List<T> ls) {
            return Collections.unmodifiableList(new ArrayList<T>(ls));
        }
        public String name() {
            throw new UnsupportedOperationException();
        }
        public String uiName() {
            throw new UnsupportedOperationException();
        }
        public boolean hasPositiveConstraints() {
            return !_posConstraints.isEmpty();
        }
        public boolean hasNegativeConstraints() {
            return !_negConstraints.isEmpty();
        }
        public boolean hasConstraints() {
            return !_posConstraints.isEmpty() || !_negConstraints.isEmpty();
        }
    }

    final static class ExCont extends Term implements IExCont {
        ExCont(ITerm t) {
            super(Collections.singletonList(t));
        }
    }

    final static class InCont extends Term implements IInCont {
        InCont(java.util.List<ITerm> ts) {
            super(ts);
        }
    }

    final static class SqCont extends Term implements ISqCont {
        SqCont(java.util.List<ITerm> ts) {
            super(ts);
        }
    }

    static class NamedTerm extends Term implements INamedTerm {
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
        @Override
        public String uiName() {
            return NameMapper.forName(_name);
        }
    }

    final static class Var extends NamedTerm implements IVar {
        Var(String name) { super(name); }
    }

    final static class MetaVar extends NamedTerm implements IMetaVar {
        MetaVar(String name, List<ITerm> subTerms) {
            super(name, subTerms);
        }
    }

    final static class Functor extends NamedTerm implements IFunctor {
        private List<ITerm> _args;

        Functor(String name, List<ITerm> args, List<ITerm> subTerms) {
            super(name, subTerms);
            _args = seal(args);
        }

        public Iterable<ITerm> args() { return _args; }
        public ITerm arg(int i) { return _args.get(i); }
        public int arity() { return _args.size(); }
    }

    final static class NameMapper {
        static Map<String,String> _nameMap = new TreeMap<String,String>();

        static {
            updateNameMap();
            new Config.KeyObserver("lrs.namemap") {
                @Override
                protected void keyChanged() {
                    updateNameMap();
                }
            };
        }

        static String forName(String name) {
            if (_nameMap.containsKey(name))
                return _nameMap.get(name);
            return name;
        }

        static void updateNameMap() {
            _nameMap.clear();

            String S = Strings.unescapeCString(Config.s("lrs.namemap"));
            String[] ss = S.split(",\\s*");

            for (String s : ss) {
                int i = s.indexOf(':');
                if (i == -1) {
                    Log.warning("ignoring lrs namemap spec: " + s);
                    continue;
                }

                String key = s.substring(0, i);
                String val = s.substring(i + 1);

                _nameMap.put(key, val);
            }
        }
    }
}
