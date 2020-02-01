package io.github.pingao777.jdk8.cms;

import io.github.pingao777.common.parser.Lexer;
import io.github.pingao777.common.model.Token;
import io.github.pingao777.common.enums.TokenType;


/**
 * Created by pingao on 2020/1/6.
 */
public class Jdk8CMCLexer extends Lexer {

    public Jdk8CMCLexer(String input) { super(input); }

    @Override
    public void consume() {
        advance();
    }

    @Override
    public Token getNextToken() {
        while (c != EOF) {
            switch (c) {
                case ' ':
                case '\t':
                    // case '\r':
                    WS();
                    continue;
                case '\r':
                case '\n':
                    consume();
                    return new Token(String.valueOf(c), TokenType.CR, row, col);
                case ',':
                    consume();
                    return new Token(",", TokenType.COMMA, row, col);
                case '[':
                    consume();
                    return new Token("[", TokenType.LBRACK, row, col);
                case ']':
                    consume();
                    return new Token("]", TokenType.RBRACK, row, col);
                case '(':
                    consume();
                    return new Token("(", TokenType.LPAREN, row, col);
                case ')':
                    consume();
                    return new Token(")", TokenType.RPAREN, row, col);
                case '=':
                    consume();
                    return new Token("=", TokenType.EQUALS, row, col);
                case ':':
                    consume();
                    return new Token(":", TokenType.COLON, row, col);
                case '/':
                    consume();
                    return new Token("/", TokenType.SLASH, row, col);
                case '\\':
                    consume();
                    return new Token("\\", TokenType.BSLASH, row, col);
                case '-':
                    consume();
                    return new Token("-", TokenType.MINUS, row, col);
                case '+':
                    consume();
                    return new Token("+", TokenType.PLUS, row, col);
                case '%':
                    consume();
                    return new Token("%", TokenType.PERCENT, row, col);
                case '_':
                    consume();
                    return new Token("_", TokenType.UNDER, row, col);
                case '>':
                    consume();
                    return new Token(">", TokenType.GT, row, col);
                case '"':
                    return str();
                default:
                    if (isName()) {
                        return name();
                    } else if (isNum()) {
                        return num();
                    }
                    throw new Error("invalid character: " + c);
            }
        }
        return new Token("<EOF>", TokenType.EOF);
    }

    @Override
    protected void WS() {
        while (c == ' ' || c == '\t' || c == '\r') advance();
    }

    Token str() {
        StringBuilder buf = new StringBuilder();
        while (true) {
            buf.append(c);
            consume();
            if (c == '"') {
                consume();
                break;
            }
        }
        return new Token(buf.toString(), TokenType.STR, row, col);
    }

    boolean isName() { return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z'; }

    Token name() {
        StringBuilder buf = new StringBuilder();
        do {
            buf.append(c);
            consume();
        } while (isName());
        return new Token(buf.toString(), TokenType.NAME, row, col);
    }

    boolean isNum() {
        return c >= '0' && c <= '9' || c == '.' || c == 'x' || c >= 'a' && c <= 'f';
    }

    Token num() {
        StringBuilder buf = new StringBuilder();
        do {
            buf.append(c);
            consume();
        } while (isNum());
        return new Token(buf.toString(), TokenType.NUM, row, col);
    }
}
