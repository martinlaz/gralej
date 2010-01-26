// $Id$
//
// Copyright (C) 2010, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//
package gralej.parsers;

import gralej.controller.StreamInfo;
import gralej.om.IEntity;
import gralej.om.IneqsAndResidue;
import gralej.parsers.TraleMsgHandlerHelper.DataPackage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import tomato.GrammarHandler;
import tomato.Lexer;
import tomato.Parser;
import tomato.SimpleLexer;

/**
 *
 * @author martin
 */
public final class SimpleFormatParser implements IGraleParser {
    
    private static Parser _parser;
    private static char[] _EMPTY_CHAR_ARRAY = new char[0];

    public SimpleFormatParser() {
        initParser();
    }

    private void initParser() {
        if (_parser != null)
            return;
        _parser = new Parser(Parsers.loadLRTable("/gralej/parsers/simple.g"));
        GrammarHandler.bind(SimpleFormatGrammarHandler.class, _parser.grammar());
    }

    private SimpleLexer getLexer(InputStream is) {
        SimpleLexer lexer = new SimpleLexer(_parser.grammar());
        lexer.addWhitespaceEater();
        lexer.addLineCommentHandler('#');
        try {
            lexer.reset(new InputStreamReader(is, "utf-8"));
        } catch (UnsupportedEncodingException ex) {

        }
        return lexer;
    }

    public List<IDataPackage> parseAll(InputStream s) throws ParseException {
        return parseAll(s, StreamInfo.GRALEJ_SIMPLE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IDataPackage> parseAll(InputStream s, StreamInfo meta) throws ParseException {
        List<IEntity> entResults;
        SimpleLexer lexer = getLexer(s);
        try {
            entResults = (List<IEntity>) _parser.parse(lexer);
        }
        catch (Exception ex) {
            String exInfo = "@row/col " + lexer.lineNumber() + "/" + lexer.charPos() + ": ";
            throw new ParseException(exInfo + ex, ex);
        }
        List<IDataPackage> results = new ArrayList<IDataPackage>(entResults.size());
        for (IEntity ent : entResults) {
            String title = ent.text();
            if (title == null)
                title = "<no title>";
            IDataPackage dp = new DataPackage(title, ent, _EMPTY_CHAR_ARRAY, meta, IneqsAndResidue.EMPTY);
            results.add(dp);
        }
        return results;
    }

    @Override
    public void parse(final InputStream s, final StreamInfo meta, final IParseResultReceiver receiver) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (IDataPackage dp : parseAll(s, meta))
                        receiver.newDataPackage(dp);
                }
                catch (ParseException ex) {
                    try { s.close(); } catch (IOException ioex) {}
                    receiver.streamClosed(s, meta, ex);
                }
            }
        }).start();
    }
}
