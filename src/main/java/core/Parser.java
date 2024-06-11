package core;

import model.JSONObject;

import java.text.NumberFormat;

public class Parser {

    private TokenList tokens;


    public Object parse(TokenList tokens) {
        Token nextToken = tokens.next();
        if (nextToken.getTokenType() == TokenType.BEGIN_OBJECT) {
            return parseJSONObject();
        } else if (nextToken.getTokenType() == TokenType.BEGIN_ARRAY) {
            return parseJsonArray();
        } else if (nextToken.getTokenType() == TokenType.STRING || nextToken.getTokenType() == TokenType.BOOLEAN || nextToken.getTokenType() == TokenType.NULL || nextToken.getTokenType() == TokenType.NUMBER || nextToken.getTokenType() == TokenType.END_DOCUMENT) {
            // JSON支持单数值类型
            return SingleValue(nextToken);
        } else {

            throw new RuntimeException("Parse error, invalid Token.");

        }
    }

    private Object SingleValue(Token token) {

        if (token.getTokenType() == TokenType.STRING) {
            return token.getValue();
        } else if (token.getTokenType() == TokenType.BOOLEAN) {
            return Boolean.valueOf(token.getValue());
        } else if (token.getTokenType() == TokenType.NULL) {
            return null;
        } else if (token.getTokenType() == TokenType.NUMBER) {
            try {
                // 尝试将字符串解析为整数
                return Integer.parseInt(token.getValue());
            } catch (NumberFormatException e) {
                // 如果解析整数失败，尝试解析为浮点数
                try {
                    return Double.parseDouble(token.getValue());
                } catch (NumberFormatException ex) {
                    // 如果解析浮点数也失败，则抛出异常
                    throw new RuntimeException("Invalid number format at position: ");
                }
            }
        } else if (token.getTokenType() == TokenType.END_DOCUMENT) {
            return "";
        } else {
            throw new RuntimeException("Invalid Single Value");
        }
    }

    private Object parseJsonArray() {
        return null;
    }

    private Object parseJSONObject() {

        JSONObject jsonObject = new JSONObject();
        String key = null;
        Object value;
        // 预期是'String'或者'}'
        int expectCode = TokenType.STRING.getTokenCode() | TokenType.END_OBJECT.getTokenCode();
        while (tokens.hasMore()) {
            Token nextToken = tokens.next();
            TokenType tokenType = nextToken.getTokenType();
            String tokenValue = nextToken.getValue();
            switch (tokenType) {
                case STRING:
                    checkExpectToken(tokenType, expectCode);
                    // 字符串的下一位
                    Token token = tokens.current();
                    // 作为键
                    if (token.getTokenType() == TokenType.SEP_COLON) {
                        key = token.getValue();
                        expectCode = TokenType.SEP_COMMA.getTokenCode();
                    } else {
                        // 作为值
                        value = token.getValue();
                        jsonObject.put(key,value);
                        expectCode = TokenType.END_OBJECT.getTokenCode() | TokenType.SEP_COMMA.getTokenCode();
                    }
                    break;
            }
        }


        return null;
    }

    private void checkExpectToken(TokenType tokenType, int expectToken) {
        if ((tokenType.getTokenCode() & expectToken) == 0) {
            throw new RuntimeException("Parse error, invalid Token.");
        }
    }

}
