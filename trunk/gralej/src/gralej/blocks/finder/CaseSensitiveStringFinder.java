/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.blocks.finder;

import gralej.blocks.Label;

/**
 *
 * @author Martin
 */
class CaseSensitiveStringFinder extends StringFinder {

    CaseSensitiveStringFinder(String s) { super(s); }
    
    @Override
    protected boolean matches(Label label) {
        return _s.equals(label.getText());
    }
}
