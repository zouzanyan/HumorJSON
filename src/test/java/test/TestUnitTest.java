package test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import core.Token;
import core.TokenList;
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
        List<Token> tokenizer = new TokenList().tokenizer(stringBuilder.toString());
        for (int i = 0; i < tokenizer.size(); i++) {
            System.out.println(tokenizer.get(i).getValue());
        }
    }

    @Test
    public void test1() throws IOException {

        List<Token> tokenizer = new TokenList().tokenizer("{\"name\":\"zou\"}");
        for (int i = 0; i < tokenizer.size(); i++) {
            System.out.println(tokenizer.get(i).getValue());
        }
    }

    @Test
    public void test123() throws IOException {

        Object parse = JSON.parse("{\"name\":\"zou\"}");
        System.out.println(parse);


    }



}