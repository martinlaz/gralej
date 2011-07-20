package gralej.parsers;

import gralej.om.*;
import static gralej.Globals.LRS_PREFIX;

import java.util.LinkedList;

@SuppressWarnings("unchecked")
public class SimpleFormatGrammarHandler extends tomato.GrammarHandler {
    private static class L extends LinkedList {}

    private final static gralej.om.EntityFactory F = EntityFactory.getInstance();

    private static String s(Object[] _, int i) { return _[i].toString(); }
    
    private static L L_new(Object[] _, int i) {
        L ls = new L();
        ls.add(_[i]);
        return ls;
    }
    
    private static L L_add(Object[] _, int i, int j) {
        L ls = ((L)_[i]);
        ls.add(_[j]);
        return ls;
    }
    
    private static IEntity bindTags(IEntity ent) {
        new TagBindingVisitor(ent);
        return ent;
    }

    public static class Terminals extends tomato.AbstractTerminals {
        public Terminals(tomato.Grammar g) {
            super(g);
        }
        public tomato.Terminal _DQ_STRING;
        public tomato.Terminal _ID;
        public tomato.Terminal _INT;
        public tomato.Terminal _SQ_STRING;
        public tomato.Terminal __CHAR_123;
        public tomato.Terminal __CHAR_124;
        public tomato.Terminal __CHAR_125;
        public tomato.Terminal __CHAR_126;
        public tomato.Terminal __CHAR_36;
        public tomato.Terminal __CHAR_40;
        public tomato.Terminal __CHAR_41;
        public tomato.Terminal __CHAR_42;
        public tomato.Terminal __CHAR_44;
        public tomato.Terminal __CHAR_47;
        public tomato.Terminal __CHAR_58;
        public tomato.Terminal __CHAR_60;
        public tomato.Terminal __CHAR_61;
        public tomato.Terminal __CHAR_62;
        public tomato.Terminal __CHAR_64;
        public tomato.Terminal __CHAR_91;
        public tomato.Terminal __CHAR_93;
    } // end of Terminals

    protected void bindReduceHandlers() {
        tomato.ReduceHandler handler;

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                IList ls = F.newList((L)_[1]);
            ls.setTail((IEntity)_[2]);
            return ls;
            }
        };
        // List -> '[' TFS_seq ListTail_opt ']'
        bindReduceHandler(25, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                ITag tag = (ITag)_[0];
            tag.setTarget((IEntity)_[2]);
            return tag;
            }
        };
        // Reentr -> Tag '=' TFS
        bindReduceHandler(43, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                ITree tree;
            String treeLabel = null;
            if (_[1] != null)
                treeLabel = s(_,1);
            IEntity tfs = (IEntity)_[2];
            if (treeLabel == null && tfs == null)
                throw new RuntimeException(
                    "At least one of the tree label or the tree contents must be specified");
            L subTrees = (L)_[3];
            if (subTrees == null)
                tree = F.newTree(treeLabel);
            else
                tree = F.newTree(treeLabel, subTrees);
            if (tfs != null)
                tree.setContent(tfs);
            return tree;
            }
        };
        // Tree -> '{' TreeLabel_opt TreeContent_opt Tree_seq_opt '}'
        bindReduceHandler(13, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                ITypedFeatureStructure tfs = F.newTFS((IType)null);
            tfs.addFeatureValue((IFeatureValuePair)_[2]);
            return F.newFeatVal(s(_,0), tfs);
            }
        };
        // FeatVal -> Id ':' FeatVal
        bindReduceHandler(41, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                String s = s(_,1);
            if (s.startsWith(LRS_PREFIX))
                return F.newLRSExpr(s.substring(LRS_PREFIX.length()));
            return F.newAny(s);
            }
        };
        // Atom -> '@' _DQ_STRING
        bindReduceHandler(23, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newFeatVal(s(_,0), (IEntity)_[2]);
            }
        };
        // FeatVal -> Id ':' TFS
        bindReduceHandler(40, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newList();
            }
        };
        // List -> '[' ']'
        bindReduceHandler(24, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newRelation(s(_,0), (L)_[2]);
            }
        };
        // Relation -> Id '(' TFS_seq ')'
        bindReduceHandler(22, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTFS((IType) null, (L)_[2]);
            }
        };
        // TFS -> '*' '(' FeatVal_seq ')'
        bindReduceHandler(32, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTFS(s(_,0));
            }
        };
        // TFS -> Id
        bindReduceHandler(30, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTFS(s(_,0), (L)_[2]);
            }
        };
        // TFS -> Id '(' FeatVal_seq ')'
        bindReduceHandler(31, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTag(Integer.parseInt(s(_,1)));
            }
        };
        // Tag -> '$' _INT
        bindReduceHandler(42, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return L_add(_, 0, 1);
            }
        };
        // Relation_seq -> Relation_seq Relation
        bindReduceHandler(12, handler);
        // S1 -> S1 DataPackage
        bindReduceHandler(3, handler);
        // Tree_seq -> Tree_seq Tree
        bindReduceHandler(21, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return L_add(_,0,2);
            }
        };
        // FeatVal_seq -> FeatVal_seq ',' FeatVal
        bindReduceHandler(39, handler);
        // TFS_seq -> TFS_seq ',' TFS
        bindReduceHandler(29, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return L_new(_, 0);
            }
        };
        // Relation_seq -> Relation
        bindReduceHandler(11, handler);
        // S1 -> DataPackage
        bindReduceHandler(2, handler);
        // Tree_seq -> Tree
        bindReduceHandler(20, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return L_new(_,0);
            }
        };
        // FeatVal_seq -> FeatVal
        bindReduceHandler(38, handler);
        // TFS_seq -> TFS
        bindReduceHandler(28, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[0];
            }
        };
        // Id -> _DQ_STRING
        bindReduceHandler(45, handler);
        // Id -> _ID
        bindReduceHandler(44, handler);
        // Id -> _SQ_STRING
        bindReduceHandler(46, handler);
        // S -> S1
        bindReduceHandler(1, handler);
        // TFS -> Atom
        bindReduceHandler(35, handler);
        // TFS -> List
        bindReduceHandler(34, handler);
        // TFS -> Reentr
        bindReduceHandler(37, handler);
        // TFS -> Relation
        bindReduceHandler(33, handler);
        // TFS -> Tag
        bindReduceHandler(36, handler);
        // TreeContent_opt -> TFS
        bindReduceHandler(17, handler);
        // Tree_or_TFS -> TFS
        bindReduceHandler(6, handler);
        // Tree_or_TFS -> Tree
        bindReduceHandler(5, handler);
        // Tree_seq_opt -> Tree_seq
        bindReduceHandler(19, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[1];
            }
        };
        // ListTail_opt -> '|' TFS
        bindReduceHandler(27, handler);
        // TreeLabel_opt -> ':' Id
        bindReduceHandler(15, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return null;
            }
        };
        // TreeLabel_opt -> 
        bindReduceHandler(14, handler);
    }
}


