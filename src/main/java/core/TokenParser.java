package core;

import model.JSONArray;
import model.JSONObject;

public class TokenParser {

    private CharParser tokens;


    public Object parse(CharParser tokens) {
        this.tokens = tokens;
        Token nextToken = tokens.next();
        if (nextToken.getTokenType() == TokenType.BEGIN_OBJECT) {
            return parseJSONObject();
        } else if (nextToken.getTokenType() == TokenType.BEGIN_ARRAY) {
            return parseJSONArray();
        } else if (nextToken.getTokenType() == TokenType.STRING || nextToken.getTokenType() == TokenType.BOOLEAN || nextToken.getTokenType() == TokenType.NULL || nextToken.getTokenType() == TokenType.NUMBER) {
            // JSON支持单值类型
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
        } else {
            throw new RuntimeException("Invalid Single Value");
        }
    }

    private Object parseJSONArray() {

        // array的第一次解析不能是',',':'
        int expectCode = TokenType.STRING.getTokenCode() | TokenType.BEGIN_ARRAY.getTokenCode() | TokenType.END_ARRAY.getTokenCode()
                | TokenType.BEGIN_OBJECT.getTokenCode() | TokenType.END_OBJECT.getTokenCode() | TokenType.NUMBER.getTokenCode()
                | TokenType.NULL.getTokenCode();
        JSONArray jsonArray = new JSONArray();
        while (tokens.hasMore()) {
            Token nextToken = tokens.next();
            TokenType tokenType = nextToken.getTokenType();
            String tokenValue = nextToken.getValue();
            switch (tokenType) {
                case STRING:
                    checkExpectToken(tokenType, expectCode);
                    jsonArray.add(tokenValue);
                    // 基本上都是预期 ']' ','
                    expectCode = TokenType.END_ARRAY.getTokenCode() | TokenType.SEP_COMMA.getTokenCode();
                    break;
                case NULL:
                    checkExpectToken(tokenType, expectCode);
                    jsonArray.add(Boolean.valueOf(tokenValue));

                    expectCode = TokenType.END_ARRAY.getTokenCode() | TokenType.SEP_COMMA.getTokenCode();
                    break;
                case NUMBER:
                    checkExpectToken(tokenType, expectCode);
                    try {
                        // 尝试将字符串解析为整数
                        int i = Integer.parseInt(tokenValue);
                        jsonArray.add(i);
                    } catch (NumberFormatException e) {
                        // 如果解析整数失败，尝试解析为浮点数
                        try {
                            double d = Double.parseDouble(tokenValue);
                            jsonArray.add(d);
                        } catch (NumberFormatException ex) {
                            // 如果解析浮点数也失败，则抛出异常
                            throw new RuntimeException("Invalid number format at position: ");
                        }
                    }
                    expectCode = TokenType.END_ARRAY.getTokenCode() | TokenType.SEP_COMMA.getTokenCode();
                    break;

                case BOOLEAN:
                    checkExpectToken(tokenType, expectCode);
                    jsonArray.add(Boolean.valueOf(tokenValue));
                    expectCode = TokenType.END_ARRAY.getTokenCode() | TokenType.SEP_COMMA.getTokenCode();
                    break;

                case BEGIN_OBJECT:
                    checkExpectToken(tokenType, expectCode);
                    jsonArray.add(parseJSONObject());
                    expectCode = TokenType.END_ARRAY.getTokenCode() | TokenType.SEP_COMMA.getTokenCode();
                    break;

                case SEP_COMMA:
                    checkExpectToken(tokenType, expectCode);
                    expectCode = TokenType.STRING.getTokenCode() | TokenType.BEGIN_ARRAY.getTokenCode()
                            | TokenType.BEGIN_OBJECT.getTokenCode() | TokenType.END_OBJECT.getTokenCode() | TokenType.NUMBER.getTokenCode()
                            | TokenType.NULL.getTokenCode() | TokenType.BOOLEAN.getTokenCode();
                    break;

                case BEGIN_ARRAY:
                    checkExpectToken(tokenType, expectCode);
                    jsonArray.add(parseJSONArray());
                    expectCode = TokenType.END_ARRAY.getTokenCode() | TokenType.SEP_COMMA.getTokenCode();
                    break;
                case END_ARRAY:
                    checkExpectToken(tokenType, expectCode);
                    return jsonArray;
                default:
                    throw new RuntimeException("JSONArray Unexpected Token.");
            }
        }
        throw new RuntimeException("JSONArray parse error");


    }

