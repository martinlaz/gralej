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
%

S0 ->
        { return java.util.Collections.EMPTY_LIST; }
    | S1
    .

S1 ->
    S
        { return Lnew(_,0); }
    | S1 S
        { return Ladd(_,0,1); }
    .

S ->
    TFS
        { return bindTags((IEntity)_[0]); }
    | Tree
        { return bindTags((IEntity)_[0]); }
    .

Tree ->
    '{' TreeLabel_opt TreeContent_opt Tree_seq_opt '}'
        {
            ITree tree;
            String treeLabel = null;
            if (_[1] != null)
                treeLabel = S(_,1);
            IEntity tfs = (IEntity)_[2];
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

TreeLabel_opt ->
        { return null; }
    | ':' Id
        { return _[1]; }
    .

TreeContent_opt ->
    | TFS
    .

Tree_seq_opt ->
    | Tree_seq
    .

Tree_seq ->
    Tree
        { return Lnew(_,0); }
    | Tree_seq Tree
        { return Ladd(_,0,1); }
    .

Relation ->
    Id '(' TFS_seq ')'
        { return F.newRelation(S(_,0), (L)_[2]); }
    .

Atom ->
    '@' _DQ_STRING
        {
            String s = S(_,1);
            if (s.startsWith(LRS_PREFIX))
                return F.newLRSExpr(s.substring(LRS_PREFIX.length()));
            return F.newAny(s); 
        }
    .

List ->
    "[]"
        { return F.newList(); }
    | '[' TFS_seq ListTail_opt ']'
        {
            IList ls = F.newList((L)_[1]);
            ls.setTail((IEntity)_[2]);
            return ls;
        }
    .

ListTail_opt ->
    | '|' TFS
        { return _[1]; }
    .

TFS_seq ->
    TFS
        { return Lnew(_,0); }
    | TFS_seq ',' TFS
        { return Ladd(_,0,2); }
    .

TFS ->
    Id
        { return F.newTFS(S(_,0)); }
    | Id '(' FeatVal_seq ')'
        { return F.newTFS(S(_,0), (L)_[2]); }
    | '*' '(' FeatVal_seq ')'
        { return F.newTFS((IType) null, (L)_[2]); }
    | Relation
    | List
    | Atom
    | Tag
    | Reentr
#    | '(' TFS ')'
#        { return _[1]; }
    .

FeatVal_seq ->
    FeatVal
        { return Lnew(_,0); } 
    | FeatVal_seq ',' FeatVal
        { return Ladd(_,0,2); }
    .

FeatVal ->
    Id ':' TFS
        { return F.newFeatVal(S(_,0), (IEntity)_[2]); }
    | Id ':' FeatVal
        {
            ITypedFeatureStructure tfs = F.newTFS((IType)null);
            tfs.addFeatureValue((IFeatureValuePair)_[2]);
            return F.newFeatVal(S(_,0), tfs);
        }
    .
    
Tag ->
    '$' _INT
        { return F.newTag(Integer.parseInt(S(_,1))); }
    .
Reentr ->
    Tag '=' TFS
        {
            ITag tag = (ITag)_[0];
            tag.setTarget((IEntity)_[2]);
            return tag;
        }
    .
Id ->
    _ID
    | _DQ_STRING
    | _SQ_STRING
    .

%
}

