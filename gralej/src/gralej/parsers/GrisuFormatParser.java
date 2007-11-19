package gralej.parsers;

import gralej.controller.StreamInfo;

import java.io.*;
import java.util.*;
import tomato.*;


/**
 * A dummy class representing a parser for the 
 * plain old TRALE/Grisu interchange protocol
 * @author Niels
 * @version $Id$
 */
class GrisuFormatParser implements IGraleParser {
	private Parser			_parser;
	private	TraleMsgLexer 	_lexer;
	private TraleMsgHandler	_grammarHandler;
	
	public GrisuFormatParser() {
		try {
			final String savedGrammar = "trale-msg.g.bin";
			InputStream is = getClass().getResourceAsStream(savedGrammar);
			if (is == null)
				throw new IOException("Cannot load resource: " + savedGrammar);
			LRTable lr = (LRTable) new ObjectInputStream(is).readObject();
			_parser = new Parser(lr);
			_lexer = new TraleMsgLexer(lr.grammar());
			_grammarHandler = (TraleMsgHandler) GrammarHandler.bind("gralej.parsers.TraleMsgHandler", lr.grammar()); 
		}
		catch (Exception e) {
			new RuntimeException(e);
		}
	}

	public List<IParsedAVM> getParses(InputStream s,  StreamInfo meta) {
		final List<IParsedAVM> parses = new LinkedList<IParsedAVM>();
		_grammarHandler.setResultReceiver(new IParseResultReceiver() {
			public void newParse(IParsedAVM result) {
				parses.add(result);
			}
		});
		_lexer.reset(new InputStreamReader(s));
		_parser.parse(_lexer);
		return parses;
	}

	public void parse(InputStream s,  StreamInfo meta, IParseResultReceiver receiver) {
		_grammarHandler.setResultReceiver(receiver);
		_lexer.reset(new InputStreamReader(s));
		new Thread(new Runnable() {
				public void run() {
					_parser.parse(_lexer);
				}
			}
		).start();
	}
}
