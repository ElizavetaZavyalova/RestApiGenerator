package org.example.read_json.rest_controller_json.pseudonyms;


import lombok.extern.slf4j.Slf4j;
import org.example.analize.deicstra.Dijkstra;

import java.util.*;

import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Request.TableRef.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.ENTITY;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Pseudonym.*;
import static org.example.read_json.rest_controller_json.pseudonyms.Pseudonyms.RegExp.*;

@Slf4j
public abstract class Pseudonyms {
    Map<String, String> tablesPseudonyms = new HashMap<>();
    Map<String, String> fieldsPseudonyms = new HashMap<>();
    Map<String, List<String>> joinsPseudonyms = new HashMap<>();
    Map<String, List<String>> entityPseudonyms = new HashMap<>();
    Map<String, List<String>> refGraph = new HashMap<>();


    Pseudonyms(Map<String, Map<String, List<String>>> pseudonyms) throws IllegalArgumentException {

        if (pseudonyms.containsKey(TABLES)) {
            addPseudonymsToTables(pseudonyms.get(TABLES));
        }
        if (pseudonyms.containsKey(FIELDS)) {
            addPseudonymsToFields(pseudonyms.get(FIELDS));
        }
        if (pseudonyms.containsKey(JOINS)) {
             addPseudonymsToJoins(pseudonyms.get(JOINS));
        }
        if (pseudonyms.containsKey(ENTITY)) {
            addPseudonymsToEntity(pseudonyms.get(ENTITY));
        }
    }
    void addPseudonymsToEntity(Map<String, List<String>> entity){
        entityPseudonyms=entity;
    }
    void addToRefGraph(String name1,String name2){
        addRefGraph(name1,name2);
        addRefGraph(name2,name1);
    }
    void addRefGraph(String vertex,String value){
        if(refGraph.containsKey(vertex)){
            refGraph.get(vertex).add(value);
        }
        refGraph.put(vertex,new ArrayList<>(List.of(value)));
    }

    void addPseudonymsToJoins(Map<String, List<String>> joins) throws IllegalArgumentException {
        joins.forEach((key, values) -> {
            log.info(key+values.toString());
            if (joinsPseudonyms.containsKey(key)) {
                throw new IllegalArgumentException("JOINS:" + key + " IS ALREADY EXIST");
            }
            String[] splitKey = key.split(SPLIT);
            if (splitKey.length != 2) {
                throw new IllegalArgumentException("JOINS:" + key + " IS MUST BE LIKE T1:T2");
            }
            if (splitKey[T1].isEmpty() || splitKey[T2].isEmpty()) {
                throw new IllegalArgumentException("JOINS:" + key + " IS MUST BE LIKE T1:T2");
            }
            if (values.size() != 2 && values.size() != 1) {
                throw new IllegalArgumentException("JOINS:" + key + " MUST BE [ref1, ref2] OR  [:, ref2] or[ref1, :] or [:,:] or [table]");
            }
            if (values.size() == 1) {
                if (values.get(T1).isEmpty()) {
                    throw new IllegalArgumentException("JOINS:" + key + " MUST BE [Table] ");
                }
            } else if (values.get(T1).isEmpty() || values.get(T2).isEmpty()) {
                throw new IllegalArgumentException("JOINS:" + key + " MUST BE [ref1, ref2] OR  [:, ref2] or[ref1, :]");

            }
            addToRefGraph(splitKey[T1],splitKey[T2]);
            joinsPseudonyms.put(key, values);
            String revKey = splitKey[T2] + SPLIT + splitKey[T1];
            List<String> revValues = new ArrayList<>(values);
            Collections.reverse(revValues);
            revValues = revValues.stream().map(v -> {
                if (v.equals(_ONE_TO_MANY)) return _MANY_TO_ONE;
                if (v.equals(_MANY_TO_ONE)) return _ONE_TO_MANY;
                return v;
            }).toList();
            joinsPseudonyms.put(revKey, revValues);
        });
    }
   public List<String> findPath(String table1 ,String table2){
        return new Dijkstra(refGraph).findPath(table1,table2);
    }

    public String getRealTableName(String key) {
        if (isContainsTablePseudonym(key)) {
            return tablesPseudonyms.get(key);
        }
        return key;
    }

    public boolean isContainsJoinPseudonym(String key) {
        return joinsPseudonyms.containsKey(key);
    }

    public List<String> getRealJoinsName(String key) throws IllegalArgumentException {
        if (isContainsJoinPseudonym(key)) {
            return joinsPseudonyms.get(key);
        }
        throw new IllegalArgumentException("JOINS:" + key + " IS NOT FOUND");
    }
    public List<String> getRealEntity(String key) throws IllegalArgumentException {
        if (isContainsEntityPseudonym(key)) {
            return entityPseudonyms.get(key);
        }
         return List.of(key);
    }

    public boolean isContainsTablePseudonym(String key) {
        return tablesPseudonyms.containsKey(key);
    }
    public boolean isContainsEntityPseudonym(String key) {
        return entityPseudonyms.containsKey(key);
    }

    public boolean isContainsFieldPseudonym(String key) {
        return fieldsPseudonyms.containsKey(key);
    }

    public String getRealFieldName(String key) {
        if (isContainsFieldPseudonym(key)) {
            return fieldsPseudonyms.get(key);
        }
        return key;
    }

    void addPseudonymsToTables(Map<String, List<String>> tables) throws IllegalArgumentException {
        tables.forEach((key, vals) -> {// //TODO is Correct name-key
            for (String val : vals) {
                if (tablesPseudonyms.containsKey(val)) {
                    throw new IllegalArgumentException("PSEUDONYM OF TABLE:" + val + " IS ALREADY EXIST");
                }
                if (val.isEmpty()) {
                    throw new IllegalArgumentException("PSEUDONYM OF TABLE " + key + "isEmty");
                }
                tablesPseudonyms.put(val, key);
            }
        });
    }

    void addPseudonymsToFields(Map<String, List<String>> fields) throws IllegalArgumentException {
        fields.forEach((key, vals) -> {//TODO is Correct name-key
            for (String val : vals) {
                if (fieldsPseudonyms.containsKey(val)) {
                    throw new IllegalArgumentException("PSEUDONYM OF FIELD:" + val + " IS ALREADY EXIST");
                }
                if (val.isEmpty()) {
                    throw new IllegalArgumentException("PSEUDONYM OF TABLE " + key + "isEmpty");
                }
                fieldsPseudonyms.put(val, key);
            }
        });
    }

    record RegExp() {
        static final String SPLIT = ":";
        static final int T1 = 0;
        static final int T2 = 1;
    }


}
