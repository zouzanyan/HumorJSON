package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokenList {

    private List<Token> tokens = new ArrayList<>();
    private CharScanner charScanner;

    // 记录当前指针位置
    private int pos = 0;

    public void add(Token token) {
        tokens.add(token);
    }

    public Token next() {
        return tokens.get(pos++);
    }

    public boolean hasMore() {
        return pos < tokens.size();
    }

    @Override
    public String toString() {
        return "TokenList{" +
                "tokens=" + tokens +
                '}';
    }

    private List<Token> tokenizer(String jsonStr) {
        charScanner = new CharScanner(jsonStr);
        while (charScanner.hasMore()) {

            // 逐个字符扫描
            char nextChar = charScanner.nextChar();

            parseToken(nextChar);


        }
        return null;


    }

    private Token parseToken(char nextChar) {

        switch (nextChar) {
            case '{':
                return new Token(TokenType.BEGIN_OBJECT, "{");
            case '}':
                return new Token(TokenType.END_OBJECT, "}");
            case '[':
                return new Token(TokenType.BEGIN_ARRAY, "[");
            case ']':
                return new Token(TokenType.END_ARRAY, "]");
            case ',':
                return new Token(TokenType.SEP_COMMA, ",");
            case ':':
                return new Token(TokenType.SEP_COLON, ":");
            case 'n':
                return readNull();
            case 't':
            case 'f':
                return readBoolean();
            case '"':
                return readString();
            case '-':
                return readNumber();
            default:
                return null;
        }
    }

    private Token readNumber() {
        return null;
    }

    private Token readString() {
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            // 判断转义符号
            if (charScanner.nextChar() == '\\') {
                char nextChar = charScanner.nextChar();
                if (nextChar == '"' || nextChar == '\\' || nextChar == 'r' || nextChar == 'n' || nextChar == 'b' || nextChar == 't' || nextChar == 'f') {
                    stringBuilder.append(nextChar);
                    stringBuilder.insert(stringBuilder.length() - 1, '\\');
                } else if (nextChar == 'u') {
                    for (int i = 0; i < 4; i++) {
                        char ch = charScanner.nextChar();
                        if (isHex(ch)) {
                            stringBuilder.append(ch);
                        } else {
                            throw new RuntimeException("Invalid character [\\u]");
                        }
                    }
                    stringBuilder.insert(stringBuilder.length() - 4, "\\u");
                } else {
                    throw new RuntimeException("Invalid escape character");
                }
            }else if (charScanner.nextChar() == '\"'){
                return new Token(TokenType.STRING, stringBuilder.toString());
            }else if(charScanner.nextChar() == '\r' | charScanner.nextChar() == '\n'){
                throw new RuntimeException("Invalid escape character [\\r or \\n]");
            }
            // TODO json不支持直接解析\r \n \t之类的



        }

    }

    private boolean isHex(char ch) {
        return ((ch >= '0' && ch <= '9') || ('a' <= ch && ch <= 'f')
                || ('A' <= ch && ch <= 'F'));
    }

    private Token readBoolean() {
        if (charScanner.nextChar() == 'r' & charScanner.nextChar() == 'u' & charScanner.nextChar() == 'e') {
            return new Token(TokenType.BOOLEAN, "true");
        }
        if (charScanner.nextChar() == 'a' & charScanner.nextChar() == 'l' & charScanner.nextChar() == 's' & charScanner.nextChar() == 'e') {
            return new Token(TokenType.BOOLEAN, "false");
        }
        throw new RuntimeException("Invalid json string [boolean]");
    }

    private Token readNull() {

        if (charScanner.nextChar() == 'u' & charScanner.nextChar() == 'l' & charScanner.nextChar() == 'l') {
            return new Token(TokenType.NULL, "null");
        } else {
            throw new RuntimeException("Invalid json string [null]");
        }

    }

}


class awdP {
    public static void main(String[] args) {
        char[] chars = "\\n".toCharArray();
        System.out.println(chars.length);
        for (char i: chars
             ) {
            System.out.println(i);
        }

    }
}