package model;

import java.util.ArrayList;
import java.util.List;

public class JSONArray{

    private final List<Object> list = new ArrayList<>();

    public void add(Object obj) {
        list.add(obj);
    }

    public Object get(int index) {
        return list.get(index);
    }

    public int size() {
        return list.size();
    }

    public JSONObject getJSONObject(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JSONObject)) {
            throw new RuntimeException("Type of value is not JSONObject");
        }

        return (JSONObject) obj;
    }

    public JSONArray getJSONArray(int index) {
        Object obj = list.get(index);
        if (!(obj instanceof JSONArray)) {
            throw new RuntimeException("Type of value is not JSONArray");
        }

        return (JSONArray) obj;
    }


    @Override
    public String toString() {
        return "JSONArray{" +
                "list=" + list +
                '}';
    }
}
