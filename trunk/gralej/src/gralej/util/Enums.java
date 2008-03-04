/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gralej.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Martin
 */
public class Enums {
    public Enums(String... constants) {
        _enum = new ArrayList<String>(constants.length);
        for (String s : constants)
            _enum.add(s);
    }
    
    public int decode(String s) {
        int i = _enum.indexOf(s);
        if (i == -1)
            throw new Exception(s);
        return i;
    }
    
    public String toString(int code) {
        return _enum.get(code);
    }
    
    public boolean contains(String s) {
        return _enum.indexOf(s) != -1;
    }
    
    static public class Exception extends RuntimeException {
        Exception(String s) { super(s); }
    }
    
    List<String> _enum;
}
