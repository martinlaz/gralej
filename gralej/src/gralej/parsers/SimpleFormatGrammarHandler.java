package gralej.parsers;

import gralej.om.*;
import static gralej.Globals.LRS_PREFIX;

import java.util.LinkedList;

@SuppressWarnings("unchecked")
public class SimpleFormatGrammarHandler extends tomato.GrammarHandler {
    private static String S(Object[] _, int i) { return _[i].toString(); }
    private static gralej.om.EntityFactory F = EntityFactory.getInstance();
    
    private static class L extends LinkedList {}
    
    private static L Lnew(Object[] _, int i) {
        L ls = new L(); ls.add(_[i]); return ls;
    }
    
    private static L Ladd(Object[] _, int i, int j) {
        L ls = ((L)_[i]); ls.add(_[j]); return ls;
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
        public tomato.Terminal __CHAR_36;
        public tomato.Terminal __CHAR_40;
        public tomato.Terminal __CHAR_41;
        public tomato.Terminal __CHAR_42;
        public tomato.Terminal __CHAR_44;
        public tomato.Terminal __CHAR_45;
        public tomato.Terminal __CHAR_58;
        public tomato.Terminal __CHAR_59;
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
        bindReduceHandler(21, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                ITag tag = (ITag)_[0];
            tag.setTarget((IEntity)_[2]);
            return tag;
            }
        };
        // Reentr -> Tag ':' TFS
        bindReduceHandler(40, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                ITree tree;
            String treeLabel = null;
            if (_[0] != null)
                treeLabel = S(_,0);
            Object[] treeContent = (Object[])_[2];
            IEntity tfs = null;
            L subTrees = null;
            if (treeContent != null) {
                tfs = (IEntity) treeContent[0];
                subTrees = (L) treeContent[1];
            }
            if (subTrees == null)
                tree = F.newTree(treeLabel);
            else
                tree = F.newTree(treeLabel, subTrees);
            tree.setContent(tfs);
            return tree;
            }
        };
        // Tree -> TreeLabel_opt '{' TreeContent_opt '}'
        bindReduceHandler(6, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                ITypedFeatureStructure tfs = F.newTFS((IType)null);
            tfs.addFeatureValue((IFeatureValuePair)_[2]);
            return F.newFeatVal(S(_,0), tfs);
            }
        };
        // FeatVal -> Id ':' FeatVal
        bindReduceHandler(38, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                String s = S(_,1);
            if (s.startsWith(LRS_PREFIX))
                return F.newLRSExpr(s.substring(LRS_PREFIX.length()));
            return F.newAny(s);
            }
        };
        // Atom -> '@' _DQ_STRING
        bindReduceHandler(19, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newFeatVal(S(_,0), (IEntity)_[2]);
            }
        };
        // FeatVal -> Id ':' TFS
        bindReduceHandler(37, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newList();
            }
        };
        // List -> '[' ']'
        bindReduceHandler(20, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newRelation(S(_,0), (L)_[2]);
            }
        };
        // Relation -> Id '(' TFS_seq ')'
        bindReduceHandler(18, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTFS((IType) null, (L)_[2]);
            }
        };
        // TFS -> '*' '(' FeatVal_seq ')'
        bindReduceHandler(28, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTFS(S(_,0));
            }
        };
        // TFS -> Id
        bindReduceHandler(26, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTFS(S(_,0), (L)_[2]);
            }
        };
        // TFS -> Id '(' FeatVal_seq ')'
        bindReduceHandler(27, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTag(Integer.parseInt(S(_,1)));
            }
        };
        // Tag -> '$' _INT
        bindReduceHandler(39, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return Ladd(_,0,1);
            }
        };
        // S1 -> S1 S ';'
        bindReduceHandler(3, handler);
        // Tree_seq -> Tree_seq Tree
        bindReduceHandler(17, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return Ladd(_,0,2);
            }
        };
        // FeatVal_seq -> FeatVal_seq ',' FeatVal
        bindReduceHandler(36, handler);
        // TFS_seq -> TFS_seq ',' TFS
        bindReduceHandler(25, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return Lnew(_,0);
            }
        };
        // FeatVal_seq -> FeatVal
        bindReduceHandler(35, handler);
        // S1 -> S ';'
        bindReduceHandler(2, handler);
        // TFS_seq -> TFS
        bindReduceHandler(24, handler);
        // Tree_seq -> Tree
        bindReduceHandler(16, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[0];
            }
        };
        // Id -> _DQ_STRING
        bindReduceHandler(42, handler);
        // Id -> _ID
        bindReduceHandler(41, handler);
        // Id -> _SQ_STRING
        bindReduceHandler(43, handler);
        // S0 -> S1
        bindReduceHandler(1, handler);
        // TFS -> Atom
        bindReduceHandler(31, handler);
        // TFS -> List
        bindReduceHandler(30, handler);
        // TFS -> Reentr
        bindReduceHandler(33, handler);
        // TFS -> Relation
        bindReduceHandler(29, handler);
        // TFS -> Tag
        bindReduceHandler(32, handler);
        // TFS_opt -> TFS
        bindReduceHandler(13, handler);
        // TreeContent_opt -> TreeContent
        bindReduceHandler(10, handler);
        // TreeLabel_opt -> Id
        bindReduceHandler(8, handler);
        // Tree_seq_opt -> Tree_seq
        bindReduceHandler(15, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[1];
            }
        };
        // ListTail_opt -> '|' TFS
        bindReduceHandler(23, handler);
        // TFS -> '(' TFS ')'
        bindReduceHandler(34, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return bindTags((IEntity)_[0]);
            }
        };
        // S -> TFS
        bindReduceHandler(4, handler);
        // S -> Tree
        bindReduceHandler(5, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return java.util.Collections.EMPTY_LIST;
            }
        };
        // S0 -> 
        bindReduceHandler(0, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return new Object[] {_[0], _[1]};
            }
        };
        // TreeContent -> TFS_opt Tree_seq_opt
        bindReduceHandler(11, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return null;
            }
        };
        // TFS_opt -> '-'
        bindReduceHandler(12, handler);
        // TreeLabel_opt -> '-'
        bindReduceHandler(7, handler);
    }
}


