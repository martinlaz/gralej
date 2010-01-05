// $Id$
//
// Copyright (C) 2009, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import tomato.LRTable;

/**
 *
 * @author Martin
 */
final class Parsers {
    private static Class _class = new Parsers().getClass();

    static LRTable loadLRTable(String grammarResourceName) {
        // first try the pre-compiled grammar
        // it must load ok most of the time
        InputStream is = _class.getResourceAsStream(grammarResourceName + ".bin");
        if (is != null) {
            try {
                ObjectInputStream ois = new ObjectInputStream(is);
                return (LRTable) ois.readObject();
            }
            catch (Exception e) {
                //Log.warning("Exception while loading bin grammar:", e);
            }
            finally {
                try { is.close(); } catch (Exception ex) {}
            }
        }

        // fail-safe
        // no cached grammar? recompile it
        try {
            is = _class.getResourceAsStream(grammarResourceName);
            if (is == null) {
                throw new IOException("Failed to load resource: "
                        + grammarResourceName);
            }
            return LRTable.newInstance(new InputStreamReader(is));
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
