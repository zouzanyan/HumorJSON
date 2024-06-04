package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONObject {

    private final Map<String, Object> map = new HashMap<>();

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public List<Map.Entry<String, Object>> getAllKeyValue() {
        return new ArrayList<>(map.entrySet());
    }

    public JSONObject getJSONObject(String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }

        Object obj = map.get(key);
        if (!(obj instanceof JSONObject)) {
            throw new RuntimeException("Type of value is not JsonObject");
        }

        return (JSONObject) obj;
    }

    public JSONArray getJSONArray(String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }

        Object obj = map.get(key);
        if (!(obj instanceof JSONArray)) {
            throw new RuntimeException("Type of value is not JsonArray");
        }

        return (JSONArray) obj;
    }
}
