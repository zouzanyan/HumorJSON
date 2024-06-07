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

    public List<Token> tokenizer(String jsonStr) {
        charScanner = new CharScanner(jsonStr);
        while (charScanner.hasMore()) {

            // 逐个字符扫描
            char nextChar = charScanner.nextChar();

            Token token = parseToken(nextChar);
            tokens.add(token);


        }
        return tokens;


    }

    private Token parseToken(char nextChar) {

        //
        if (nextChar == '{') {
            return new Token(TokenType.BEGIN_OBJECT, "{");
        } else if (nextChar == '}') {
            return new Token(TokenType.END_OBJECT, "}");
        } else if (nextChar == '[') {
            return new Token(TokenType.BEGIN_ARRAY, "[");
        } else if (nextChar == ']') {
            return new Token(TokenType.END_ARRAY, "]");
        } else if (nextChar == ',') {
            return new Token(TokenType.SEP_COMMA, ",");
        } else if (nextChar == ':') {
            return new Token(TokenType.SEP_COLON, ":");
        } else if (nextChar == 'n') {
            return readNull();
        } else if (nextChar == 't' || nextChar == 'f') {
            return readBoolean();
        } else if (nextChar == '"') {
            return readString();
        } else if (nextChar == '-' || isDigit(nextChar)) {
            return readNumber();
        } else {
            throw new RuntimeException("Illegal character");
        }
    }

    private Token readNumber() {
        StringBuilder number = new StringBuilder();
        // 回退判断数字的首类型
        char c = charScanner.peekChar();;
        if (c == '-'){
            number.append(c);
            charScanner.nextChar();
        }

        // 读取第一个数字字符
        c = charScanner.nextChar();
        if (!isDigit(c)) {
            throw new RuntimeException("Invalid number format");
        }
        number.append(c);


        // 读取整数部分的剩余数字
        while (isDigit((c = charScanner.nextChar()))) {
            number.append(c);
        }

        // 读取小数
        if (c == '.'){
            number.append(c);
            // 小数点后第一位不是数字异常
            c = charScanner.nextChar();
            if (!isDigit(c)) {
                throw new RuntimeException("Invalid fraction");
            }
            number.append(c);

            while (isDigit(c = charScanner.nextChar())){
                number.append(c);
            }
        }

        // 读取科学计数法

        if (c == 'e' || c == 'E') {
            number.append(c);

            c = charScanner.nextChar();
            if (c == '-' || c == '+') {
                number.append(c);
                c = charScanner.nextChar();
            }
            if (!isDigit(c)) {
                throw new RuntimeException("Invalid exponent");
            }
            number.append(c);
            while (isDigit((c = charScanner.nextChar()))) {
                number.append(c);
            }
        }
        return new Token(TokenType.NUMBER, number.toString());
    }

    private boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private Token readString() {
        StringBuilder stringBuilder = new StringBuilder();

        while (true) {
            char c = charScanner.nextChar();

            /*  判断转义符号
             *  TODO json标准对于字符串不支持直接解析\r \n \t之类的，fastjson开源库可以解析不报错，node.js中JSON.parse()不能解析报错
             *   这里为了易用性支持直接解析
             * */
            if (c == '\\') {
                stringBuilder.append('\\');
                char nextChar = charScanner.nextChar();
                if (nextChar == '"' || nextChar == '\\' || nextChar == 'r' || nextChar == 'n' || nextChar == 'b' || nextChar == 't' || nextChar == 'f') {
                    stringBuilder.append(nextChar);
                } else if (nextChar == 'u') {
                    stringBuilder.append('u');
                    for (int i = 0; i < 4; i++) {
                        char ch = charScanner.nextChar();
                        if (isHex(ch)) {
                            stringBuilder.append(ch);
                        } else {
                            throw new RuntimeException("Invalid character [\\u]");
                        }
                    }
                } else {
                    throw new RuntimeException("Invalid escape character");
                }
            } else if (c == '"') {
                return new Token(TokenType.STRING, stringBuilder.toString());
            } else {
                stringBuilder.append(c);
            }
//            }else if(charScanner.nextChar() == '\r' | charScanner.nextChar() == '\n'){
//                throw new RuntimeException("Invalid escape character [\\r or \\n]");
//            }
        }

    }

    private boolean isHex(char ch) {
        return ((ch >= '0' && ch <= '9') || ('a' <= ch && ch <= 'f')
                || ('A' <= ch && ch <= 'F'));
    }

    private Token readBoolean() {
        if (charScanner.nextChar() == 'r' & charScanner.nextChar() == 'u' & charScanner.nextChar() == 'e') {
            return new Token(TokenType.BOOLEAN, "true");
        } else if (charScanner.nextChar() == 'a' & charScanner.nextChar() == 'l' & charScanner.nextChar() == 's' & charScanner.nextChar() == 'e') {
            return new Token(TokenType.BOOLEAN, "false");
        } else {
            throw new RuntimeException("Invalid json string [boolean]");
        }

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

        TokenList tokenList = new TokenList();
        List<Token> tokenizer = tokenList.tokenizer("0.12");
        System.out.println(tokenizer);
    }
}