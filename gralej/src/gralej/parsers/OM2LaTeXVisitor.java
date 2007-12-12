package gralej.parsers;

import java.util.Set;
import java.util.TreeSet;

import gralej.om.*;

public class OM2LaTeXVisitor extends AbstractVisitor {

    StringBuffer _out;
    Set<Integer> _reentrancies = new TreeSet<Integer>();

    public String output(IVisitable root) {
        _out = new StringBuffer();
        root.accept(this);
        return _out.toString();
    }

    private String texify(String in) {
        String out = in;
        out = out.replace("_", "\\_"); // more?
        return out;
    }

    public void visit(IVisitable visitable) {
        throw new RuntimeException("unknown visitable: " + visitable);
    }

    public void visit(IEntity entity) {
        throw new RuntimeException("unknown entity: " + entity);
    }

    public void visit(IFeatureValuePair featVal) {
        if (featVal.isHidden())
            return;
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
        /*
         * if (_reentrancies.add(tag.number())) { _out.append("\\@{" +
         * tag.number() + "}"); tag.target().accept(this); } else {
         * _out.append("\\@{" + tag.number() + "}"); }
         */
        _out.append("\\@{" + tag.number() + "}");
        if (tag.isExpanded()) {
            tag.target().accept(this);
        }
    }

    public void visit(IAny any) {
        _out.append(any.value()); // TODO vacuous
    }

    public void visit(ITypedFeatureStructure tfs) {
        boolean complex = tfs.featureValuePairs().iterator().hasNext();
        if (complex)
            _out.append("\\[\\tp{");
        _out.append(texify(tfs.typeName()));
        if (complex)
            _out.append("}");
        _out.append("\\\\\n");

        for (IFeatureValuePair featVal : tfs.featureValuePairs())
            featVal.accept(this);

        if (complex)
            _out.append("\\]\n");
    }

    public void visit(ITree tree) {
        if (!tree.isLeaf())
            _out.append("\\begin{bundle}{");
        _out.append("\\begin{Avm}{" + tree.label() + "}\n");
        tree.content().accept(this);
        _out.append("\\end{Avm}\n");
        if (!tree.isLeaf())
            _out.append("}\n");
        for (ITree child : tree.children()) {
            _out.append("\\chunk{");
            if (child instanceof ITree) {
                child.accept(this);
            } else {
                _out.append("\\begin{Avm}\n");
                child.accept(this);
                _out.append("\\end{Avm}\n");
            }
            _out.append("}\n");
        }
        if (!tree.isLeaf())
            _out.append("\\end{bundle}\n");

    }

}
