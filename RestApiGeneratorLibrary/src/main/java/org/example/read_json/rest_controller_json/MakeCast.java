package org.example.read_json.rest_controller_json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record MakeCast() {
    public static Map<String, Object> makeMap(Object object, String name) throws IllegalArgumentException {
        try {
            return (Map<String, Object>) object;
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(name + " MUST BE MAP");
        }
    }
    public static Map<String, String> makeStringMap(Map<String, Object> restJson, String keyWord,boolean requiredParameter) throws IllegalArgumentException {
        try {
            if (restJson.containsKey(keyWord)) {
                return (Map<String, String>) restJson.get(keyWord);
            } else if (!requiredParameter) {
                return new HashMap<>();
            }
            throw new IllegalArgumentException("NO " + keyWord);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " MUST BE STRING MAP");
        }
    }

    static Map<String, Object> makeMapAndCheckKey(Map<String, Object> restJson, String keyWord) throws IllegalArgumentException {
        if (!restJson.containsKey(keyWord)) {
            throw new IllegalArgumentException("NO " + keyWord);
        }
        return makeMap(restJson.get(keyWord), keyWord);
    }

    static String makeFromStringList(Map<String, Object> configJson, String keyWord, boolean requiredParameter) throws IllegalArgumentException {
        try {
            if (configJson.containsKey(keyWord)) {
                return String.join("", (List<String>) configJson.get(keyWord));
            } else if (!requiredParameter) {
                return "";
            }
            throw new IllegalArgumentException("NO " + keyWord);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " MUST BE LIST OF STRING");
        }
    }

    static String makeStringFromStringOrList(Map<String, Object> configJson, String keyWord1, String keyWord2, boolean requiredParameter) throws IllegalArgumentException {
        if (configJson.containsKey(keyWord1) && configJson.containsKey(keyWord2)) {
            throw new IllegalArgumentException(keyWord1 + " AND " + keyWord2 + " NOT USE IN ONE PLACE");
        }
        if (configJson.containsKey(keyWord1)){
            return MakeCast.makeString(configJson, keyWord1, requiredParameter);
        }
        return makeFromStringList(configJson, keyWord2, requiredParameter);

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
    public static Map<String, Map<String, List<String>>> makeMapOfMapOfList(Map<String, Object> configJson, String keyWord, boolean requiredParameter) throws IllegalArgumentException{
        try {
            if (configJson.containsKey(keyWord)) {
                return ( Map<String, Map<String, List<String>>>) configJson.get(keyWord);
            } else if (!requiredParameter) {
                return new HashMap<>();
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " MUST BE MAP OF MAP OF LIST");
        }
        throw new IllegalArgumentException("NO " + keyWord);
    }
}
