package test;


import com.alibaba.fastjson.JSON;
import core.TokenParser;
import core.Token;
import core.CharParser;
import model.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


class TestUnitTest {


    @Test
    public void test123() throws IOException {
        Object parse = JSON.parse("{\"name\":\"zou\"}");
        System.out.println(parse);
        JSON.parseObject("", String.class);
    }

    @Test
    public void testsaf(){
        CharParser charParser = new CharParser();
        String json = "{\"a\": 1, \"b\": \"b\", \"c\": {\"a\": 1, \"b\": null, \"d\": [0.1, \"a\", 1,2, 123, 1.23e+10, true, false, null]}}";
        System.out.println(model.JSON.parse(json));

    }

    @Test
    void testdihaw12323() throws IOException, URISyntaxException {
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("music.json"));
        byte[] bytes = Files.readAllBytes(Paths.get("music.json"));
        String json = new String(bytes, "UTF-8");
        Object o = model.JSON.parse(json);
        System.out.println(((JSONObject)o).get("playlist"));

    }

    @Test
    void testasdf() throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("music.json"));
        byte[] bytes = Files.readAllBytes(Paths.get("music.json"));
        String json = new String(bytes, "UTF-8");
        Object parse = JSON.parse(json);

        System.out.println(parse);



    }

    @Test
    public void adwad(){
        Object parse = model.JSON.parse("\"sfdef\"");
        System.out.println(parse);
    }



}