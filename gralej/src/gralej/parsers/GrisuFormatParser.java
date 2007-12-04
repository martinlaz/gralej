package gralej.parsers;

import gralej.controller.StreamInfo;

import java.io.*;
import java.util.*;
import tomato.GrammarHandler;
import tomato.LRTable;
import tomato.Parser;


/**
 * A dummy class representing a parser for the 
 * plain old TRALE/Grisu interchange protocol
 * @author Niels
 * @version $Id$
 */
class GrisuFormatParser implements IGraleParser {

    private Parser _parser;
    private TraleMsgLexer _lexer;
    private TraleMsgHandler _grammarHandler;

    public GrisuFormatParser() {
        try {
            LRTable lr = loadLRTable();
            _parser = new Parser(lr);
            _lexer = new TraleMsgLexer(lr.grammar());
            _grammarHandler = (TraleMsgHandler) GrammarHandler.bind("gralej.parsers.TraleMsgHandler", lr.grammar());
        } catch (Exception e) {
            new RuntimeException(e);
        }
    }

    private LRTable loadLRTable() throws Exception {
        final String grammarResourceName = "trale-msg.g";
        InputStream is = getClass().getResourceAsStream(grammarResourceName);
        if (is == null) {
            throw new IOException("Failed to load resource: " + grammarResourceName);
        }
        return LRTable.newInstance(new InputStreamReader(is));
    }

    public List<IDataPackage> getParses(InputStream s, StreamInfo meta) throws ParseException {
        final List<IDataPackage> parses = new LinkedList<IDataPackage>();
        _grammarHandler.setResultReceiver(new IParseResultReceiver() {

                    public void newDataPackage(IDataPackage data) {
                        parses.add(data);
                    }

                    public void streamClosed(StreamInfo meta, Exception exception) {
                    }
                });
        _lexer.reset(new InputStreamReader(s));
        try {
            _parser.parse(_lexer);
        } catch (Exception e) {
            throw new ParseException(e);
        } finally {
            try {
                s.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return parses;
    }

    public void parse(final InputStream s, final StreamInfo meta, final IParseResultReceiver receiver) {
        _grammarHandler.setResultReceiver(receiver);
        _lexer.reset(new InputStreamReader(s));
        new Thread(new Runnable() {

                    public void run() {
                        Exception exception = null;
                        try {
                            _parser.parse(_lexer);
                        }
                        catch (Exception ex) {
                            exception = ex;
                        }
                        try {
                            s.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        receiver.streamClosed(meta, exception);
                    }
                }).start();
    }
}
