//
// WARNING: this is either trale-msg.g or a Java file generated from it !!!
//

package gralej.parsers;

import gralej.om.IEntity;
import gralej.om.IFeatureValuePair;
import gralej.om.IList;
import gralej.om.ITree;
import gralej.om.IRelation;
import gralej.om.IVisitable;
import gralej.om.IneqsAndResidue;

import java.util.Map;
import java.util.LinkedList;
import java.util.TreeMap;

import tomato.GrammarHandler;
import tomato.Token;

import static gralej.Globals.LRS_PREFIX;

@SuppressWarnings("unchecked")
public class TraleMsgHandler extends GrammarHandler {
    static class L<T> extends LinkedList<T> {
        L<T> a(T el) { add(el); return this; }
    }
    
    static class Pair<T,V> {
        final T _1;
        final V _2;
        Pair(T first, V second) { _1 = first; _2 = second; }
    }
    
    OM.Flags _flags;
    
    Map<Integer,IEntity> _id2ent  = new TreeMap<Integer,IEntity>();
    Map<Integer,IEntity> _tag2ent = new TreeMap<Integer,IEntity>();
    
    ITree _tree;

    L<OM.Tag> _tags = new L<OM.Tag>();
    L<Pair<OM.Tree,Integer>> _trees = new L<Pair<OM.Tree,Integer>>();
    
    TraleMsgHandlerHelper _helper = new TraleMsgHandlerHelper();

    private void bindRefs() {
        for (OM.Tag tag : _tags)
            tag.setTarget(_tag2ent.get(tag.number()));
        
        for (Pair<OM.Tree,Integer> p : _trees)
            p._1.setContent(_id2ent.get(p._2));
    }
    
    static String S(Object o) {
        return ((Token)o).content().toString();
    }
    
    static int N(Object o) {
        return Integer.parseInt(S(o));
    }
    
    static class NotImplementedException extends RuntimeException {
        public NotImplementedException() {}
        public NotImplementedException(String msg) { super(msg); }
    }
    

    public static class Terminals extends tomato.AbstractTerminals {
        public Terminals(tomato.Grammar g) {
            super(g);
        }
        public tomato.Terminal _BEGIN_ANY;
        public tomato.Terminal _BEGIN_CONJ;
        public tomato.Terminal _BEGIN_DISJ;
        public tomato.Terminal _BEGIN_FEATVAL;
        public tomato.Terminal _BEGIN_FUNCT;
        public tomato.Terminal _BEGIN_LIST;
        public tomato.Terminal _BEGIN_REENTR;
        public tomato.Terminal _BEGIN_REF;
        public tomato.Terminal _BEGIN_REL;
        public tomato.Terminal _BEGIN_REST;
        public tomato.Terminal _BEGIN_SET;
        public tomato.Terminal _BEGIN_STRUC;
        public tomato.Terminal _BEGIN_TAIL;
        public tomato.Terminal _BEGIN_TREE;
        public tomato.Terminal _INT;
        public tomato.Terminal _LB;
        public tomato.Terminal _LPAR;
        public tomato.Terminal _LT;
        public tomato.Terminal _MINUS;
        public tomato.Terminal _NEWDATA;
        public tomato.Terminal _NEWLINE;
        public tomato.Terminal _PIPE;
        public tomato.Terminal _PLUS;
        public tomato.Terminal _RB;
        public tomato.Terminal _RPAR;
        public tomato.Terminal _SLASH;
        public tomato.Terminal _STAR;
        public tomato.Terminal _STRING;
    } // end of Terminals

