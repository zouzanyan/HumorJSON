package test;


import com.alibaba.fastjson.JSON;
import core.TokenParser;
import core.Token;
import core.CharParser;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


class TestUnitTest {
    @Test
    public void test() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("test.json");
        int a;
        StringBuilder stringBuilder = new StringBuilder();
        while ((a = fileInputStream.read()) != -1) {
            stringBuilder.append((char) a);
        }
        System.out.println(stringBuilder.toString());
        List<Token> tokenizer = new CharParser().tokenizer(stringBuilder.toString());
        for (int i = 0; i < tokenizer.size(); i++) {
            System.out.println(tokenizer.get(i).getValue());
        }
    }

    @Test
    public void test1() throws IOException {

        List<Token> tokenizer = new CharParser().tokenizer("{\"name\":\"zou\"}");
        for (int i = 0; i < tokenizer.size(); i++) {
            System.out.println(tokenizer.get(i).getValue());
        }
    }

    @Test
    public void test123() throws IOException {

        Object parse = JSON.parse("{\"name\":\"zou\"}");
        System.out.println(parse);


    }

    @Test
    public void testsaf(){
        CharParser charParser = new CharParser();
        String json = "{\"a\": 1, \"b\": \"b\", \"c\": {\"a\": 1, \"b\": null, \"d\": [0.1, \"a\", 1,2, 123, 1.23e+10, true, false, null]}}";

        charParser.tokenizer(json);
        System.out.println(charParser);
        TokenParser tokenParser = new TokenParser();
        Object parse = tokenParser.parse(charParser);
        System.out.println(parse);
    }



}