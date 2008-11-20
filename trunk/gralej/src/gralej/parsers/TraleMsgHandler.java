package gralej.parsers;

import gralej.om.IAny;
import gralej.om.IEntity;
import gralej.om.IFeatureValuePair;
import gralej.om.IList;
import gralej.om.ITree;
import gralej.om.ITypedFeatureStructure;

import java.util.Map;
import java.util.TreeMap;

import tomato.GrammarHandler;
import tomato.Token;

public class TraleMsgHandler extends GrammarHandler {
    static class L<T> extends java.util.LinkedList<T> {}
    
    static class Pair<T,V> {
        final T _1;
        final V _2;
        Pair(T first, V second) { _1 = first; _2 = second; }
    }
    
    OM.Flags _flags;
    
    Map<Integer,IEntity> _id2ent  = new TreeMap<Integer,IEntity>();
    Map<Integer,IEntity> _tag2ent = new TreeMap<Integer,IEntity>();
    
    ITree _tree;
    ITypedFeatureStructure _tfs;
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
        public tomato.Terminal _LPAR;
        public tomato.Terminal _LT;
        public tomato.Terminal _MINUS;
        public tomato.Terminal _NEWDATA;
        public tomato.Terminal _NEWLINE;
        public tomato.Terminal _PIPE;
        public tomato.Terminal _PLUS;
        public tomato.Terminal _RPAR;
        public tomato.Terminal _STAR;
        public tomato.Terminal _STRING;
    } // end of Terminals

    protected void bindReduceHandlers() {
        tomato.ReduceHandler handler;

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                // get ready for the next datapackage
            _tfs  = null;
            _tree = null;
            _id2ent.clear();
            _tag2ent.clear();
            _tags.clear();
            _trees.clear();
            return null;
            }
        };
        // datapackage0 -> [datapackage, _NEWLINE]
        bindReduceHandler(2, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                IAny any = new OM.Any(
                (OM.Flags)_[1],
                S(_[3])
                );
            _id2ent.put(N(_[2]), any);
            return any;
            }
        };
        // any -> [_BEGIN_ANY, flags, id, value, _RPAR]
        bindReduceHandler(43, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                L<IEntity> ls = (L<IEntity>)_[0];
          ls.add((IEntity)_[1]);
          return ls;
            }
        };
        // structs -> [structs, struct]
        bindReduceHandler(7, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                L<IEntity> structs  = (L<IEntity>)_[3];
            IEntity tail        = (IEntity)_[4];
            
            IList ls = new OM.List((OM.Flags)_[1], structs, tail);
            _id2ent.put(N(_[2]), ls);
            return ls;
            }
        };
        // list -> [_BEGIN_LIST, flags, id, structs, tail, _RPAR]
        bindReduceHandler(33, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                L<IFeatureValuePair> ls = (L<IFeatureValuePair>)_[0];
          ls.add((IFeatureValuePair)_[1]);
          return ls;
            }
        };
        // featvals -> [featvals, featval]
        bindReduceHandler(11, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                L<ITree> ls = (L<ITree>)_[0];
          ls.add((ITree)_[1]);
          return ls;
            }
        };
        // trees -> [trees, tree]
        bindReduceHandler(9, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                OM.TFS tfs = new OM.TFS(
                (OM.Flags)_[1],
                S(_[3]),        // type name
                (L<IFeatureValuePair>)_[4]
                );
            int id = N(_[2]);
            _id2ent.put(id, tfs);
            
            if (id == 0)
                _tfs = tfs;
            
            return tfs;
            }
        };
        // struc -> [_BEGIN_STRUC, flags, id, type, featvals, _RPAR]
        bindReduceHandler(28, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                OM.Tag tag = new OM.Tag(
                (OM.Flags)_[1],
                N(_[3])     // tag number
                );
            _tags.add(tag);
            return tag;
            }
        };
        // ref -> [_BEGIN_REF, flags, id, target, _RPAR]
        bindReduceHandler(42, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                String label        = S(_[3]);
        int linkid          = N(_[5]);
        //IEntity content     = _id2ent.get(linkid);
        L<ITree> children   = (L<ITree>)_[6];
        
        OM.Tree t = new OM.Tree(label, children);
        
        _trees.add(new Pair<OM.Tree,Integer>(t,linkid));
        
        return _tree = t;
            }
        };
        // tree -> [_BEGIN_TREE, flags, id, label, arclabel, linkid, trees, _RPAR]
        bindReduceHandler(26, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                _flags.setDifferent();    return null;
            }
        };
        // flag -> [different]
        bindReduceHandler(57, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                _flags.setExpanded();     return null;
            }
        };
        // flag -> [expanded]
        bindReduceHandler(60, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                _flags.setHidden();       return null;
            }
        };
        // flag -> [hidden]
        bindReduceHandler(56, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                _flags.setMultiline();    return null;
            }
        };
        // flag -> [multiline]
        bindReduceHandler(59, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                _flags.setStruckout();    return null;
            }
        };
        // flag -> [struckout]
        bindReduceHandler(58, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                bindRefs();
            
            String title = S(_[1]);
            
            if (_tree != null)
                _helper.adviceResult(title, _tree);
            else if (_tfs != null)
                _helper.adviceResult(title, _tfs);
            else if (_[2] instanceof IList)
                _helper.adviceResult(title, (IList) _[2]);
            else
                throw new NotImplementedException("in datapackage");
            
            return null;
            }
        };
        // datapackage -> [_NEWDATA, windowtitle, structures]
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
        // reentr -> [_BEGIN_REENTR, flags, id, tag, struct, _RPAR]
        bindReduceHandler(27, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[0];
            }
        };
        // arclabel -> [_STRING]
        bindReduceHandler(49, handler);
        // feature -> [_STRING]
        bindReduceHandler(44, handler);
        // functor -> [_STRING]
        bindReduceHandler(45, handler);
        // id -> [_INT]
        bindReduceHandler(50, handler);
        // label -> [_STRING]
        bindReduceHandler(47, handler);
        // linkid -> [_INT]
        bindReduceHandler(51, handler);
        // name -> [_STRING]
        bindReduceHandler(52, handler);
        // struct -> [any]
        bindReduceHandler(23, handler);
        // struct -> [conjunction]
        bindReduceHandler(25, handler);
        // struct -> [disjunction]
        bindReduceHandler(24, handler);
        // struct -> [function]
        bindReduceHandler(19, handler);
        // struct -> [list]
        bindReduceHandler(21, handler);
        // struct -> [ref]
        bindReduceHandler(18, handler);
        // struct -> [relation]
        bindReduceHandler(20, handler);
        // struct -> [set]
        bindReduceHandler(22, handler);
        // struct -> [struc]
        bindReduceHandler(17, handler);
        // structure -> [reentr]
        bindReduceHandler(15, handler);
        // structure -> [struct]
        bindReduceHandler(14, handler);
        // structure -> [tree]
        bindReduceHandler(16, handler);
        // structures -> [structure]
        bindReduceHandler(4, handler);
        // tag -> [_INT]
        bindReduceHandler(54, handler);
        // target -> [_INT]
        bindReduceHandler(55, handler);
        // value -> [_STRING]
        bindReduceHandler(53, handler);
        // windowtitle -> [_STRING]
        bindReduceHandler(46, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[3];
            }
        };
        // tail -> [_BEGIN_TAIL, flags, id, struct, _RPAR]
        bindReduceHandler(35, handler);
        // type -> [_LPAR, flags, id, name, _RPAR]
        bindReduceHandler(41, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _flags = new OM.Flags();
            }
        };
        // flags -> []
        bindReduceHandler(12, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _flags;
            }
        };
        // flags -> [flags, flag]
        bindReduceHandler(13, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new L<IEntity>();
            }
        };
        // structs -> []
        bindReduceHandler(6, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new L<IFeatureValuePair>();
            }
        };
        // featvals -> []
        bindReduceHandler(10, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new L<ITree>();
            }
        };
        // trees -> []
        bindReduceHandler(8, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new OM.FeatVal(
                (OM.Flags)_[1],
                S(_[3]),        // feature
                (IEntity)_[4]   // value
                );
            }
        };
        // featval -> [_BEGIN_FEATVAL, flags, id, feature, struct, _RPAR]
        bindReduceHandler(29, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                throw new NotImplementedException("value-less feature");
            }
        };
        // featval -> [_BEGIN_FEATVAL, flags, id, feature, _RPAR]
        bindReduceHandler(30, handler);
    }
}

