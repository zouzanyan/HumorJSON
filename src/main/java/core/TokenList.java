package core;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokenList {

    private List<Token> tokens = new ArrayList<>();
    private CharScanner charScanner;

    // permissive 宽容模式  strict 严格模式
    private String checkMode = "permissive";

    // 记录当前指针位置
    private int pos = 0;

    public void add(Token token) {
        tokens.add(token);
    }

    public Token next() {
        return tokens.get(pos++);
    }

    public Token peekPrevious() {
        return pos - 1 < 0 ? null : tokens.get(pos - 2);
    }

    public Token current(){
        return tokens.get(pos);
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

            // 如果是空格，换行符等等直接跳过
            char nextChar = charScanner.nextChar();
            if (isWhiteSpace(nextChar)) {
                continue;
            }

            Token token = parseToken(nextChar);
            tokens.add(token);
        }
        tokens.add(new Token(TokenType.END_DOCUMENT, null));
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
        char c = charScanner.peekChar();
        ;
        if (c == '-') {
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
        if (c == '.') {
            number.append(c);
            // 小数点后第一位不是数字异常
            c = charScanner.nextChar();
            if (!isDigit(c)) {
                throw new RuntimeException("Invalid fraction");
            }
            number.append(c);

            while (isDigit(c = charScanner.nextChar())) {
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

        if (checkMode == "permissive") {
            return readPermissiveStringToken();
        }
        return parseStringToken();

    }

    // 通用解析,参考ECMA404标准
    private Token parseStringToken() {
        StringBuilder stringBuilder = new StringBuilder();
        char c;
        while ((c = charScanner.nextChar()) != '\"') {
            // 判断转义符号
            if (c == '\\') {
//                stringBuilder.append(c);

                char nextChar = charScanner.nextChar();
                if (nextChar == '"' || nextChar == '\\' || nextChar == 'r' || nextChar == 'n' || nextChar == 'b' || nextChar == 't' || nextChar == 'f' || nextChar == '/') {
                    stringBuilder.append(c);
                    stringBuilder.append(nextChar);
                } else if (nextChar == 'u') {
                    // 处理unicode转义
                    stringBuilder.append(nextChar);
                    for (int i = 0; i < 4; i++) {
                        c = charScanner.nextChar();
                        // u后面连续4个字符必须是16进制
                        if (isHex(c)) {
                            stringBuilder.append(c);
                        } else {
                            throw new RuntimeException("Invalid character [\\u]");
                        }
                    }
                } else {
                    throw new RuntimeException("Invalid Escape Character");
                }
            } else if ((c = charScanner.nextChar()) == '\r' || (c = charScanner.nextChar()) == '\n') {
                // JSON字符串不允许有未转义的 换行符 (\n) 或回车符 (\r)
                throw new RuntimeException("Invalid escape character [\\r or \\n]");
            } else {
                stringBuilder.append(c);
            }

        }
        return new Token(TokenType.STRING, stringBuilder.toString());
    }

    // 宽容模式解析JSON String,用于一些JSON字符串不规范的解析
    private Token readPermissiveStringToken() {

        StringBuilder stringBuilder = new StringBuilder();
        char c;
        while ((c = charScanner.nextChar()) != '\"') {
            stringBuilder.append(c);
        }
        return new Token(TokenType.STRING, stringBuilder.toString());

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

    private boolean isWhiteSpace(char ch) {
        return (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n');
    }

}

