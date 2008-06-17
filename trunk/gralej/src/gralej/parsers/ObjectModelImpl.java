package gralej.parsers;

import gralej.om.*;

class OM {
    static class Flags {
        final static int HIDDEN = 1;
        final static int DIFFERENT = 2;
        final static int STRUCKOUT = 4;
        final static int MULTILINE = 8;
        final static int EXPANDED = 16;

        int flags;

        boolean isHidden() {
            return (flags & HIDDEN) != 0;
        }

        boolean isDifferent() {
            return (flags & DIFFERENT) != 0;
        }

        boolean isStruckout() {
            return (flags & STRUCKOUT) != 0;
        }

        boolean isMultiline() {
            return (flags & MULTILINE) != 0;
        }

        boolean isExpanded() {
            return (flags & EXPANDED) != 0;
        }

        void setHidden() {
            flags |= HIDDEN;
        }

        void setDifferent() {
            flags |= DIFFERENT;
        }

        void setStruckout() {
            flags |= STRUCKOUT;
        }

        void setMultiline() {
            flags |= MULTILINE;
        }

        void setExpanded() {
            flags |= EXPANDED;
        }
    }

    static class Entity implements IEntity {
        Flags _flags;

        Entity(Flags flags) {
            _flags = flags;
        }

        public boolean isHidden() {
            return _flags.isHidden();
        }

        public boolean isDifferent() {
            return _flags.isDifferent();
        }

        public boolean isStruckout() {
            return _flags.isStruckout();
        }

        public boolean isMultiline() {
            return _flags.isMultiline();
        }

        public boolean isExpanded() {
            return _flags.isExpanded();
        }

        public void accept(IVisitor v) {
            v.visit(this);
        }
    }

    static class FeatVal extends Entity implements IFeatureValuePair {
        String _f;
        IEntity _v;

        FeatVal(Flags flags, String f, IEntity v) {
            super(flags);
            _f = f;
            _v = v;
        }

        public String feature() {
            return _f;
        }

        public IEntity value() {
            return _v;
        }

        public void accept(IVisitor v) {
            v.visit(this);
        }
    }

    static class List extends Entity implements IList {
        java.util.List<IEntity> _ls;
        IEntity _tail;

        List(Flags flags, java.util.List<IEntity> ls, IEntity tail) {
            super(flags);
            _ls = ls;
            _tail = tail;
        }

        public Iterable<IEntity> elements() {
            return _ls;
        }
        
        public IEntity tail() {
            return _tail;
        }

        public void accept(IVisitor v) {
            v.visit(this);
        }
    }

    static class Tag extends Entity implements ITag {
        int _number;
        IEntity _target;

        Tag(Flags flags, int number) {
            super(flags);
            _number = number;
        }

        public int number() {
            return _number;
        }

        public IEntity target() {
            return _target;
        }

        void setTarget(IEntity target) {
            _target = target;
        }

        public void accept(IVisitor v) {
            v.visit(this);
        }
    }

    static class Any extends Entity implements IAny {
        String _value;

        Any(Flags flags, String value) {
            super(flags);
            _value = value;
        }

        public String value() {
            return _value;
        }

        public void accept(IVisitor v) {
            v.visit(this);
        }
    }

    static class TFS extends Entity implements ITypedFeatureStructure {
        String _typeName;
        java.util.List<IFeatureValuePair> _featVals;

        TFS(Flags flags, String typeName,
                java.util.List<IFeatureValuePair> featVals) {
            super(flags);
            _typeName = typeName;
            _featVals = featVals;
        }

        public String typeName() {
            return _typeName;
        }

        public Iterable<IFeatureValuePair> featureValuePairs() {
            return _featVals;
        }

        public boolean isSpecies() {
            return _featVals.isEmpty();
        }

        public void accept(IVisitor v) {
            v.visit(this);
        }
    }

    static class Tree implements ITree {
        String _label;
        IEntity _content;
        java.util.List<ITree> _children;

        Tree(String label, java.util.List<ITree> children) {
            _label = label;
            _children = children;
        }

        void setContent(IEntity content) {
            _content = content;
        }

        public String label() {
            return _label;
        }

        public IEntity content() {
            return _content;
        }

        public Iterable<ITree> children() {
            return _children;
        }

        public boolean isLeaf() {
            return _children.isEmpty();
        }

        public void accept(IVisitor v) {
            v.visit(this);
        }
    }
}
