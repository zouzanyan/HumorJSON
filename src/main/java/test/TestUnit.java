package test;

import com.alibaba.fastjson.JSON;
import core.Token;
import core.TokenList;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class TestUnit {

    public static void main(String[] args) throws IOException {
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
}