    private Object parseJSONObject() {

        JSONObject jsonObject = new JSONObject();
        String key = null;
        Object value;
        // 第一个token预期只能是'String'或者'}',否者抛异常
        int expectCode = TokenType.STRING.getTokenCode() | TokenType.END_OBJECT.getTokenCode();
        while (tokens.hasMore()) {
            Token nextToken = tokens.next();
            TokenType tokenType = nextToken.getTokenType();
            String tokenValue = nextToken.getValue();
            switch (tokenType) {
                case STRING:
                    checkExpectToken(tokenType, expectCode);
                    // 获取当前的值但不向右移动指针
                    Token token = tokens.current();
                    // 作为键
                    if (token.getTokenType() == TokenType.SEP_COLON) {
                        key = nextToken.getValue();
                        // 期待':'
                        expectCode = TokenType.SEP_COLON.getTokenCode();
                    } else {
                        // 作为值
                        value = nextToken.getValue();
                        jsonObject.put(key, value);
                        // 期待 '}' 或者 ','
                        expectCode = TokenType.END_OBJECT.getTokenCode() | TokenType.SEP_COMMA.getTokenCode();
                    }
                    break;

                case NULL:
                    checkExpectToken(tokenType, expectCode);
                    jsonObject.put(key, null);
                    // 预期'}'或者','
                    expectCode = TokenType.END_OBJECT.getTokenCode() | TokenType.SEP_COMMA.getTokenCode();
                    break;
                case NUMBER:
                    checkExpectToken(tokenType, expectCode);
                    try {
                        // 尝试将字符串解析为整数
                        int i = Integer.parseInt(tokenValue);
                        jsonObject.put(key, i);
                    } catch (NumberFormatException e) {
                        // 如果解析整数失败，尝试解析为浮点数
                        try {
                            double d = Double.parseDouble(tokenValue);
                            jsonObject.put(key, d);
                        } catch (NumberFormatException ex) {
                            // 如果解析浮点数也失败，则抛出异常
                            throw new RuntimeException("Invalid number format at position: ");
                        }
                    }
                    expectCode = TokenType.SEP_COMMA.getTokenCode() | TokenType.END_OBJECT.getTokenCode();

                    break;
                case BOOLEAN:
                    checkExpectToken(tokenType, expectCode);
                    jsonObject.put(key, Boolean.valueOf(tokenValue));
                    // 预期',' '}'
                    expectCode = TokenType.SEP_COMMA.getTokenCode() | TokenType.END_OBJECT.getTokenCode();
                    break;


                case BEGIN_OBJECT:
                    checkExpectToken(tokenType, expectCode);
                    jsonObject.put(key, parseJSONObject());
                    // 预期',' 或者 '}'
                    expectCode = TokenType.SEP_COMMA.getTokenCode() | TokenType.END_OBJECT.getTokenCode();
                    break;

                case END_OBJECT:
                    checkExpectToken(tokenType, expectCode);
                    return jsonObject;

                case SEP_COLON:
                    checkExpectToken(tokenType, expectCode);
                    expectCode = TokenType.STRING.getTokenCode() |
                            TokenType.NULL.getTokenCode() |
                            TokenType.NUMBER.getTokenCode() |
                            TokenType.BOOLEAN.getTokenCode() |
                            TokenType.BEGIN_OBJECT.getTokenCode() |
                            TokenType.BEGIN_ARRAY.getTokenCode();
                    break;
                case SEP_COMMA:
                    checkExpectToken(tokenType, expectCode);
                    expectCode = TokenType.STRING.getTokenCode();
                    break;

                case BEGIN_ARRAY:
                    checkExpectToken(tokenType, expectCode);
                    jsonObject.put(key, parseJSONArray());
                    expectCode = TokenType.SEP_COMMA.getTokenCode() | TokenType.END_OBJECT.getTokenCode();
                    break;
                default:
                    throw new RuntimeException("JSONObject Unexpected Token.");
            }
        }
        throw new RuntimeException("JSONObject parse error");
    }

    private void checkExpectToken(TokenType tokenType, int expectToken) {
        if ((tokenType.getTokenCode() & expectToken) == 0) {
            throw new RuntimeException("Parse error, invalid Token.");
        }
    }

}
