package model;

import core.CharParser;
import core.TokenParser;

public class JSON {

    /*
     *
     *
     * */
    public static Object parse(String jsonStr) {
        CharParser charParser = new CharParser();
        charParser.tokenizer(jsonStr);
        return new TokenParser().parse(charParser);
    }

    public static Object parseLeniently(String jsonStr) {
        CharParser charParser = new CharParser();
        charParser.setCheckMode("permissive");
        charParser.tokenizer(jsonStr);
        return new TokenParser().parse(charParser);
    }

    public static JSONObject parseJSONObject(String jsonStr){
        CharParser charParser = new CharParser();
        charParser.tokenizer(jsonStr);
        Object parse = new TokenParser().parse(charParser);
        if (parse instanceof JSONObject){
            return (JSONObject) parse;
        }else {
            throw new RuntimeException("not JSONObject");
        }
    }

    public static JSONArray parseJSONArray(String jsonStr){
        CharParser charParser = new CharParser();
        charParser.tokenizer(jsonStr);
        Object parse = new TokenParser().parse(charParser);
        if (parse instanceof JSONArray){
            return (JSONArray) parse;
        }else {
            throw new RuntimeException("not JSONArray");
        }
    }




}
