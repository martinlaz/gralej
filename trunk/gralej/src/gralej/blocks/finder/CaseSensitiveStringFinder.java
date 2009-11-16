package gralej.blocks.finder;

import gralej.blocks.Label;

/**
 *
 * @author Martin
 */
class CaseSensitiveStringFinder extends StringFinder {

    CaseSensitiveStringFinder(FinderOptions opts) { super(opts); }
    
    @Override
    protected boolean matches(Label label) {
        String s = label.getText();
        if (_opts.isCompleteMatch)
            return _opts.text.equals(s);
        return s.indexOf(_opts.text) != -1;
    }
}
