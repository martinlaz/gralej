// $Id$
//
// Copyright (C) 2010, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//
package gralej.parsers;

import gralej.controller.StreamInfo;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import tomato.GrammarHandler;
import tomato.ReduceResultHandler;
import tomato.Parser;
import tomato.Production;
import tomato.SimpleLexer;

/**
 *
 * @author martin
 */
public final class SimpleFormatParser implements IGraleParser {

    private final static Parser _parser;

    static {
        _parser = new Parser(Parsers.loadLRTable("/gralej/parsers/simple.g"));
        SimpleFormatGrammarHandler gh = GrammarHandler.bind(
                SimpleFormatGrammarHandler.class, _parser.grammar());
    }

    public List<IDataPackage> parseAll(InputStream s) throws ParseException {
        return parseAll(s, StreamInfo.GRALEJ_SIMPLE);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IDataPackage> parseAll(InputStream s, StreamInfo meta) throws ParseException {
        final List<IDataPackage> all = new LinkedList<IDataPackage>();
        final Exception[] ex = new Exception[1];

        Thread t = parseThread(s, meta, new IParseResultReceiver() {

            @Override
            public void newDataPackage(IDataPackage data) {
                all.add(data);
            }

            @Override
            public void streamClosed(InputStream is, StreamInfo meta, Exception exception) {
                ex[0] = exception;
            }
        });

        t.start();
        try {
            t.join();
        } catch (InterruptedException ex1) {
            Thread.currentThread().interrupt();
        }

        if (ex[0] != null) {
            throw new ParseException(ex[0]);
        }

        return all;
    }

    @Override
    public void parse(final InputStream s, final StreamInfo meta, final IParseResultReceiver receiver) {
        parseThread(s, meta, receiver).start();
    }

    private Thread parseThread(final InputStream s, final StreamInfo meta, final IParseResultReceiver receiver) {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                Exception ex = null;
                try {
                    _parser.parse(getLexer(s), new ReduceResultHandler() {
                        @Override public Object handle(Object reduceResult, Production production) {
                            if (reduceResult instanceof DataPackage) {
                                receiver.newDataPackage(((DataPackage) reduceResult).setStreamInfo(meta));
                            }
                            return reduceResult;
                        }
                    });
                } catch (Exception ex2) {
                    ex = ex2;
                }
                try {
                    s.close();
                } catch (IOException ioex) {
                }
                receiver.streamClosed(s, meta, ex);
            }
        });
    }

    private SimpleLexer getLexer(InputStream is) {
        SimpleLexer lexer = new SimpleLexer(_parser.grammar());
        lexer.addWhitespaceEater();
        lexer.addLineCommentHandler('#');
        try {
            lexer.reset(new InputStreamReader(is, "utf-8"));
        } catch (UnsupportedEncodingException ex) {
            // something must be really broken
        }
        return lexer;
    }
}
