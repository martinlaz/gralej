package gralej.parsers;

import java.util.Set;
import java.util.TreeSet;

import gralej.om.*;


public class OM2LaTeXVisitor extends AbstractVisitor {
	
	StringBuffer _out;
    Set<Integer> _reentrancies = new TreeSet<Integer>();
	
    public String output (IVisitable root) {
    	_out = new StringBuffer();
    	root.accept(this);
    	return _out.toString();
    }
    
    // TODO convert Strings to TeX (basically: escape some characters)
    private String texify (String in) {
    	String out = in;
    	out = out.replace("_", "\\_");
    	return out;
    }


    public void visit(IVisitable visitable) {
        throw new RuntimeException("unknown visitable: " + visitable);
    }
    
    public void visit(IEntity entity) {
        throw new RuntimeException("unknown entity: " + entity);
    }
    
    public void visit(IFeatureValuePair featVal) {
        _out.append(texify(featVal.feature()) + " & ");
        featVal.value().accept(this);
        _out.append("\\\\\n");
    }
    
    public void visit(IList ls) {
        _out.append("\\<");
        for (IEntity e : ls.elements())
            e.accept(this);
        _out.append("\\>\\\\\n");
    }
    
    public void visit(ITag tag) {
        if (_reentrancies.add(tag.number())) {
        	_out.append("\\@{" + tag.number() + "}");
            tag.target().accept(this);
        }
        else {
        	_out.append("\\@{" + tag.number() + "}");
        }
    }
    
    public void visit(IAny any) {
        _out.append(any.value()); // TODO vacuous
    }
    
    public void visit(ITypedFeatureStructure tfs) {
    	boolean complex = tfs.featureValuePairs().iterator().hasNext();
    	if (complex) _out.append("\\[\\tp{");
        _out.append(texify(tfs.typeName()));
        if (complex) _out.append("}");
        _out.append("\\\\\n");
        
        for (IFeatureValuePair featVal : tfs.featureValuePairs())
            featVal.accept(this);
        
        if (complex) _out.append("\\]\n");
    }
    
    public void visit(ITree tree) { // TODO vacuous
        tree.content().accept(this);
        for (ITree child : tree.children())
            child.accept(this);

    }

}
