package org.example.read_json.rest_controller_json;

import java.util.Map;

public record MakeCast() {
    public static Map<String, Object> makeMap(Object object, String name) throws IllegalArgumentException {
        try {
            return (Map<String, Object>) object;
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException( name+ " MUST BE MAP");
        }
    }
    static Map<String,Object> makeMapAndCheckKey(Map<String,Object> restJson,String keyWord) throws IllegalArgumentException {
            if (!restJson.containsKey(keyWord)) {
                throw new IllegalArgumentException("NO " + keyWord);
            }
            return makeMap(restJson.get(keyWord),keyWord);
    }
    static String makeString(Map<String, Object> configJson, String keyWord, boolean requiredParameter) throws IllegalArgumentException {
        try {
            if (configJson.containsKey(keyWord)) {
                return (String) configJson.get(keyWord);
            } else if (!requiredParameter) {
                return "";
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " MUST BE STRING");
        }
        throw new IllegalArgumentException("NO " + keyWord);
    }

    static Boolean makeBoolean(Map<String, Object> configJson, String keyWord, boolean requiredParameter) throws IllegalArgumentException {
        try {
            if (configJson.containsKey(keyWord)) {
                return (Boolean) configJson.get(keyWord);
            } else if (!requiredParameter) {
                return false;
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " MUST BE BOOLEAN");
        }
        throw new IllegalArgumentException("NO " + keyWord);
    }
}
