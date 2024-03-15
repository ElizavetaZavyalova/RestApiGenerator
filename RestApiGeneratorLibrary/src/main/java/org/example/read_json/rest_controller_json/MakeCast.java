package org.example.read_json.rest_controller_json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unchecked")
public record MakeCast() {

    public static Map<String, Object> makeMap(Object object, String name) throws IllegalArgumentException {
        try {
            return (Map<String, Object>) object;
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(name + " must be map");
        }
    }

    public static Map<String, String> makeMapOrMapFromString(Object object, String keyWord) throws IllegalArgumentException {
        try {
            return (Map<String, String>) object;
        } catch (ClassCastException ex) {
            return Map.of(keyWord, makeString(object, keyWord));
        }
    }

    public static Map<String, String> makeStringMap(Map<String, Object> restJson, String keyWord, boolean requiredParameter) throws IllegalArgumentException {
        try {
            if (restJson.containsKey(keyWord)) {
                return (Map<String, String>) restJson.get(keyWord);
            } else if (!requiredParameter) {
                return new HashMap<>();
            }
            throw new IllegalArgumentException("no " + keyWord);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " must be string map");
        }
    }

    public static Map<String, Map<String, String>> makeMapOfStringMap(Map<String, Object> restJson, String keyWord, String addField, boolean requiredParameter) throws IllegalArgumentException {
        Map<String, Map<String, String>> mapResult = new HashMap<>();
        try {
            if (restJson.containsKey(keyWord)) {
                Map<String, Object> map = (Map<String, Object>) restJson.get(keyWord);
                map.forEach((k, v) -> mapResult.put(k, makeMapOrMapFromString(v, addField)));
                return mapResult;
            } else if (!requiredParameter) {
                return new HashMap<>();
            }
            throw new IllegalArgumentException("no " + keyWord);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " must be string map");
        }
    }

    public static Map<String, Object> makeMapAndCheckKey(Map<String, Object> restJson, String keyWord) throws IllegalArgumentException {
        if (!restJson.containsKey(keyWord)) {
            throw new IllegalArgumentException("no " + keyWord);
        }
        return makeMap(restJson.get(keyWord), keyWord);
    }


    public static String makeStringIfContainsKeyMap(Map<String, Object> configJson, String keyWord, boolean requiredParameter) throws IllegalArgumentException {
        try {
            if (configJson.containsKey(keyWord)) {
                return (String) configJson.get(keyWord);
            } else if (!requiredParameter) {
                return "";
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " must be string");
        }
        throw new IllegalArgumentException("no " + keyWord);
    }

    public static String makeStringIfContainsKeyMapElseReturnEmpty(Map<String, Object> configJson, String keyWord) throws IllegalArgumentException {
        try {
            if (configJson.containsKey(keyWord)) {
                return (String) configJson.get(keyWord);
            }
            return "";
        } catch (ClassCastException ignored) {
            return "";
        }
    }

    public static String makeString(Object configJson, String keyWord) throws IllegalArgumentException {
        try {
            return (String) Optional.ofNullable(configJson).orElse("");
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " must be string");
        }

    }
    public static String makeStringOrDefaultEmpty(Object configJson){
        try {
            return (String) Optional.ofNullable(configJson).orElse("");
        } catch (ClassCastException ex) {
            return "";
        }

    }

    public static Boolean makeBoolean(Map<String, Object> configJson, String keyWord, boolean requiredParameter) throws IllegalArgumentException {
        try {
            if (configJson.containsKey(keyWord)) {
                return (Boolean) configJson.get(keyWord);
            } else if (!requiredParameter) {
                return false;
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " must be boolean");
        }
        throw new IllegalArgumentException("no " + keyWord);
    }

    public static Map<String, Map<String, List<String>>> makeMapOfMapOfList(Map<String, Object> configJson, String keyWord, boolean requiredParameter) throws IllegalArgumentException {
        try {
            if (configJson.containsKey(keyWord)) {
                return (Map<String, Map<String, List<String>>>) configJson.get(keyWord);
            } else if (!requiredParameter) {
                return new HashMap<>();
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " must be map of map of list");
        }
        throw new IllegalArgumentException("no " + keyWord);
    }

}