    protected void bindReduceHandlers() {
        tomato.ReduceHandler handler;

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                ((L)_[0]).add(_[1]); return _[0];
            }
        };
        // tableRows -> tableRows tableRow
        bindReduceHandler(40, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                // prepare for the next datapackage
            _tree = null;
            _id2ent.clear();
            _tag2ent.clear();
            _tags.clear();
            _trees.clear();
            return null;
            }
        };
        // datapackage0 -> datapackage _NEWLINE
        bindReduceHandler(2, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                L ls = new L(); ls.add(_[0]); return ls;
            }
        };
        // tableRows -> tableRow
        bindReduceHandler(39, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                L<IEntity> ls = (L<IEntity>)_[0];
          ls.add((IEntity)_[1]);
          return ls;
            }
        };
        // structs -> structs struct
        bindReduceHandler(11, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                L<IEntity> structs  = (L<IEntity>)_[3];
            IEntity tail        = (IEntity)_[4];
            
            IList ls = new OM.List((OM.Flags)_[1], structs, tail);
            _id2ent.put(N(_[2]), ls);
            return ls;
            }
        };
        // list -> _BEGIN_LIST flags id structs tail _RPAR
        bindReduceHandler(47, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                L<IFeatureValuePair> ls = (L<IFeatureValuePair>)_[0];
          ls.add((IFeatureValuePair)_[1]);
          return ls;
            }
        };
        // featvals -> featvals featval
        bindReduceHandler(15, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                L<ITree> ls = (L<ITree>)_[0];
          ls.add((ITree)_[1]);
          return ls;
            }
        };
        // trees -> trees tree
        bindReduceHandler(13, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                OM.TFS tfs = new OM.TFS(
                (OM.Flags)_[1],
                (OM.Type) _[3],
                (L<IFeatureValuePair>)_[4]
                );
            int id = N(_[2]);
            _id2ent.put(id, tfs);
            
            return tfs;
            }
        };
        // struc -> _BEGIN_STRUC flags id type featvals _RPAR
        bindReduceHandler(42, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                OM.Tag tag = new OM.Tag(
                (OM.Flags)_[1],
                N(_[3])     // tag number
                );
            _tags.add(tag);
            _id2ent.put(N(_[2]), tag);
            return tag;
            }
        };
        // ref -> _BEGIN_REF flags id target _RPAR
        bindReduceHandler(57, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                String label        = S(_[3]);
        IEntity content     = (IEntity)_[5];
        L<ITree> children   = (L<ITree>)_[6];
        
        OM.Tree t = new OM.Tree(label, children);
        t.setContent(content);
        
        return _tree = t;
            }
        };
        // tree -> _BEGIN_TREE flags id label arclabel structure trees _RPAR
        bindReduceHandler(34, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                String label        = S(_[3]);
        int linkid          = N(_[5]);
        L<ITree> children   = (L<ITree>)_[6];
        
        OM.Tree t = new OM.Tree(label, children);
        
        _trees.add(new Pair<OM.Tree,Integer>(t,linkid));
        
        return _tree = t;
            }
        };
        // tree -> _BEGIN_TREE flags id label arclabel linkid trees _RPAR
        bindReduceHandler(33, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                String s = S(_[3]);
            IEntity ent;
            if (s.startsWith(LRS_PREFIX)) {
                s = s.substring(LRS_PREFIX.length());
                ent = LRSExpr.parse(s, _tags);
            }
            else
                ent = new OM.Any(
                    (OM.Flags)_[1],
                    s
                    );
            _id2ent.put(N(_[2]), ent);
            return ent;
            }
        };
        // any -> _BEGIN_ANY flags id value _RPAR
        bindReduceHandler(58, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                _flags.setDifferent();    return null;
            }
        };
        // flag -> different
        bindReduceHandler(73, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                _flags.setExpanded();     return null;
            }
        };
        // flag -> expanded
        bindReduceHandler(76, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                _flags.setHidden();       return null;
            }
        };
        // flag -> hidden
        bindReduceHandler(72, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                _flags.setMultiline();    return null;
            }
        };
        // flag -> multiline
        bindReduceHandler(75, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                _flags.setStruckout();    return null;
            }
        };
        // flag -> struckout
        bindReduceHandler(74, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                bindRefs();
            
            String title = S(_[1]);
            L<IRelation> ineqs   = (L<IRelation>)_[4];
            L<IRelation> residue = (L<IRelation>)_[5];

            IneqsAndResidue iqsres = new IneqsAndResidue(ineqs, residue);
            
            if (_tree != null) {
                _helper.adviceResult(title, _tree, iqsres);
                return null;
            }
            IVisitable obj = (IVisitable)_[2];
            if (obj == null)
                throw new NotImplementedException("in datapackage");

            _helper.adviceResult(title, obj, iqsres);

            return null;
            }
        };
        // datapackage -> _NEWDATA windowtitle structure structures0 ineqs residue
        bindReduceHandler(3, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                if (_[4] != null) {
            int id  = N(_[2]);
            int tag = N(_[3]);
            IEntity e = (IEntity)_[4];
            
            _id2ent.put(id, e);
            _tag2ent.put(tag, e);
          }
          return null;
            }
        };
        // reentr -> _BEGIN_REENTR flags id tag struct _RPAR
        bindReduceHandler(35, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return ((L<IRelation>)_[0]).a((IRelation)_[1]);
            }
        };
        // relations -> relations relation
        bindReduceHandler(19, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return S(_[0]);
            }
        };
        // tableHeading -> _STRING
        bindReduceHandler(38, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[0];
            }
        };
        // arclabel -> _STRING
        bindReduceHandler(64, handler);
        // feature -> _STRING
        bindReduceHandler(59, handler);
        // functor -> _STRING
        bindReduceHandler(60, handler);
        // id -> _INT
        bindReduceHandler(65, handler);
        // label -> _STRING
        bindReduceHandler(62, handler);
        // linkid -> _INT
        bindReduceHandler(66, handler);
        // name -> _STRING
        bindReduceHandler(67, handler);
        // struct -> any
        bindReduceHandler(29, handler);
        // struct -> conjunction
        bindReduceHandler(31, handler);
        // struct -> disjunction
        bindReduceHandler(30, handler);
        // struct -> function
        bindReduceHandler(25, handler);
        // struct -> list
        bindReduceHandler(27, handler);
        // struct -> ref
        bindReduceHandler(24, handler);
        // struct -> relation
        bindReduceHandler(26, handler);
        // struct -> set
        bindReduceHandler(28, handler);
        // struct -> struc
        bindReduceHandler(23, handler);
        // struct -> table
        bindReduceHandler(32, handler);
        // structure -> reentr
        bindReduceHandler(21, handler);
        // structure -> struct
        bindReduceHandler(20, handler);
        // structure -> tree
        bindReduceHandler(22, handler);
        // tag -> _INT
        bindReduceHandler(69, handler);
        // target -> _INT
        bindReduceHandler(70, handler);
        // value -> _STRING
        bindReduceHandler(68, handler);
        // windowtitle -> _STRING
        bindReduceHandler(61, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[1];
            }
        };
        // ineqs -> _MINUS relations
        bindReduceHandler(5, handler);
        // residue -> _SLASH relations
        bindReduceHandler(7, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[3];
            }
        };
        // tail -> _BEGIN_TAIL flags id struct _RPAR
        bindReduceHandler(49, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _flags = new OM.Flags();
            }
        };
        // flags -> 
        bindReduceHandler(16, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _flags;
            }
        };
        // flags -> flags flag
        bindReduceHandler(17, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new L<IEntity>();
            }
        };
        // structs -> 
        bindReduceHandler(10, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new L<IFeatureValuePair>();
            }
        };
        // featvals -> 
        bindReduceHandler(14, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new L<IRelation>().a((IRelation)_[0]);
            }
        };
        // relations -> relation
        bindReduceHandler(18, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new L<ITree>();
            }
        };
        // trees -> 
        bindReduceHandler(12, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new OM.FeatVal(
                (OM.Flags)_[1],
                S(_[3]),        // feature
                (IEntity)_[4]   // value
                );
            }
        };
        // featval -> _BEGIN_FEATVAL flags id feature struct _RPAR
        bindReduceHandler(43, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new OM.FeatVal(OM.DEFAULT_FLAGS, S(_[0]), (IEntity)_[1]);
            }
        };
        // tableRow -> _STRING struct
        bindReduceHandler(41, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new OM.Relation(
                (OM.Flags)_[1],
                S(_[3]),
                (L<IEntity>)_[4]
            );
            }
        };
        // relation -> _BEGIN_REL flags id functor structs _RPAR
        bindReduceHandler(54, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new OM.Table((String)_[1], (L)_[3]);
            }
        };
        // table -> _LB tableHeading _MINUS tableRows _RB
        bindReduceHandler(36, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new OM.Type((OM.Flags)_[1], S(_[3]));
            }
        };
        // type -> _LPAR flags id name _RPAR
        bindReduceHandler(56, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                throw new NotImplementedException("conjunction");
            }
        };
        // conjunction -> _BEGIN_CONJ flags id struct struct structs _RPAR
        bindReduceHandler(46, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                throw new NotImplementedException("disjunction");
            }
        };
        // disjunction -> _BEGIN_DISJ flags id struct struct structs _RPAR
        bindReduceHandler(45, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                throw new NotImplementedException("function");
            }
        };
        // function -> _BEGIN_FUNCT flags id type functor structs _RPAR
        bindReduceHandler(53, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                throw new NotImplementedException("rest");
            }
        };
        // rest -> _BEGIN_REST flags id struct _RPAR
        bindReduceHandler(52, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                throw new NotImplementedException("set");
            }
        };
        // set -> _BEGIN_SET flags id structs rest _RPAR
        bindReduceHandler(50, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                throw new NotImplementedException("value-less feature");
            }
        };
        // featval -> _BEGIN_FEATVAL flags id feature _RPAR
        bindReduceHandler(44, handler);
    }
}

