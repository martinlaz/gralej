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
class StringFinder extends Finder {
    
    private String _s;
    
    StringFinder(String s) { _s = s; }

    @Override
    protected boolean matches(Label label) {
        return _s.equalsIgnoreCase(label.getText());
    }

}
