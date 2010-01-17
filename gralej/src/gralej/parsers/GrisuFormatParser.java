/*
 *  $Id$
 *
 *  Author:
 *     Martin Lazarov [mlazarov at sfs.uni-tuebingen.de]
 *     
 *  This file is part of the Gralej system
 *     http://code.google.com/p/gralej/
 *
 *  Gralej is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Gralej is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package gralej.parsers;

import gralej.controller.StreamInfo;

import gralej.util.Log;
import java.io.*;
import java.util.*;
import tomato.GrammarHandler;
import tomato.LRTable;
import tomato.Parser;

class GrisuFormatParser implements IGraleParser {

    private final static String GRISU_STREAM_ENCODING = "GRISU_STREAM_ENCODING";
    
    private Parser _parser;
    private TraleMsgLexer _lexer;
    private TraleMsgHandler _grammarHandler;
    private String _inputEncoding;

    public GrisuFormatParser() {
        try {
            _inputEncoding = System.getProperty(GRISU_STREAM_ENCODING);
            if (_inputEncoding == null)
                _inputEncoding = System.getenv(GRISU_STREAM_ENCODING);
        }
        catch (SecurityException ex) {
            // ignore
        }
        try {
            LRTable lr = Parsers.loadLRTable("trale-msg.g");
            _parser = new Parser(lr);
            _lexer = new TraleMsgLexer(lr.grammar());
            _grammarHandler = GrammarHandler.bind(TraleMsgHandler.class, lr.grammar());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<IDataPackage> parseAll(InputStream s, StreamInfo meta)
            throws ParseException {
        final List<IDataPackage> parses = new LinkedList<IDataPackage>();
        final Exception[] ex = new Exception[1];
        final IParseResultReceiver receiver = new IParseResultReceiver() {
            public void newDataPackage(IDataPackage data) {
                parses.add(data);
            }
            public void streamClosed(InputStream is, StreamInfo meta, Exception exception) {
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
        Reader r = null;
        if (_inputEncoding != null) {
            try {
                r = new InputStreamReader(s, _inputEncoding);
                Log.debug("using encoding:", _inputEncoding);
            }
            catch (UnsupportedEncodingException ex) {
                Log.warning(ex);
            }
        }
        if (r == null)
            r = new InputStreamReader(s);
        final Reader fr = r;

        Thread t = new Thread(new Runnable() {

            public void run() {
                Exception exception = null;
                try {
                    _lexer.reset(fr);
                    while (true) {
                        try {
                            _parser.parse(_lexer);
                            break;
                        }
                        catch (tomato.ParseException ex) {
                            if (!_lexer.skipAfterNewline()) {
                                exception = ex;
                                break;
                            }
                            Log.error(ex);
                        }
                    }
                } catch (Exception ex) {
                    exception = ex;
                }
                try {
                    s.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                receiver.streamClosed(s, meta, exception);
            }
        });
        t.start();
        return t;
    }
}
