package test;

import com.alibaba.fastjson.JSON;

public class TestUnit {

    public static void main(String[] args) {
        Object parse = JSON.parse("[[1,\"\n\",3,\"\u4e2d\"]]");
        Object parse1 = JSON.parse("\"1\n3\"");
        System.out.println(parse);
        System.out.println(parse1);
    }
}
