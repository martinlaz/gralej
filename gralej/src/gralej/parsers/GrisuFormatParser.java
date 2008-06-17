package gralej.parsers;

import gralej.controller.StreamInfo;

import gralej.util.Log;
import java.io.*;
import java.util.*;
import tomato.GrammarHandler;
import tomato.LRTable;
import tomato.Parser;

/**
 * A dummy class representing a parser for the plain old TRALE/Grisu interchange
 * protocol
 * 
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
            _grammarHandler = (TraleMsgHandler) GrammarHandler.bind(
                    "gralej.parsers.TraleMsgHandler", lr.grammar());
        } catch (Exception e) {
            new RuntimeException(e);
        }
    }

    private LRTable loadLRTable() throws Exception {
        final String grammarResourceName = "trale-msg.g";
        // first try the compiled grammar
        // it must load ok most of the time
        InputStream is = getClass().getResourceAsStream(grammarResourceName + ".bin");
        if (is != null) {
            try {
                ObjectInputStream ois = new ObjectInputStream(is);
                return (LRTable) ois.readObject();
            }
            catch (Exception e) {
                Log.warning("Exception while loading bin grammar:", e);
            }
            finally {
                is.close();
            }
        }
        
        // fail-safe
        // no cached grammar? recompile it
        is = getClass().getResourceAsStream(grammarResourceName);
        if (is == null) {
            throw new IOException("Failed to load resource: "
                    + grammarResourceName);
        }
        return LRTable.newInstance(new InputStreamReader(is));
    }

    public List<IDataPackage> parseAll(InputStream s, StreamInfo meta)
            throws ParseException {
        final List<IDataPackage> parses = new LinkedList<IDataPackage>();
        final Exception[] ex = new Exception[1];
        final IParseResultReceiver receiver = new IParseResultReceiver() {
            public void newDataPackage(IDataPackage data) {
                parses.add(data);
            }
            public void streamClosed(StreamInfo meta, Exception exception) {
                ex[0] = exception;
            }
        };
        
        Thread parseThread = parseImpl(s, meta, receiver);
        
        try {
            parseThread.join();
        }
        catch (InterruptedException e) {
        }
        if (ex[0] != null)
            throw new ParseException(ex[0]);
        return parses;
    }

    public void parse(final InputStream s, final StreamInfo meta,
            final IParseResultReceiver receiver) {
        parseImpl(s, meta, receiver);
    }
    
    private Thread parseImpl(final InputStream s, final StreamInfo meta,
            final IParseResultReceiver receiver) {
        _grammarHandler._helper.setResultReceiver(receiver);
        _grammarHandler._helper.setCharBuffer(_lexer.getCharBuffer());
        _grammarHandler._helper.setStreamInfo(meta);
        _lexer.reset(new InputStreamReader(s));
        Thread t = new Thread(new Runnable() {

            public void run() {
                Exception exception = null;
                try {
                    _parser.parse(_lexer);
                } catch (Exception ex) {
                    exception = ex;
                }
                try {
                    s.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                receiver.streamClosed(meta, exception);
            }
        });
        t.start();
        return t;
    }
}
