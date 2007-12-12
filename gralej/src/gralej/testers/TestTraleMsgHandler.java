package gralej.testers;

import gralej.parsers.TraleMsgLexer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import tomato.LRTable;
import tomato.Grammar;
import tomato.Parser;
import tomato.Lexer;
import tomato.GrammarHandler;

public class TestTraleMsgHandler {
    public static void main(String[] args) throws Exception {
        LRTable lr = LRTable.newInstance(new InputStreamReader(
                TestTraleMsgHandler.class
                        .getResourceAsStream("../parsers/trale-msg.g")));
        Parser parser = new Parser(lr);

        InputStream is = System.in;
        if (args.length > 0)
            is = new FileInputStream(args[0]);
        Grammar g = lr.grammar();
        Lexer lex = new TraleMsgLexer(g, new InputStreamReader(is));

        GrammarHandler.bind("gralej.parsers.TraleMsgHandler", g);

        parser.parse(lex);
    }
}
