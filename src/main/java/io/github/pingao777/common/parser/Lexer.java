package io.github.pingao777.common.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import io.github.pingao777.common.model.Token;


public abstract class Lexer {
    public static final char EOF = (char) -1;
    private String line;
    private Scanner scanner;
    protected int col = 0;
    protected int row = 0;
    protected char c;

    public Lexer(String path) {
        try {
            scanner = new Scanner(new File(path));
            if (scanner.hasNextLine()) {
                line = scanner.nextLine();
                c = line.charAt(col);
            } else {
                c = EOF;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getLine() {
        return line;
    }

    protected abstract void consume();

    public void advance() {
        col++;
        if (col >= line.length()) {
            if (scanner.hasNextLine()) {
                c = '\n';
                col = -1;
                line = scanner.nextLine();
                row++;
            } else {
                c = EOF;
            }
        } else {
            c = line.charAt(col);
        }
    }

    public void match(char x) {
        if (c == x) {
            consume();
        } else {
            throw new Error("expecting " + x + "; found " + c + " at [" + (row + 1) + ", " + (col + 1) + "]");
        }
    }

    public abstract Token getNextToken();

    protected abstract void WS();
}