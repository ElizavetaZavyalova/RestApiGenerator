package org.example.read_json.rest_controller_json.pseudonyms;


import org.example.read_json.rest_controller_json.RestJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.read_json.rest_controller_json.pseudonyms.Pseudonyms.KeyWards.*;
import static org.example.read_json.rest_controller_json.pseudonyms.Pseudonyms.RegExp.*;


public abstract class Pseudonyms {
    Map<String, String> tablesPseudonyms = new HashMap<>();
    Map<String, String> fieldsPseudonyms = new HashMap<>();
    Map<String, List<String>> joinsPseudonyms=new HashMap<>();

    Pseudonyms(Map<String, Map<String, List<String>>> pseudonyms)throws IllegalArgumentException {

        if (pseudonyms.containsKey(TABLES)) {
            addPseudonymsToTables(pseudonyms.get(TABLES));
        }
        if (pseudonyms.containsKey(FIELDS)) {
            addPseudonymsToFields(pseudonyms.get(FIELDS));
        }
        if (pseudonyms.containsKey(JOINS)) {
            addPseudonymsToJoins(pseudonyms.get(TABLES));
        }
    }
    void addPseudonymsToJoins(Map<String, List<String>> joins) throws IllegalArgumentException {
        joins.forEach((key, vals) -> {
            if (joinsPseudonyms.containsKey(key)) {
                throw new IllegalArgumentException("JOINS:" +key + " IS ALREADY EXIST");
            }
            String[] splitKey=key.split(SPLIT);
            if(splitKey.length!=2){
                throw new IllegalArgumentException("JOINS:" +key + " IS MUST BE LIKE T1:T2");
            }
            if(splitKey[T1].isEmpty()||splitKey[T2].isEmpty()){
                throw new IllegalArgumentException("JOINS:" +key + " IS MUST BE LIKE T1:T2");
            }
            if(vals.size()!=2){
                throw new IllegalArgumentException("JOINS:" +key + " MUST BE [ref1, ref2] OR  [:, ref2] or[ref1, :]");
            }
            if(vals.get(T1).isEmpty()||vals.get(T2).isEmpty()){
                throw new IllegalArgumentException("JOINS:" +key + " MUST BE [ref1, ref2] OR  [:, ref2] or[ref1, :]");
            }
            String revKey=splitKey[T2]+SPLIT+splitKey[T1];
            joinsPseudonyms.put(key,vals);
            joinsPseudonyms.put(revKey,List.of(vals.get(T2), vals.get(T1)));
        });
    }

    public String getRealTableName(String key) {
        if (isContainsTablePseudonym(key)) {
            return tablesPseudonyms.get(key);
        }
        return key;
    }
    public boolean isContainsJoinPseudonym(String key){
        return joinsPseudonyms.containsKey(key);
    }
    public List<String> getRealJoinsName(String key) throws IllegalArgumentException {
        if (isContainsJoinPseudonym(key)) {
            return joinsPseudonyms.get(key);
        }
        throw new IllegalArgumentException("JOINS:" +key + " IS NOT FOUND");
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
    record RegExp(){
        static final String SPLIT= ":";
        static final int T1= 0;
        static final int T2= 1;
    }

    record KeyWards() {
        static final String TABLES = "tables";
        static final String FIELDS = "fields";
        static  final String JOINS="joins";
    }
}
