package org.example.read_json.rest_controller_json.pseudonyms;


import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.deicstra.Dijkstra;


import java.util.*;
import java.util.regex.Pattern;

import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Request.TableRef.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.ENTITY;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Pseudonym.*;
import static org.example.read_json.rest_controller_json.pseudonyms.Pseudonyms.RegExp.*;

@Slf4j
@ToString
public abstract class Pseudonyms {
    Map<String, String> tablesPseudonyms = new HashMap<>();
    Map<String, String> fieldsPseudonyms = new HashMap<>();
    Map<String, List<String>> joinsPseudonyms = new HashMap<>();
    Map<String, List<String>> entityPseudonyms = new HashMap<>();
    Map<String, List<String>> refGraph = new HashMap<>();
    Map<String, List<String>> refGraphReal = new HashMap<>();

    Pseudonyms(Map<String, Map<String, List<String>>> pseudonyms) throws IllegalArgumentException {
        try {
            setAll(pseudonyms);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("in: pseudonyms" + " " + ex.getMessage());
        }
    }

    private void setAll(Map<String, Map<String, List<String>>> pseudonyms) throws IllegalArgumentException {
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
        log.info(joinsPseudonyms.toString());
    }

    private void addPseudonymsToEntity(Map<String, List<String>> entity) {
        entity.forEach((k, v) -> {
            throwExceptionIfNameIsNotCorrect(k);
            v.parallelStream().map(n -> n.split("[-=]", 2)[0]).forEach(Pseudonyms::throwExceptionIfNameIsNotCorrect);
        });
        entityPseudonyms = entity;
    }


    private void addToRefGraph(String name1, String name2, boolean inOneWay) {
        addRefGraph(name1, name2, refGraph);
        addRefGraphReverse(name2, name1, refGraph, inOneWay);
        if (!tablesPseudonyms.containsKey(name1) && !tablesPseudonyms.containsKey(name2)) {
            addRefGraph(name1, name2, refGraphReal);
            addRefGraphReverse(name2, name1, refGraphReal, inOneWay);
        }
    }

    private boolean isVertexInGraph(String vertex, Map<String, List<String>> graph) {
        return graph.containsKey(vertex);
    }

    private void addRefGraphReverse(String vertex, String value, Map<String, List<String>> graph, boolean inOneWay) {
        if (!inOneWay) {
            addRefGraph(vertex, value, graph);
            return;
        }
        if (!isVertexInGraph(vertex, graph)) {
            graph.put(vertex, new ArrayList<>());
        }
    }

    private void addRefGraph(String vertex, String value, Map<String, List<String>> graph) {
        if (isVertexInGraph(vertex, graph)) {
            graph.get(vertex).add(value);
        }
        graph.put(vertex, new ArrayList<>(List.of(value)));
    }

    private String deleteInOneWay(String key, boolean inOneWay) {
        if (inOneWay) {
            return key.substring(_IN_ONE_WAY.length());
        }
        return key;
    }

    private void throwExceptionsInJoins(String key, List<String> values, String[] splitKey) throws IllegalArgumentException {
        if (joinsPseudonyms.containsKey(key)) {
            throw new IllegalArgumentException(key + "in joins is already exist");
        }
        if (splitKey.length != 2) {
            throw new IllegalArgumentException(key + "in joins  is must be like table1:table2");
        }
        if (splitKey[T1].isEmpty() || splitKey[T2].isEmpty() || splitKey[T1].equals(splitKey[T2])) {
            throw new IllegalArgumentException(key + "in joins  is must be like table1:table2");
        }
        if (values.size() != 2 && values.size() != 1) {
            throw new IllegalArgumentException(key + "in joins  is must be [ref1, ref2] or  [:, ref2] or[ref1, :] or [:,:] or [table]");
        }
        throwExceptionIfNameIsNotCorrect(splitKey[T1]);
        if (values.size() == 1) {
            if (values.get(T1).isEmpty()) {
                throw new IllegalArgumentException(key + "in joins  is must be [Table] ");
            }
        } else if (values.get(T1).isEmpty() || values.get(T2).isEmpty()) {
            throwExceptionIfNameIsNotCorrect(splitKey[T2]);
            throwExceptionIfNameIsNotCorrect(values.get(T1));
            throwExceptionIfNameIsNotCorrect(values.get(T2));
            throw new IllegalArgumentException(key + "in joins  is must be [ref1, ref2] or  [:, ref2] or [ref1, :]");
        }
    }

    private void addPseudonymsToJoins(Map<String, List<String>> joins) throws IllegalArgumentException {
        joins.forEach((key, values) -> {
            final boolean IN_ONE_WAY = key.startsWith(_IN_ONE_WAY);
            key = deleteInOneWay(key, IN_ONE_WAY);
            String[] splitKey = key.split(SPLIT);
            throwExceptionsInJoins(key, values, splitKey);
            addToRefGraph(splitKey[T1], splitKey[T2], IN_ONE_WAY);
            joinsPseudonyms.put(key, values);
            if (IN_ONE_WAY) {
                return;
            }
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

    public List<String> findPath(String table1, String table2, boolean real) {
        if (!real) {
            return new Dijkstra(refGraph).findPath(table1, table2);
        }
        return new Dijkstra(refGraphReal).findPath(table1, table2);
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
        throw new IllegalArgumentException("joins:" + key + " is not found");
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

    String getTablePseudonymRef(String pseudonymName) {
        if (pseudonymName.startsWith(_IN_ONE_WAY)) {
            return _IN_ONE_WAY;
        }
        return _DEFAULT;
    }

    String deleteTablePseudonymRef(String pseudonymName, String ref) {
        return pseudonymName.substring(ref.length());
    }


    void addPseudonymsToTables(Map<String, List<String>> tables) throws IllegalArgumentException {
        tables.forEach((key, values) -> {
            throwExceptionIfNameIsNotCorrect(key);
            for (String val : values) {
                String ref = getTablePseudonymRef(val);
                val = deleteTablePseudonymRef(val, ref);
                throwExceptionIfNameIsNotCorrect(val);
                if (tablesPseudonyms.containsKey(val)) {
                    throw new IllegalArgumentException("pseudonyms of table:" + val + " is already exist");
                }
                if (val.isEmpty()) {
                    throw new IllegalArgumentException("pseudonyms of table " + key + "is empty");
                }
                tablesPseudonyms.put(val, ref + key);
            }
        });
    }

    void addPseudonymsToFields(Map<String, List<String>> fields) throws IllegalArgumentException {
        fields.forEach((key, values) -> {
            throwExceptionIfNameIsNotCorrect(key);
            for (String val : values) {
                throwExceptionIfNameIsNotCorrect(val);
                if (fieldsPseudonyms.containsKey(val)) {
                    throw new IllegalArgumentException("pseudonym:" + val + " is already exist");
                }
                if (val.isEmpty()) {
                    throw new IllegalArgumentException("pseudonyms of field: " + key + "is empty");
                }
                fieldsPseudonyms.put(val, key);
            }
        });
    }

    static void throwExceptionIfNameIsNotCorrect(String name) throws IllegalArgumentException {
        if (!Pattern.matches(IS_CORRECT_NAME, name)) {
            throw new IllegalArgumentException(name + "must starts on later or _ and contains later or digit or _");
        }
    }

    record RegExp() {
        static final String SPLIT = ":";
        static final int T1 = 0;
        static final int T2 = 1;
        static final String IS_CORRECT_NAME = "[a-zA-Z_]\\w*";
    }


}
