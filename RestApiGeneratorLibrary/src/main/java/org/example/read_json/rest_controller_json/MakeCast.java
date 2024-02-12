package org.example.read_json.rest_controller_json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record MakeCast() {
    public static Map<String, Object> makeMap(Object object, String name) throws IllegalArgumentException {
        try {
            return (Map<String, Object>) object;
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(name + " MUST BE MAP");
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
            throw new IllegalArgumentException("NO " + keyWord);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " MUST BE STRING MAP");
        }
    }

    public static Map<String, Map<String, String>> makeMapOfStringMap(Map<String, Object> restJson, String keyWord, String addField, boolean requiredParameter) throws IllegalArgumentException {
        Map<String, Map<String, String>> mapResult = new HashMap<>();
        try {
            if (restJson.containsKey(keyWord)) {
                Map<String, Object> map = (Map<String, Object>) restJson.get(keyWord);
                map.forEach((k, v) -> {
                    mapResult.put(k, makeMapOrMapFromString(v, addField));
                });
                return mapResult;
            } else if (!requiredParameter) {
                return new HashMap<>();
            }
            throw new IllegalArgumentException("NO " + keyWord);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " MUST BE STRING MAP");
        }
    }

    public static Map<String, Object> makeMapAndCheckKey(Map<String, Object> restJson, String keyWord) throws IllegalArgumentException {
        if (!restJson.containsKey(keyWord)) {
            throw new IllegalArgumentException("NO " + keyWord);
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
            throw new IllegalArgumentException(keyWord + " MUST BE STRING");
        }
        throw new IllegalArgumentException("NO " + keyWord);
    }

    public static String makeString(Object configJson, String keyWord) throws IllegalArgumentException {
        try {
            return (String) Optional.ofNullable(configJson).orElse("");
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " MUST BE STRING");
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
            throw new IllegalArgumentException(keyWord + " MUST BE BOOLEAN");
        }
        throw new IllegalArgumentException("NO " + keyWord);
    }

    public static Map<String, Map<String, List<String>>> makeMapOfMapOfList(Map<String, Object> configJson, String keyWord, boolean requiredParameter) throws IllegalArgumentException {
        try {
            if (configJson.containsKey(keyWord)) {
                return (Map<String, Map<String, List<String>>>) configJson.get(keyWord);
            } else if (!requiredParameter) {
                return new HashMap<>();
            }
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException(keyWord + " MUST BE MAP OF MAP OF LIST");
        }
        throw new IllegalArgumentException("NO " + keyWord);
    }

}
