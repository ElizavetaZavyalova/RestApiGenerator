package org.example.analize.select.port_request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.interpretation.Interpretation;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;
import java.util.regex.Pattern;


import static org.example.analize.select.port_request.PortRequest.RegExp.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Request.TableRef.*;

@Slf4j
public abstract class PortRequest<R> implements Interpretation<R> {
    protected PortRequestWithCondition<R> selectNext;
    @Getter
    protected String tableName;
    @Getter
    protected String realTableName;
    protected String id = "id";
    protected String ref;
    TableRef tableRef = TableRef.DEFAULT;

    @AllArgsConstructor
    enum TableRef {
        MANY_TO_ONE(_MANY_TO_ONE), ONE_TO_MANY(_ONE_TO_MANY), ONE_TO_ONE_BY_ID(_ONE_TO_ONE_BY_ID),
        ONE_TO_ONE_BY_TABLE_NAME(_ONE_TO_ONE_BY_TABLE_NAME),
        DEFAULT("");//ONE_TO_MANY
        private final String name;

        public static TableRef getTableRef(String tableName) {
            for (TableRef type : TableRef.values()) {
                if (tableName.startsWith(type.name) && !type.equals(DEFAULT)) {
                    return type;
                }
            }
            return DEFAULT;
        }

        public static String deleteTableRef(String tableName, TableRef tableRef) {
            return tableName.substring(tableRef.name.length());
        }
    }

    record RegExp() {
        static final String IS_CORRECT_TABLE_NAME = "[a-zA-Z_][a-zA-Z0-9_]*";
        static final String JOIN_SPLIT = ":";
        static final int JOIN_CURRENT_REF_PORT = 0;
        static final int JOIN_PREVIOUS_REF_PORT = 1;
        static final int JOIN_TABLE_PORT = 0;
    }

    String makeDefaultNextId() {
        switch (tableRef) {
            case MANY_TO_ONE -> {
                return realTableName + "_id";
            }
            case ONE_TO_ONE_BY_TABLE_NAME -> {
                return realTableName;
            }
            default -> {
                return "id";
            }
        }
    }

    String makeDefaultRef() {
        switch (tableRef) {
            case MANY_TO_ONE, ONE_TO_ONE_BY_ID -> {
                return "id";
            }
            case ONE_TO_ONE_BY_TABLE_NAME -> {
                return selectNext.realTableName;
            }
            default -> {
                return selectNext.realTableName + "_" + selectNext.id;
            }
        }
    }

    private boolean isJoinAndSetItIfJoin(List<String> joins) {
        TableRef joinRef = TableRef.getTableRef(joins.get(0));
        if (joinRef == TableRef.DEFAULT) {
            return false;
        }
        tableRef = joinRef;
        return true;
    }


    void tryFindJoinsEndSetResult(Endpoint parent, boolean isPathFound) {
        List<String> joins = parent.getRealJoinName(tableName, selectNext.tableName);
        if (joins.isEmpty()) {
            joins = parent.getRealJoinName(realTableName, selectNext.realTableName);
        }
        if (joins.size() == 1) {
            if (!isJoinAndSetItIfJoin(joins)) {
                makeSelectManyToMany(joins.get(JOIN_TABLE_PORT), parent);
                tryFindJoinsEndSetResult(parent, false);
                return;
            }
        }
        if (joins.isEmpty() && !isPathFound) {
            findPath(parent);
            tryFindJoinsEndSetResult(parent, true);
            return;
        }
        if (joins.isEmpty()) {
            ref = makeDefaultRef();
            selectNext.id = makeDefaultNextId();
            return;
        }
        selectNext.id = joins.get(JOIN_PREVIOUS_REF_PORT).equals(JOIN_SPLIT) ?
                (makeDefaultNextId()) : joins.get(JOIN_PREVIOUS_REF_PORT);
        ref = joins.get(JOIN_CURRENT_REF_PORT).equals(JOIN_SPLIT) ?
                (makeDefaultRef()) : joins.get(JOIN_CURRENT_REF_PORT);
    }

    void makeSelectManyToMany(String tableName, Endpoint parent) {
        if (tableRef.equals(TableRef.DEFAULT)) {
            tableRef = TableRef.MANY_TO_ONE;
        }
        selectNext = makeSelect(tableName, selectNext, parent);
    }

    protected abstract PortRequestWithCondition<R> makeSelect(String request, PortRequestWithCondition<R> select, Endpoint parent);


    void throwException(String tableName) throws IllegalArgumentException {
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("REQUEST PORT TABLE NO TABLE");
        }
        if (!Pattern.matches(IS_CORRECT_TABLE_NAME, tableName)) {
            throw new IllegalArgumentException(tableName + "MUST STARTS ON LETTER OR _ AND CONTAINS LATTER OR _ OR DIGIT");
        }
    }

    void findPath(Endpoint parent) {
        List<String> path = parent.findPath(selectNext.getTableName(), tableName, false);
        if (path.size() == 2) {
            path = parent.findPath(selectNext.getRealTableName(), realTableName, true);
            if (path.size() == 2) {
                return;
            }
        }
        final int START_PORT = 1;
        final int LAST_PORT = path.size() - 1;
        for (int urlPortIndex = START_PORT; urlPortIndex < LAST_PORT; urlPortIndex++) {
            selectNext = makeSelect(path.get(urlPortIndex), selectNext, parent);
        }
    }


    protected void initTableName(String tableName, PortRequestWithCondition<R> select, Endpoint parent) throws IllegalArgumentException {
        tableRef = TableRef.getTableRef(tableName);
        tableName = TableRef.deleteTableRef(tableName, tableRef);
        throwException(tableName);
        this.tableName = tableName;
        realTableName = parent.getRealTableName(tableName);
        this.selectNext = select;
        if (select != null) {
            tryFindJoinsEndSetResult(parent, false);
        }
    }
}
