package gralej.parsers;

import gralej.om.*;
import gralej.controller.StreamInfo;
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
    
    private static void bindTags(IEntity model, IneqsAndResidue ineqsAndResidue) {
        TagBindingVisitor tagBinder = new TagBindingVisitor();
        tagBinder.process(model);
        for (IRelation rel : ineqsAndResidue.ineqs())
            tagBinder.process(rel);
        for (IRelation rel : ineqsAndResidue.residue())
            tagBinder.process(rel);
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
        bindReduceHandler(27, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                ITag tag = (ITag)_[0];
            tag.setTarget((IEntity)_[2]);
            return tag;
            }
        };
        // Reentr -> Tag '=' TFS
        bindReduceHandler(45, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                ITree tree;
            String treeLabel = null;
            if (_[1] != null)
                treeLabel = s(_,1);
            IEntity tfs = (IEntity)_[2];
            if (treeLabel == null && tfs == null)
                throw new RuntimeException(
                    "Both the tree label and the tree content are empty; at least one of them must be specified");
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
        bindReduceHandler(15, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                ITypedFeatureStructure tfs = F.newTFS((IType)null);
            tfs.addFeatureValue((IFeatureValuePair)_[2]);
            return F.newFeatVal(s(_,0), tfs);
            }
        };
        // FeatVal -> Id ':' FeatVal
        bindReduceHandler(43, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                String protocol = _[1].toString();
            if (!"gralej".equals(protocol))
                throw new RuntimeException("Unknown protocol: " + protocol);
            return null;
            }
        };
        // Protocol_opt -> '@' Id
        bindReduceHandler(5, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                String s = s(_,1);
            if (s.startsWith(LRS_PREFIX))
                return F.newLRSExpr(s.substring(LRS_PREFIX.length()));
            return F.newAny(s);
            }
        };
        // Atom -> '@' Id
        bindReduceHandler(25, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                String title = _[1].toString();
            IEntity model = (IEntity)_[2];
            IneqsAndResidue ineqsAndResidue = IneqsAndResidue.getInstance((L)_[3], (L)_[4]);

            bindTags(model, ineqsAndResidue);

            return new DataPackage(title, model, new char[0], StreamInfo.GRALEJ_SIMPLE, ineqsAndResidue);
            }
        };
        // DataPackage -> '<' Id Tree_or_TFS Ineqs_opt Residue_opt '>'
        bindReduceHandler(6, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newFeatVal(s(_,0), (IEntity)_[2]);
            }
        };
        // FeatVal -> Id ':' TFS
        bindReduceHandler(42, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newList();
            }
        };
        // List -> '[' ']'
        bindReduceHandler(26, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newRelation(s(_,0), (L)_[2]);
            }
        };
        // Relation -> Id '(' TFS_seq ')'
        bindReduceHandler(24, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTFS((IType) null, (L)_[2]);
            }
        };
        // TFS -> '*' '(' FeatVal_seq ')'
        bindReduceHandler(34, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTFS(s(_,0));
            }
        };
        // TFS -> Id
        bindReduceHandler(32, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTFS(s(_,0), (L)_[2]);
            }
        };
        // TFS -> Id '(' FeatVal_seq ')'
        bindReduceHandler(33, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return F.newTag(Integer.parseInt(s(_,1)));
            }
        };
        // Tag -> '$' _INT
        bindReduceHandler(44, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return L_add(_, 0, 1);
            }
        };
        // Relation_seq -> Relation_seq Relation
        bindReduceHandler(14, handler);
        // S1 -> S1 DataPackage
        bindReduceHandler(3, handler);
        // Tree_seq -> Tree_seq Tree
        bindReduceHandler(23, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return L_add(_,0,2);
            }
        };
        // FeatVal_seq -> FeatVal_seq ',' FeatVal
        bindReduceHandler(41, handler);
        // TFS_seq -> TFS_seq ',' TFS
        bindReduceHandler(31, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return L_new(_, 0);
            }
        };
        // Relation_seq -> Relation
        bindReduceHandler(13, handler);
        // S1 -> DataPackage
        bindReduceHandler(2, handler);
        // Tree_seq -> Tree
        bindReduceHandler(22, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return L_new(_,0);
            }
        };
        // FeatVal_seq -> FeatVal
        bindReduceHandler(40, handler);
        // TFS_seq -> TFS
        bindReduceHandler(30, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[0];
            }
        };
        // Id -> _DQ_STRING
        bindReduceHandler(47, handler);
        // Id -> _ID
        bindReduceHandler(46, handler);
        // Id -> _SQ_STRING
        bindReduceHandler(48, handler);
        // TFS -> Atom
        bindReduceHandler(37, handler);
        // TFS -> List
        bindReduceHandler(36, handler);
        // TFS -> Reentr
        bindReduceHandler(39, handler);
        // TFS -> Relation
        bindReduceHandler(35, handler);
        // TFS -> Tag
        bindReduceHandler(38, handler);
        // TreeContent_opt -> TFS
        bindReduceHandler(19, handler);
        // Tree_or_TFS -> TFS
        bindReduceHandler(8, handler);
        // Tree_or_TFS -> Tree
        bindReduceHandler(7, handler);
        // Tree_seq_opt -> Tree_seq
        bindReduceHandler(21, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return _[1];
            }
        };
        // Ineqs_opt -> '~' Relation_seq
        bindReduceHandler(10, handler);
        // ListTail_opt -> '|' TFS
        bindReduceHandler(29, handler);
        // Residue_opt -> '/' Relation_seq
        bindReduceHandler(12, handler);
        // TreeLabel_opt -> ':' Id
        bindReduceHandler(17, handler);

        handler = new tomato.ReduceHandler() {
            public Object execute(Object[] _) {
                return null;
            }
        };
        // TreeLabel_opt -> 
        bindReduceHandler(16, handler);
    }
}


