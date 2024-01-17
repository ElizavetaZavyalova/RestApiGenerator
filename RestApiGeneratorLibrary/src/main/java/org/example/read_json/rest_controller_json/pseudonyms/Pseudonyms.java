package org.example.read_json.rest_controller_json.pseudonyms;


import org.example.read_json.rest_controller_json.RestJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.read_json.rest_controller_json.pseudonyms.Pseudonyms.KeyWards.*;


public abstract class Pseudonyms {
    Map<String, String> tablesPseudonyms = new HashMap<>();
    Map<String, String> fieldsPseudonyms = new HashMap<>();

    Pseudonyms(Map<String, Map<String, List<String>>> pseudonyms)throws IllegalArgumentException {

        if (pseudonyms.containsKey(TABLES)) {
            addPseudonymsToTables(pseudonyms.get(TABLES));
        }
        if (pseudonyms.containsKey(FIELDS)) {
            addPseudonymsToFields(pseudonyms.get(FIELDS));
        }
    }

    public String getRealTableName(String key) {
        if (isContainsTablePseudonym(key)) {
            return tablesPseudonyms.get(key);
        }
        return key;
    }
    public boolean isContainsTablePseudonym(String key){
        return tablesPseudonyms.containsKey(key);
    }
    public boolean isContainsFieldPseudonym(String key){
        return tablesPseudonyms.containsKey(key);
    }

    public String getRealFieldName(String key) {
        if (isContainsFieldPseudonym(key)) {
            return fieldsPseudonyms.get(key);
        }
        return key;
    }

    void addPseudonymsToTables(Map<String, List<String>> tables) throws IllegalArgumentException {
        tables.forEach((key, vals) -> {
            for (String val : vals) {
                if (tablesPseudonyms.containsKey(val)) {
                    throw new IllegalArgumentException("PSEUDONYM OF TABLE:" + val + " IS ALREADY EXIST");
                }
                tablesPseudonyms.put(val, key);
            }
        });
    }

    void addPseudonymsToFields(Map<String, List<String>> fields) throws IllegalArgumentException {
        fields.forEach((key, vals) -> {
            for (String val : vals) {
                if (fieldsPseudonyms.containsKey(val)) {
                    throw new IllegalArgumentException("PSEUDONYM OF FIELD:" + val + " IS ALREADY EXIST");
                }
                fieldsPseudonyms.put(val, key);
            }
        });
    }

    record KeyWards() {
        static final String TABLES = "tables";
        static final String FIELDS = "fields";
    }
}
