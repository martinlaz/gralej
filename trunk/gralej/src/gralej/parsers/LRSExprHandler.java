package gralej.parsers;

import java.util.List;
import java.util.LinkedList;
import static java.util.Collections.EMPTY_LIST;

import static gralej.parsers.LRSExpr.*;
import gralej.om.lrs.ITerm;

import tomato.GrammarHandler;
import tomato.Token;

public class LRSExprHandler extends GrammarHandler {
    private static class LI extends LinkedList<Integer> {}
    private static class LT extends LinkedList<ITerm> {}
    
    private static String S(Object[] _, int i) {
        Object obj = _[i];
        if (obj instanceof CharSequence)
            return obj.toString();
        return ((Token)obj).content().toString();
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

    public static class Terminals extends tomato.AbstractTerminals {
        public Terminals(tomato.Grammar g) {
            super(g);
        }
        public tomato.Terminal __CHAR_100;
        public tomato.Terminal __CHAR_101;
        public tomato.Terminal __CHAR_102;
        public tomato.Terminal __CHAR_103;
        public tomato.Terminal __CHAR_104;
        public tomato.Terminal __CHAR_105;
        public tomato.Terminal __CHAR_106;
        public tomato.Terminal __CHAR_107;
        public tomato.Terminal __CHAR_108;
        public tomato.Terminal __CHAR_109;
        public tomato.Terminal __CHAR_110;
        public tomato.Terminal __CHAR_111;
        public tomato.Terminal __CHAR_112;
        public tomato.Terminal __CHAR_113;
        public tomato.Terminal __CHAR_114;
        public tomato.Terminal __CHAR_115;
        public tomato.Terminal __CHAR_116;
        public tomato.Terminal __CHAR_117;
        public tomato.Terminal __CHAR_118;
        public tomato.Terminal __CHAR_119;
        public tomato.Terminal __CHAR_120;
        public tomato.Terminal __CHAR_121;
        public tomato.Terminal __CHAR_122;
        public tomato.Terminal __CHAR_123;
        public tomato.Terminal __CHAR_125;
        public tomato.Terminal __CHAR_126;
        public tomato.Terminal __CHAR_40;
        public tomato.Terminal __CHAR_41;
        public tomato.Terminal __CHAR_44;
        public tomato.Terminal __CHAR_47;
        public tomato.Terminal __CHAR_48;
        public tomato.Terminal __CHAR_49;
        public tomato.Terminal __CHAR_50;
        public tomato.Terminal __CHAR_51;
        public tomato.Terminal __CHAR_52;
        public tomato.Terminal __CHAR_53;
        public tomato.Terminal __CHAR_54;
        public tomato.Terminal __CHAR_55;
        public tomato.Terminal __CHAR_56;
        public tomato.Terminal __CHAR_57;
        public tomato.Terminal __CHAR_58;
        public tomato.Terminal __CHAR_65;
        public tomato.Terminal __CHAR_66;
        public tomato.Terminal __CHAR_67;
        public tomato.Terminal __CHAR_68;
        public tomato.Terminal __CHAR_69;
        public tomato.Terminal __CHAR_70;
        public tomato.Terminal __CHAR_71;
        public tomato.Terminal __CHAR_72;
        public tomato.Terminal __CHAR_73;
        public tomato.Terminal __CHAR_74;
        public tomato.Terminal __CHAR_75;
        public tomato.Terminal __CHAR_76;
        public tomato.Terminal __CHAR_77;
        public tomato.Terminal __CHAR_78;
        public tomato.Terminal __CHAR_79;
        public tomato.Terminal __CHAR_80;
        public tomato.Terminal __CHAR_81;
        public tomato.Terminal __CHAR_82;
        public tomato.Terminal __CHAR_83;
        public tomato.Terminal __CHAR_84;
        public tomato.Terminal __CHAR_85;
        public tomato.Terminal __CHAR_86;
        public tomato.Terminal __CHAR_87;
        public tomato.Terminal __CHAR_88;
        public tomato.Terminal __CHAR_89;
        public tomato.Terminal __CHAR_90;
        public tomato.Terminal __CHAR_91;
        public tomato.Terminal __CHAR_93;
        public tomato.Terminal __CHAR_94;
        public tomato.Terminal __CHAR_97;
        public tomato.Terminal __CHAR_98;
        public tomato.Terminal __CHAR_99;
    } // end of Terminals

    protected void bindReduceHandlers() {
        tomato.ReduceHandler handler;

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                LI li = (LI)_[0]; li.add((Integer)_[2]); return li;
            }
        };
        // TagList -> TagList ',' Tag
        bindReduceHandler(16, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                LI li = new LI(); li.add((Integer)_[0]); return li;
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
        bindReduceHandler(93, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return ((StringBuilder)_[0]).append(S(_,1));
            }
        };
        // AlnumSeq -> AlnumSeq Alnum
        bindReduceHandler(92, handler);
        // _INT -> _INT Digit
        bindReduceHandler(96, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return EMPTY_LIST;
            }
        };
        // SubTerms -> 
        bindReduceHandler(21, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return Integer.parseInt(S(_,1));
            }
        };
        // Tag -> '[' _INT ']'
        bindReduceHandler(17, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return Ts(_,2);
            }
        };
        // SubTerms -> ':' '[' Terms ']'
        bindReduceHandler(22, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[0];
            }
        };
        // Alnum -> Digit
        bindReduceHandler(90, handler);
        // Alnum -> LCase
        bindReduceHandler(89, handler);
        // Alnum -> UCase
        bindReduceHandler(88, handler);
        // AlnumSeqOpt -> AlnumSeq
        bindReduceHandler(94, handler);
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
        // Digit -> '0'
        bindReduceHandler(78, handler);
        // Digit -> '1'
        bindReduceHandler(79, handler);
        // Digit -> '2'
        bindReduceHandler(80, handler);
        // Digit -> '3'
        bindReduceHandler(81, handler);
        // Digit -> '4'
        bindReduceHandler(82, handler);
        // Digit -> '5'
        bindReduceHandler(83, handler);
        // Digit -> '6'
        bindReduceHandler(84, handler);
        // Digit -> '7'
        bindReduceHandler(85, handler);
        // Digit -> '8'
        bindReduceHandler(86, handler);
        // Digit -> '9'
        bindReduceHandler(87, handler);
        // LCase -> 'd'
        bindReduceHandler(55, handler);
        // LCase -> 'e'
        bindReduceHandler(56, handler);
        // LCase -> 'f'
        bindReduceHandler(57, handler);
        // LCase -> 'g'
        bindReduceHandler(58, handler);
        // LCase -> 'h'
        bindReduceHandler(59, handler);
        // LCase -> 'i'
        bindReduceHandler(60, handler);
        // LCase -> 'j'
        bindReduceHandler(61, handler);
        // LCase -> 'k'
        bindReduceHandler(62, handler);
        // LCase -> 'l'
        bindReduceHandler(63, handler);
        // LCase -> 'm'
        bindReduceHandler(64, handler);
        // LCase -> 'n'
        bindReduceHandler(65, handler);
        // LCase -> 'o'
        bindReduceHandler(66, handler);
        // LCase -> 'p'
        bindReduceHandler(67, handler);
        // LCase -> 'q'
        bindReduceHandler(68, handler);
        // LCase -> 'r'
        bindReduceHandler(69, handler);
        // LCase -> 's'
        bindReduceHandler(70, handler);
        // LCase -> 't'
        bindReduceHandler(71, handler);
        // LCase -> 'u'
        bindReduceHandler(72, handler);
        // LCase -> 'v'
        bindReduceHandler(73, handler);
        // LCase -> 'w'
        bindReduceHandler(74, handler);
        // LCase -> 'x'
        bindReduceHandler(75, handler);
        // LCase -> 'y'
        bindReduceHandler(76, handler);
        // LCase -> 'z'
        bindReduceHandler(77, handler);
        // LCase -> 'a'
        bindReduceHandler(52, handler);
        // LCase -> 'b'
        bindReduceHandler(53, handler);
        // LCase -> 'c'
        bindReduceHandler(54, handler);
        // LRSExpr -> Terms
        bindReduceHandler(0, handler);
        // Term -> BasicTerm
        bindReduceHandler(3, handler);
        // UCase -> 'A'
        bindReduceHandler(26, handler);
        // UCase -> 'B'
        bindReduceHandler(27, handler);
        // UCase -> 'C'
        bindReduceHandler(28, handler);
        // UCase -> 'D'
        bindReduceHandler(29, handler);
        // UCase -> 'E'
        bindReduceHandler(30, handler);
        // UCase -> 'F'
        bindReduceHandler(31, handler);
        // UCase -> 'G'
        bindReduceHandler(32, handler);
        // UCase -> 'H'
        bindReduceHandler(33, handler);
        // UCase -> 'I'
        bindReduceHandler(34, handler);
        // UCase -> 'J'
        bindReduceHandler(35, handler);
        // UCase -> 'K'
        bindReduceHandler(36, handler);
        // UCase -> 'L'
        bindReduceHandler(37, handler);
        // UCase -> 'M'
        bindReduceHandler(38, handler);
        // UCase -> 'N'
        bindReduceHandler(39, handler);
        // UCase -> 'O'
        bindReduceHandler(40, handler);
        // UCase -> 'P'
        bindReduceHandler(41, handler);
        // UCase -> 'Q'
        bindReduceHandler(42, handler);
        // UCase -> 'R'
        bindReduceHandler(43, handler);
        // UCase -> 'S'
        bindReduceHandler(44, handler);
        // UCase -> 'T'
        bindReduceHandler(45, handler);
        // UCase -> 'U'
        bindReduceHandler(46, handler);
        // UCase -> 'V'
        bindReduceHandler(47, handler);
        // UCase -> 'W'
        bindReduceHandler(48, handler);
        // UCase -> 'X'
        bindReduceHandler(49, handler);
        // UCase -> 'Y'
        bindReduceHandler(50, handler);
        // UCase -> 'Z'
        bindReduceHandler(51, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[1];
            }
        };
        // Constraints -> '(' TagList ')'
        bindReduceHandler(14, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new ExCont(T(_,1));
            }
        };
        // ExCont -> '^' Term
        bindReduceHandler(23, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new Functor(S(_,0), Ts(_,2), Ts(_,4));
            }
        };
        // Functor -> _LCASE_WORD '(' Terms ')' SubTerms
        bindReduceHandler(20, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new InCont(Ts(_,1));
            }
        };
        // InCont -> '{' Terms '}'
        bindReduceHandler(24, handler);

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
                return new LI[]{ new LI(), (LI)_[2]};
            }
        };
        // ContribConstraints -> '~' Constraints
        bindReduceHandler(13, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new MetaVar(S(_,0), Ts(_,1));
            }
        };
        // MetaVar -> _UCASE_WORD SubTerms
        bindReduceHandler(19, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new SqCont(Ts(_,1));
            }
        };
        // SqCont -> '[' Terms ']'
        bindReduceHandler(25, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new StringBuilder().append(S(_,0)).append(S(_,1));
            }
        };
        // _LCASE_WORD -> LCase AlnumSeqOpt
        bindReduceHandler(97, handler);
        // _UCASE_WORD -> UCase AlnumSeqOpt
        bindReduceHandler(98, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new StringBuilder().append(S(_,0));
            }
        };
        // AlnumSeq -> Alnum
        bindReduceHandler(91, handler);
        // _INT -> Digit
        bindReduceHandler(95, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new Var(S(_,0));
            }
        };
        // Var -> _LCASE_WORD
        bindReduceHandler(18, handler);
    }
}

