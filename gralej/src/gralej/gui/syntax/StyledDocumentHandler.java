// $Id$
//
// Copyright (C) 2011, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//

package gralej.gui.syntax;

import gralej.util.Log;
import java.util.Collection;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Martin Lazarov
 */
public abstract class StyledDocumentHandler {

    public StyledDocumentHandler install(final StyledDocument doc) {
        doc.addDocumentListener(new DocumentListener() {

            @Override public void insertUpdate(DocumentEvent e) {
                recolorRows(doc, e.getOffset(), e.getLength());
            }

            @Override public void removeUpdate(DocumentEvent e) {
                recolorRows(doc, e.getOffset(), e.getLength());
            }

            @Override public void changedUpdate(DocumentEvent e) { }

        });
        addStyles(doc);
        return this;
    }

    public void recolor(StyledDocument doc) {
        recolorRows(doc, 0, doc.getLength());
    }

    protected void recolorRows(final StyledDocument doc, int offset, int length) {
        String text = "";
        int begin = offset, end = offset + length;
        try {
            for ( ;begin > 0; begin--) {
                String s = doc.getText(begin - 1, 1);
                if (s.equals("\n"))
                    break;
            }
            int docLength = doc.getLength();
            for (; end < docLength; end++) {
                String s = doc.getText(end, 1);
                if (s.equals("\n"))
                    break;
            }
            text = doc.getText(begin, Math.min(end - begin, docLength - begin));
            //Log.debug(">", text, "<");
        }
        catch (BadLocationException ex) {
            Log.debug(ex, "requested:", ex.offsetRequested(), "offset:", offset, "length:", length, "begin:", begin, "end:", end);
        }

        final Collection<Token> tokens = getLexer().getTokens(text);
        final int beginFinal = begin;

        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                for (Token token : tokens) {
                    doc.setCharacterAttributes(beginFinal + token.getOffset(), token.getLength(), doc.getStyle(token.getType()), true);
                }
            }
        });
    }

    abstract protected void addStyles(StyledDocument doc);

    abstract protected Lexer getLexer();
    
}
