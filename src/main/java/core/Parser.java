package core;

import model.JSONObject;

public class Parser {

    private TokenList tokens;


    public Object parse(TokenList tokens){
        Token nextToken = tokens.next();
        if (nextToken.getTokenType() == TokenType.BEGIN_OBJECT) {
            return parseJSONObject();
        }else if (nextToken.getTokenType() == TokenType.BEGIN_ARRAY) {
            return parseJsonArray();
        } else {
            throw new RuntimeException("Parse error, invalid Token.");

        }
    }

    private Object parseJsonArray() {
        return null;
    }

    private Object parseJSONObject() {
        return null;
    }

}
