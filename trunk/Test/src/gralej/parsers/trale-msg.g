package gralej.parsers;

import java.util.Map;
import java.util.TreeMap;

import gralej.om.*;

import tomato.GrammarHandler;
import tomato.Token;

public class TraleMsgHandler extends GrammarHandler {
    static class L<T> extends java.util.LinkedList<T> {
    }
    
    OM.Flags _flags;
    
    Map<Integer,IEntity> _id2ent  = new TreeMap<Integer,IEntity>();
    Map<Integer,IEntity> _tag2ent = new TreeMap<Integer,IEntity>();
    
    ITree _tree;
    ITypedFeatureStructure _tfs;
    L<OM.Tag> _tags = new L<OM.Tag>();
    
    void bindTags() {
        for (OM.Tag tag : _tags)
            tag.setTarget(_tag2ent.get(tag.number()));
        _tags.clear();
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
%

########################
## Root ################
########################
start ->
    datapackages { System.out.println("</session>"); return null; } .

datapackages -> 
        { System.out.println("<session>"); return null; }
    |   datapackages datapackage .

datapackage
  ->  _NEWDATA windowtitle structures _NEWLINE
        {{
            bindTags();
            
            if (_tree != null)
                //System.err.println("tree");
                _tree.accept(new OM2XMLVisitor());
            else if (_tfs != null)
                //System.err.println("struc");
                _tfs.accept(new OM2XMLVisitor());
            else
                System.err.println("<unknown>");
            
            _tfs  = null;
            _tree = null;
            
            return null;
        }}
  .
########################
## Sequences ###########
########################
structures
  ->  structure | structures structure .
structs
  ->    { return new L<IEntity>(); } 
  |  structs struct 
        { L<IEntity> ls = (L<IEntity>)_[0];
          ls.add((IEntity)_[1]);
          return ls;
        }
  .
trees
  ->    { return new L<ITree>(); }
  |  trees tree
        { L<ITree> ls = (L<ITree>)_[0];
          ls.add((ITree)_[1]);
          return ls;
        }
  .
featvals
  ->    { return new L<IFeatureValuePair>(); }
  |   featvals featval
        { L<IFeatureValuePair> ls = (L<IFeatureValuePair>)_[0];
          ls.add((IFeatureValuePair)_[1]);
          return ls;
        }
  .
flags
  ->  { _flags = new OM.Flags(); return null; } | flags flag 
  .
########################
## Structures ##########
########################
structure
  ->  struct | reentr | tree
  .

struct
  ->  struc
  | ref 
  | function 
  | relation 
  | list 
  | set 
  | any 
  | disjunction 
  | conjunction
  .

tree
  ->  _BEGIN_TREE flags id label arclabel linkid trees _RPAR
    {
        String label        = S(_[3]);
        int linkid          = N(_[5]);
        IEntity content     = _id2ent.get(linkid);
        L<ITree> children   = (L<ITree>)_[6];
        
        return _tree = new OM.Tree(label, content, children);
    }
  .

reentr
  ->  _BEGIN_REENTR flags id tag struct _RPAR
       {{
          if (_[4] != null) {
            int id  = N(_[2]);
            int tag = N(_[3]);
            IEntity e = (IEntity)_[4];
            
            _id2ent.put(id, e);
            _tag2ent.put(tag, e);
          }
          return null;
       }}
  .

struc
  ->  _BEGIN_STRUC flags id type featvals _RPAR
        {
            _tfs = new OM.TFS(
                _flags,
                S(_[3]),        // type name
                (L<IFeatureValuePair>)_[4]
                );
            _id2ent.put(N(_[2]), _tfs);
            return _tfs;
        }
  .

featval
  ->  _BEGIN_FEATVAL flags id feature struct _RPAR
        {
            return new OM.FeatVal(
                _flags,
                S(_[3]),        // feature
                (IEntity)_[4]   // value
                );
        }
   |  _BEGIN_FEATVAL flags id feature _RPAR 
        { throw new NotImplementedException("value-less feature"); }
  .

disjunction
  ->  _BEGIN_DISJ flags id struct struct structs _RPAR . 

conjunction
  ->  _BEGIN_CONJ flags id struct struct structs _RPAR .

list
  ->  _BEGIN_LIST flags id structs tail _RPAR
        {
            L<IEntity> structs  = (L<IEntity>)_[3];
            IEntity tail        = (IEntity)_[4];
            
            if (tail != null)
                structs.add(tail);
            
            IList ls = new OM.List(_flags, structs);
            _id2ent.put(N(_[2]), ls);
            return ls;
        }
  .

tail
  ->  | _BEGIN_TAIL flags id struct _RPAR 
            { return _[3]; }
  .

set
  ->  _BEGIN_SET flags id structs rest _RPAR .

rest
  ->  | _BEGIN_REST flags id struct _RPAR .

function
  ->  _BEGIN_FUNCT flags id type functor structs _RPAR .

relation
  ->  _BEGIN_REL flags id functor structs _RPAR .

type
  ->  _LPAR flags id name _RPAR
        { return _[3]; }
  .

ref
  ->  _BEGIN_REF flags id target _RPAR
        {
            OM.Tag tag = new OM.Tag(
                _flags,
                N(_[3])     // tag number
                );
            _tags.add(tag);
            return tag;
        }
  .

any
  ->  _BEGIN_ANY flags id value _RPAR
        {
            IAny any = new OM.Any(
                _flags,
                S(_[3])
                );
            _id2ent.put(N(_[2]), any);
            return any;
        }
  .

########################
# Values ###############
########################
feature     ->  _STRING .
functor     ->  _STRING .
windowtitle ->  _STRING .
label       ->	_STRING .
arclabel    -> | _STRING .
id          ->	_INT.
linkid      ->	_INT.
name        -> _STRING .
value       -> _STRING .
tag         -> _INT .
target      -> _INT .

########################
# Flags ################
########################
flag        ->
        hidden      { _flags.setHidden();       return null; }
    |   different   { _flags.setDifferent();    return null; }
    |   struckout   { _flags.setStruckout();    return null; }
    |   multiline   { _flags.setMultiline();    return null; }
    |   expanded    { _flags.setExpanded();     return null; }
    .

hidden      {} -> _PLUS .
different   {} -> _STAR .
struckout   {} -> _MINUS .
multiline   {} -> _PIPE .
expanded    {} -> _LT .

%
}
