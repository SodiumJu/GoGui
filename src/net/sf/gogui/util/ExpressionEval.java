package net.sf.gogui.util;

import java.util.HashMap;

public class ExpressionEval {

    public interface Node {

        boolean eval();

    }

    private String str;

    private HashMap<String, Boolean> map;

    private int pos = -1, ch;

    public ExpressionEval(String str) {
        this.str = str;
        this.map = new HashMap<String, Boolean>();
    }

    private void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ')
            nextChar();
        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    public Node parse() {
        nextChar();
        Node x = parseExpression();
        if (pos < str.length())
            throw new RuntimeException("Unexpected: " + (char) ch);
        return x;
    }

    public void set(String name, boolean value) {
        map.put(name, value);
    }

    // Grammar:
    // expression = term | expression `|` term
    // term = factor | term `&` factor
    // factor = `!` factor | `(` expression `)` | variableName

    private Node parseExpression() {
        Node x = parseTerm();
        for (;;) {
            if (eat('|')) {
                Node left = x;
                Node right = parseTerm();
                x = new Node() { // or
                    public boolean eval() {
                        return left.eval() | right.eval();
                    }
                };
            } else
                return x;
        }
    }

    private Node parseTerm() {
        Node x = parseFactor();
        for (;;) {
            if (eat('&')) {
                Node left = x;
                Node right = parseTerm();
                x = new Node() { // and
                    public boolean eval() {
                        return left.eval() & right.eval();
                    }
                };
            } else
                return x;
        }
    }

    private Node parseFactor() {
        if (eat('!')) {
            Node x = parseFactor();
            return new Node() { // unary not
                public boolean eval() {
                    return !x.eval();
                }
            };
        }

        Node x;
        int startPos = this.pos;
        if (eat('(')) { // parentheses
            x = parseExpression();
            if (!eat(')'))
                throw new RuntimeException("Missing ')'");
        } else if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) { // variableNames
            while ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9'))
                nextChar();
            String varName = str.substring(startPos, this.pos);
            /* if (varName.equals("true")) {
                x = new Node() { // true constant
                    public boolean eval() {
                        return true;
                    }
                };
            } else if (varName.equals("false")) {
                x = new Node() { // false constant
                    public boolean eval() {
                        return false;
                    }
                };
            } else {
                x = new Node() { // variable
                    public boolean eval() {
                        return map.getOrDefault(varName, false);
                    }
                };
            } */
            x = new Node() { // variable
                public boolean eval() {
                    return map.getOrDefault(varName, false);
                }
            };
        } else {
            throw new RuntimeException("Unexpected: " + (char) ch);
        }

        return x;
    }

}
