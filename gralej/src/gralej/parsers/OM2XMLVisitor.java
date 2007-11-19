package gralej.parsers;

import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

import gralej.om.*;

public class OM2XMLVisitor implements IVisitor {
    PrintWriter _out;
    Set<Integer> _reentrancies = new TreeSet<Integer>();
    
    OM2XMLVisitor(PrintWriter out) {
        _out = out;
    }
    
    OM2XMLVisitor() {
        this(new PrintWriter(System.out, true));
    }
    
    public void visit(IVisitable visitable) {
        throw new RuntimeException("unknown visitable: " + visitable);
    }
    
    public void visit(IEntity entity) {
        throw new RuntimeException("unknown entity: " + entity);
    }
    
    public void visit(IFeatureValuePair featVal) {
        _out.println("<f name='" + featVal.feature() + "'>");
        featVal.value().accept(this);
        _out.println("</f>");
    }
    
    public void visit(IList ls) {
        _out.println("<ls>");
        for (IEntity e : ls.elements())
            e.accept(this);
        _out.println("</ls>");
    }
    
    public void visit(ITag tag) {
        _out.print("<tag ");
        if (_reentrancies.add(tag.number())) {
        	_out.print("id='" + tag.number() + "'>");
            tag.target().accept(this);
            _out.println("</tag>");
        }
        else {
        	_out.println("ref='" + tag.number() + "'/>");
        }
    }
    
    public void visit(IAny any) {
        _out.println("<any>" + any.value() + "</any>");
    }
    
    public void visit(ITypedFeatureStructure tfs) {
        _out.println("<tfs type='" + tfs.typeName() + "'>");
        for (IFeatureValuePair featVal : tfs.featureValuePairs())
            featVal.accept(this);
        _out.println("</tfs>");
    }
    
    public void visit(ITree tree) {
        _out.println("<tree label='" + tree.label() + "'>");
        _out.println("<content>");
        tree.content().accept(this);
        _out.println("</content>");
        for (ITree child : tree.children())
            child.accept(this);
        _out.println("</tree>");
    }
}
