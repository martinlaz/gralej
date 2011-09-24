// $Id$
//
// Copyright (C) 2011, Martin Lazarov (martinlaz at gmail).
// All rights reserved.
//
package gralej.gui.syntax;

import gralej.Config;
import gralej.util.Log;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Martin Lazarov
 */
public class GralejExprStyledDocumentHandler extends StyledDocumentHandler {
    private static final String REGEX_ID_1 = "[\\p{L}_][\\p{L}_\\d]*";
    private static final String REGEX_ID_2 = "\"[^\"]*\"";
    private static final String REGEX_ID_3 = "'[^']*'";
    private static final String REGEX_ID = "(?:" + REGEX_ID_1 + "|" + REGEX_ID_2 + "|" + REGEX_ID_3 + ")";

    private static enum TokenType {
        ATOM("@\\s*" + REGEX_ID),
        NODE("(?<=\\{\\s{0,5}:\\s{0,5})" + REGEX_ID),
        WINDOW("(?<=<\\s{0,5})" + REGEX_ID),
        FEATURE(REGEX_ID + "(?=\\s{0,5}:)"),
        TYPE(REGEX_ID + "(?=\\s*\\()"),
        ID(REGEX_ID),
        COMMENT("#.*"),
        TAG("\\$\\d+"),
        LIST("\\[|\\]"),
                ;

        final String regex;

        TokenType(String regex) { this.regex = regex; }
    }

    private static final Lexer LEXER = new Lexer() {
            Pattern p;
            {
                StringBuilder regex = new StringBuilder().append("(").append(TokenType.values()[0].regex);
                TokenType[] tts = TokenType.values();
                for (int i = 1; i < tts.length; ++i) {
                    regex.append(")|(").append(tts[i].regex);
                }
                regex.append(")");
                //Log.debug("gralej-expr-token-regex", regex);
                p = Pattern.compile(regex.toString());
            }
            @Override public Collection<Token> getTokens(String text) {
                List<Token> tokens = new LinkedList<Token>();
                Matcher matcher = p.matcher(text);
                while (matcher.find()) {
                    int group = firstMatchingGroup(matcher);
                    TokenType token = TokenType.values()[group - 1];
                    //Log.debug(token, matcher.group());
                    tokens.add(new Token(token.name(), matcher.group(), matcher.start(), matcher.end() - matcher.start()));
                }
                return tokens;
            }

            private int firstMatchingGroup(Matcher matcher) {
                for (int i = 1; i <= matcher.groupCount(); ++i) {
                    if (matcher.group(i) != null)
                        return i;
                }
                return -1;
            }
        };

    @Override
    protected void addStyles(StyledDocument doc) {
        for (TokenType tokenType : TokenType.values()) {
            Style style = doc.addStyle(tokenType.name(), null);
            StyleConstants.setForeground(style, Config.color("gui.syntax.gralej-expr." + tokenType.name().toLowerCase()));
        }
    }

    @Override
    protected Lexer getLexer() {
        return LEXER;
    }
}
