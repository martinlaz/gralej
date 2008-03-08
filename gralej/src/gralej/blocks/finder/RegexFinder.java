/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.blocks.finder;

import gralej.blocks.Label;
import java.util.regex.Pattern;

/**
 *
 * @author Martin
 */
class RegexFinder extends Finder {
    
    private Pattern _pat;
    
    RegexFinder(String regex) {
        _pat = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected boolean matches(Label label) {
        return _pat.matcher(label.getText()).matches();
    }

}
