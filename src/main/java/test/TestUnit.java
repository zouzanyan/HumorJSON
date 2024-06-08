package test;

import com.alibaba.fastjson.JSON;

public class TestUnit {

    public static void main(String[] args) {
        Object parse1 = JSON.parse("\"\\p\"");
        System.out.println(parse1);
    }
}
