package gralej.parsers;

import java.util.List;
import java.util.LinkedList;
import static java.util.Collections.EMPTY_LIST;

import static gralej.parsers.LRSExpr.*;
import gralej.om.ITag;
import gralej.om.lrs.ITerm;

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
%

LRSExpr ->
    Terms
    .

Terms ->
    Term
        { LT lt = new LT(); lt.add(T(_,0)); return lt; }
    | Terms ',' Term
        { LT lt = (LT)_[0]; lt.add(T(_,2)); return lt; }
    .

Term ->
    BasicTerm
    | BasicTerm '/' ContribConstraints
        {
            Term t  = T(_,0);
            LI[] cs = (LI[])_[2];
            t.setConstraints(cs[0], cs[1]);
            return t;
        }
    .

BasicTerm ->
    Var
    | MetaVar
    | Functor
    | ExCont
    | InCont
    | SqCont
    .

ContribConstraints ->
    Constraints # only positive
        {{ return new LI[]{ (LI)_[0], new LI()}; }}
    | Constraints '~' Constraints # positive and negative
        {{ return new LI[]{ (LI)_[0], (LI)_[2]}; }}
    | '~' Constraints # only negative
        {{ return new LI[]{ new LI(), (LI)_[2]}; }}
    .

Constraints ->
    '(' TagList ')'
        {{
            LI tags = new LI();
            for (Integer itag : (LN)_[1]) {
                OM.Tag tag = new OM.Tag(itag);
                tags.add(tag);
                _tags.add(tag);
            }
            return tags;
        }}
    .

TagList ->
    Tag
        { LN li = new LN(); li.add((Integer)_[0]); return li; }
    | TagList ',' Tag
        { LN li = (LN)_[0]; li.add((Integer)_[2]); return li; }
    .

Tag ->
    '[' Int ']'
        { return Integer.parseInt(S(_,1)); }
    .

Var ->
    LCaseWord
        { return new Var(S(_,0)); }
    .

MetaVar ->
    UCaseWord SubTerms
        { return new MetaVar(S(_,0), Ts(_,1)); }
    .

Functor ->
    LCaseWord '(' Terms ')' SubTerms
        { return new Functor(S(_,0), Ts(_,2), Ts(_,4)); }
    .

SubTerms ->
        { return EMPTY_LIST; }
    | ':' '[' Terms ']'
        { return Ts(_,2); }
    .

ExCont ->
    '^' BasicTerm
        { return new ExCont(T(_,1)); }
    .

InCont ->
    '{' Terms '}'
        { return new InCont(Ts(_,1)); }
    .

SqCont ->
    '[' Terms ']'
        { return new SqCont(Ts(_,1)); }
    .

# lexer

UCase ->
      'A' | 'B' | 'C' | 'D' | 'E' | 'F'
    | 'G' | 'H' | 'I' | 'J' | 'K' | 'L'
    | 'M' | 'N' | 'O' | 'P' | 'Q' | 'R'
    | 'S' | 'T' | 'U' | 'V' | 'W' | 'X'
    | 'Y' | 'Z' | __NON_ASCII_UCASE
    .

LCase ->
      'a' | 'b' | 'c' | 'd' | 'e' | 'f'
    | 'g' | 'h' | 'i' | 'j' | 'k' | 'l'
    | 'm' | 'n' | 'o' | 'p' | 'q' | 'r'
    | 's' | 't' | 'u' | 'v' | 'w' | 'x'
    | 'y' | 'z' | __NON_ASCII_LCASE
    .

Digit ->
    '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
    .

Alnum ->
    UCase
    | LCase
    | Digit
    .

AlnumSeq ->
    Alnum
        { return new StringBuilder().append(S(_,0)); }
    | AlnumSeq Alnum
        { return ((StringBuilder)_[0]).append(S(_,1)); }
    .

AlnumSeqOpt ->
        { return ""; }
    | AlnumSeq
    .

Int ->
    Digit
        { return new StringBuilder().append(S(_,0)); }
    | Int Digit
        { return ((StringBuilder)_[0]).append(S(_,1)); }
    .

LCaseWord ->
    LCase AlnumSeqOpt
        { return new StringBuilder().append(S(_,0)).append(S(_,1)); }
    .

UCaseWord ->
    UCase AlnumSeqOpt
        { return new StringBuilder().append(S(_,0)).append(S(_,1)); }
    .

%
}
