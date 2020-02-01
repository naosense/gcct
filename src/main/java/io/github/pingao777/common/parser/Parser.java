package io.github.pingao777.common.parser;

import java.util.List;
import io.github.pingao777.common.enums.TokenType;
import io.github.pingao777.common.model.ResultData;
import io.github.pingao777.common.model.Token;


/**
 * Created by pingao on 2020/1/6.
 */
public abstract class Parser {
    Lexer input;
    Token[] lookahead;
    int k;
    int p = 0;

    public Parser(Lexer input, int k) {
        this.input = input;
        this.k = k;
        lookahead = new Token[k];
        for (int i = 1; i <= k; i++) consume();
    }

    public void consume() {
        lookahead[p] = input.getNextToken();
        p = (p + 1) % k;
    }

    public Token LT(int i) {return lookahead[(p + i - 1) % k];}

    public TokenType LA(int i) { return LT(i).getType(); }

    public String LV(int i) {
        return LT(i).getText();
    }

    protected String getLine() {
        return input.getLine();
    }

    protected int getRow() {
        return LT(1).getRow();
    }

    protected int getCol() {
        return LT(1).getCol();
    }

    public void match(TokenType x) {
        if (LA(1) == x) {
            consume();
        } else {
            throw new Error("expecting " + x +
                "; found " + LT(1));
        }
    }

    public void skip(int n) {
        for (int i = 0; i < n; i++) {
            match(LA(1));
        }
    }

    public String between(TokenType begin, TokenType end) {
        StringBuilder sb = new StringBuilder();
        after(begin);
        while (LA(1) != end) {
            sb.append(LV(1));
            match(LA(1));
        }
        return sb.toString();
    }

    public void before(TokenType type) {
        while (true) {
            TokenType a1 = LA(1);
            if (a1 == type || a1 == TokenType.EOF) {
                break;
            }
            match(a1);
        }
    }

    public void before(Token token) {
        while (true) {
            Token a1 = LT(1);
            if (a1.equals(token) || a1.getType() == TokenType.EOF) {
                break;
            }
            match(a1.getType());
        }
    }

    public void after(Token token) {
        before(token);
        skip(1);
    }

    public void after(TokenType type) {
        before(type);
        skip(1);
    }

    public abstract List<ResultData> parse(ParseContext context);
}
