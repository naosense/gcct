package io.github.pingao777.common.model;

import java.util.Objects;
import io.github.pingao777.common.enums.TokenType;


/**
 * Created by pingao on 2020/1/6.
 */
public class Token {
    private int row;
    private int col;
    private String text;
    private TokenType type;

    public Token(String text, TokenType type) {
        this.text = text;
        this.type = type;
    }

    public Token(String text, TokenType type, int row, int col) {
        this.text = text;
        this.type = type;
        this.row = row;
        this.col = col - text.length();
    }

    public int getRow() {
        return row + 1;
    }

    public int getCol() {
        return col + 1;
    }

    public String getText() {
        return text;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Token token = (Token) o;
        return text.equals(token.text) &&
            type == token.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, type);
    }

    @Override
    public String toString() {
        return "Token{" +
            "row=" + row +
            ", col=" + col +
            ", text='" + text + '\'' +
            ", type=" + type +
            '}';
    }
}
