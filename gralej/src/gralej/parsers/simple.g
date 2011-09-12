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
%

S:
    | Protocol_opt S1
    .


S1:
    DataPackage
        { return L_new(_, 0); }
    
    | S1 DataPackage
        { return L_add(_, 0, 1); }
    .


Protocol_opt:
    | '@' Id
        {
            String protocol = _[1].toString();
            if (!"gralej".equals(protocol))
                throw new RuntimeException("Unknown protocol: " + protocol);
            return null;
        }
    .


DataPackage:    
    '<' Id              # title
        Tree_or_TFS     # contents
        Ineqs_opt       # inequations
        Residue_opt     # residuals
    '>'
        {
            String title = _[1].toString();
            IEntity model = (IEntity)_[2];
            IneqsAndResidue ineqsAndResidue = IneqsAndResidue.getInstance((L)_[3], (L)_[4]);

            bindTags(model, ineqsAndResidue);

            return new DataPackage(title, model, new char[0], StreamInfo.GRALEJ_SIMPLE, ineqsAndResidue);
        }
    .


Tree_or_TFS:
    Tree
    | TFS
    .


Ineqs_opt:
    | '~' Relation_seq
        { return _[1]; }
    .


Residue_opt:
    | '/' Relation_seq
        { return _[1]; }
    .


Relation_seq:
    Relation
        { return L_new(_, 0); }
        
    | Relation_seq Relation
        { return L_add(_, 0, 1); }
    .


Tree:
    '{' TreeLabel_opt TreeContent_opt Tree_seq_opt '}'
        {
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
    .


TreeLabel_opt:
        { return null; }

    | ':' Id
        { return _[1]; }
    .


TreeContent_opt:
    | TFS
    .


Tree_seq_opt:
    | Tree_seq
    .


Tree_seq:
    Tree
        { return L_new(_, 0); }
    
    | Tree_seq Tree
        { return L_add(_, 0, 1); }
    .


Relation:
    Id '(' TFS_seq ')'
        { return F.newRelation(s(_,0), (L)_[2]); }
    .


Atom:
    '@' Id
        {
            String s = s(_,1);
            if (s.startsWith(LRS_PREFIX))
                return F.newLRSExpr(s.substring(LRS_PREFIX.length()));
            return F.newAny(s); 
        }
    .


List:
    "[]"
        { return F.newList(); }
    
    | '[' TFS_seq ListTail_opt ']'
        {
            IList ls = F.newList((L)_[1]);
            ls.setTail((IEntity)_[2]);
            return ls;
        }
    .


ListTail_opt:
    | '|' TFS
        { return _[1]; }
    .


TFS_seq:
    TFS
        { return L_new(_,0); }
    
    | TFS_seq ',' TFS
        { return L_add(_,0,2); }
    .


TFS:
    Id
        { return F.newTFS(s(_,0)); }
    
    | Id '(' FeatVal_seq ')'
        { return F.newTFS(s(_,0), (L)_[2]); }
    
    | '*' '(' FeatVal_seq ')'
        { return F.newTFS((IType) null, (L)_[2]); }
    
    | Relation
    | List
    | Atom
    | Tag
    | Reentr
    .


FeatVal_seq:
    FeatVal
        { return L_new(_,0); } 
    
    | FeatVal_seq ',' FeatVal
        { return L_add(_,0,2); }
    .


FeatVal:
    Id ':' TFS
        { return F.newFeatVal(s(_,0), (IEntity)_[2]); }
    
    | Id ':' FeatVal
        {
            ITypedFeatureStructure tfs = F.newTFS((IType)null);
            tfs.addFeatureValue((IFeatureValuePair)_[2]);
            return F.newFeatVal(s(_,0), tfs);
        }
    .

    
Tag:
    '$' _INT
        { return F.newTag(Integer.parseInt(s(_,1))); }
    .


Reentr:
    Tag '=' TFS
        {
            ITag tag = (ITag)_[0];
            tag.setTarget((IEntity)_[2]);
            return tag;
        }
    .


Id:
    _ID
    | _DQ_STRING
    | _SQ_STRING
    .

%
}

