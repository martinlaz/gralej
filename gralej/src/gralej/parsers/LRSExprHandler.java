package gralej.parsers;

import java.util.List;
import java.util.LinkedList;
import static java.util.Collections.EMPTY_LIST;

import static gralej.parsers.LRSExpr.*;
import gralej.om.ITag;
import gralej.om.lrs.ITerm;

@SuppressWarnings("unchecked")
public class LRSExprHandler extends tomato.GrammarHandler {
    private static class LN extends LinkedList<Integer> {}
    private static class LI extends LinkedList<ITag> {}
    private static class LT extends LinkedList<ITerm> {}
    
    private List<OM.Tag> _tags;
    
    private static String S(Object[] _, int i) {
        return _[i].toString();
    }
    private static Term T(Object[] _, int i) {
        return (Term)_[i];
    }
    private static List<ITerm> Ts(Object[] _, int i) {
        Object obj = _[i];
        if (obj == EMPTY_LIST)
            return EMPTY_LIST;
        return (LT) obj;
    }
    void setTagStore(List<OM.Tag> tags) {
        if (tags == null)
            tags = EMPTY_LIST;
        _tags = tags;
    }

    public static class Terminals extends tomato.AbstractTerminals {
        public Terminals(tomato.Grammar g) {
            super(g);
        }
        public tomato.Terminal _DIGIT;
        public tomato.Terminal _LCASE_LETTER;
        public tomato.Terminal _UCASE_LETTER;
        public tomato.Terminal __CHAR_123;
        public tomato.Terminal __CHAR_125;
        public tomato.Terminal __CHAR_126;
        public tomato.Terminal __CHAR_40;
        public tomato.Terminal __CHAR_41;
        public tomato.Terminal __CHAR_42;
        public tomato.Terminal __CHAR_44;
        public tomato.Terminal __CHAR_47;
        public tomato.Terminal __CHAR_58;
        public tomato.Terminal __CHAR_91;
        public tomato.Terminal __CHAR_93;
        public tomato.Terminal __CHAR_94;
    } // end of Terminals

    protected void bindReduceHandlers() {
        tomato.ReduceHandler handler;

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                LI tags = new LI();
            for (Integer itag : (LN)_[1]) {
                OM.Tag tag = new OM.Tag(itag);
                tags.add(tag);
                _tags.add(tag);
            }
            return tags;
            }
        };
        // Constraints -> '(' TagList ')'
        bindReduceHandler(14, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                LN li = (LN)_[0]; li.add((Integer)_[2]); return li;
            }
        };
        // TagList -> TagList ',' Tag
        bindReduceHandler(16, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                LN li = new LN(); li.add((Integer)_[0]); return li;
            }
        };
        // TagList -> Tag
        bindReduceHandler(15, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                LT lt = (LT)_[0]; lt.add(T(_,2)); return lt;
            }
        };
        // Terms -> Terms ',' Term
        bindReduceHandler(2, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                LT lt = new LT(); lt.add(T(_,0)); return lt;
            }
        };
        // Terms -> Term
        bindReduceHandler(1, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                Term t  = T(_,0);
            LI[] cs = (LI[])_[2];
            t.setConstraints(cs[0], cs[1]);
            return t;
            }
        };
        // Term -> BasicTerm '/' ContribConstraints
        bindReduceHandler(4, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return "";
            }
        };
        // AlnumSeqOpt -> 
        bindReduceHandler(33, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return ((StringBuilder)_[0]).append(S(_,1));
            }
        };
        // AlnumSeq -> AlnumSeq Alnum
        bindReduceHandler(32, handler);
        // Int -> Int _DIGIT
        bindReduceHandler(36, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return -1;
            }
        };
        // TagValue -> '*'
        bindReduceHandler(18, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return EMPTY_LIST;
            }
        };
        // SubTerms -> 
        bindReduceHandler(23, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return Integer.parseInt(S(_,1));
            }
        };
        // Tag -> '[' TagValue ']'
        bindReduceHandler(17, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return Ts(_,2);
            }
        };
        // SubTerms -> ':' '[' Terms ']'
        bindReduceHandler(24, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[0];
            }
        };
        // Alnum -> _DIGIT
        bindReduceHandler(30, handler);
        // Alnum -> _LCASE_LETTER
        bindReduceHandler(29, handler);
        // Alnum -> _UCASE_LETTER
        bindReduceHandler(28, handler);
        // AlnumSeqOpt -> AlnumSeq
        bindReduceHandler(34, handler);
        // BasicTerm -> ExCont
        bindReduceHandler(8, handler);
        // BasicTerm -> Functor
        bindReduceHandler(7, handler);
        // BasicTerm -> InCont
        bindReduceHandler(9, handler);
        // BasicTerm -> MetaVar
        bindReduceHandler(6, handler);
        // BasicTerm -> SqCont
        bindReduceHandler(10, handler);
        // BasicTerm -> Var
        bindReduceHandler(5, handler);
        // LRSExpr -> Terms
        bindReduceHandler(0, handler);
        // TagValue -> Int
        bindReduceHandler(19, handler);
        // Term -> BasicTerm
        bindReduceHandler(3, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new ExCont(T(_,1));
            }
        };
        // ExCont -> '^' BasicTerm
        bindReduceHandler(25, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new Functor(S(_,0), Ts(_,2), Ts(_,4));
            }
        };
        // Functor -> LCaseWord '(' Terms ')' SubTerms
        bindReduceHandler(22, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new InCont(Ts(_,1));
            }
        };
        // InCont -> '{' Terms '}'
        bindReduceHandler(26, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new LI[]{ (LI)_[0], (LI)_[2]};
            }
        };
        // ContribConstraints -> Constraints '~' Constraints
        bindReduceHandler(12, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new LI[]{ (LI)_[0], new LI()};
            }
        };
        // ContribConstraints -> Constraints
        bindReduceHandler(11, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new LI[]{ new LI(), (LI)_[1]};
            }
        };
        // ContribConstraints -> '~' Constraints
        bindReduceHandler(13, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new MetaVar(S(_,0), Ts(_,1));
            }
        };
        // MetaVar -> UCaseWord SubTerms
        bindReduceHandler(21, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new SqCont(Ts(_,1));
            }
        };
        // SqCont -> '[' Terms ']'
        bindReduceHandler(27, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new StringBuilder().append(S(_,0)).append(S(_,1));
            }
        };
        // LCaseWord -> _LCASE_LETTER AlnumSeqOpt
        bindReduceHandler(37, handler);
        // UCaseWord -> _UCASE_LETTER AlnumSeqOpt
        bindReduceHandler(38, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new StringBuilder().append(S(_,0));
            }
        };
        // AlnumSeq -> Alnum
        bindReduceHandler(31, handler);
        // Int -> _DIGIT
        bindReduceHandler(35, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new Var(S(_,0));
            }
        };
        // Var -> LCaseWord
        bindReduceHandler(20, handler);
    }
}

