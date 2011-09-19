// $Id$
//
// Copyright (C) 2011, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.parsers;

import java.util.List;
import gralej.util.Log;
import java.io.PrintStream;
import gralej.om.IAny;
import gralej.om.IEntity;
import gralej.om.IFeatureValuePair;
import gralej.om.IList;
import gralej.om.IRelation;
import gralej.om.ITable;
import gralej.om.ITag;
import gralej.om.ITree;
import gralej.om.IType;
import gralej.om.ITypedFeatureStructure;
import gralej.om.IVisitable;
import gralej.om.IVisitor;
import gralej.om.lrs.ILRSExpr;
import java.util.HashSet;
import java.util.Set;

import static gralej.Globals.LRS_PREFIX;

/**
 *
 * @author Martin Lazarov
 */
public final class DataPackage2GralejFormatVisitor implements IVisitor {

    private PrintStream _out;
    private Set<Integer> _tags = new HashSet<Integer>();
    private int _indentLevel;

    public DataPackage2GralejFormatVisitor(IDataPackage dp, PrintStream out) {
        _out = out;
        _out.append("<").append(id(dp.getTitle())).println();
        
        dp.getModel().accept(this);

        outputRelations("~", dp.getInequations());
        outputRelations("/", dp.getResidue());
        
        _out.println(">");
        _out.println();
    }

    private void outputRelations(String mark, List<IRelation> rels) {
        if (rels.isEmpty())
            return;
        _out.println();
        _out.println(mark);
        _indentLevel++;
        for (IRelation rel : rels) {
            indent();
            visit(rel);
            _out.println();
        }
        _indentLevel--;
    }

    private void indent() {
        for (int i = 0; i < _indentLevel; ++i) {
            _out.append("    ");
        }
    }

    @Override
    public void visit(IList ls) {
        _out.print('[');
        outputTfsSeq(ls.elements());
        if (ls.tail() != null) {
            _out.print(" | ");
            ls.tail().accept(this);
        }
        _out.print(']');
    }

    private void outputTfsSeq(Iterable<IEntity> tfsSeq) {
        boolean needComma = false;
        for (IEntity entity : tfsSeq) {
            if (needComma) _out.print(", "); else needComma = true;
            entity.accept(this);
        }
    }

    @Override
    public void visit(ITag tag) {
        _out.append('$').print(tag.number());
        if (_tags.add(tag.number()) && tag.target() != null) {
            _out.print(" = ");
            tag.target().accept(this);
        }
    }

    @Override
    public void visit(IAny any) {
        _out.append('@').append(id(any.value()));
    }

    @Override
    public void visit(ITypedFeatureStructure tfs) {
        _out.append(tfs.type() == null ? "*" : id(tfs.typeName()));
        if (!tfs.featureValuePairs().isEmpty()) {
            _out.println('(');
            _indentLevel++;
            boolean needComma = false;
            for (IFeatureValuePair fv : tfs.featureValuePairs()) {
                if (needComma) _out.println(","); else needComma = true;
                indent();
                _out.append(id(fv.feature())).append(": ");
                fv.value().accept(this);
            }
            _indentLevel--;
            _out.print(')');
        }
    }

    @Override
    public void visit(ITree tree) {
        _out.print('{');
        if (tree.label() != null)
            _out.append(" :").append(id(tree.label())).println();
        _indentLevel++;
        if (tree.content() != null) {
            indent();
            tree.content().accept(this);
            _out.println();
        }
        for (ITree child : tree.children()) {
            indent();
            visit(child);
        }
        _indentLevel--;
        indent();
        _out.println('}');
    }

    @Override
    public void visit(IRelation expr) {
        _out.append(id(expr.name())).append('(');
        outputTfsSeq(expr.args());
        _out.append(')');
    }

    @Override
    public void visit(ILRSExpr expr) {
        _out.append('@').append('"').append(LRS_PREFIX).append(expr.text()).append('"');
    }

    private String id(String s) {
        if (s.matches("[a-zA-Z_][a-zA-Z_0-9]*"))
            return s;
        if (s.indexOf("'") == -1)
            return "'" + s + "'";
        return "'" + s.replaceAll("'", "\\\\'") + "'";
    }

    @Override
    public void visit(IFeatureValuePair featVal) {
        throw new UnsupportedOperationException(featVal.toString());
    }

    @Override
    public void visit(IType tag) {
        throw new UnsupportedOperationException(tag.toString());
    }

    @Override
    public void visit(IVisitable visitable) {
        throw new UnsupportedOperationException(visitable.toString());
    }

    @Override
    public void visit(IEntity entity) {
        throw new UnsupportedOperationException(entity.toString());
    }

    @Override
    public void visit(ITable table) {
        //throw new UnsupportedOperationException(table.toString());
        Log.warning("Ignoring table: " + table);
    }
}
