package org.example.analize.select.port_request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.interpretation.Interpretation;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


import static org.example.analize.select.port_request.BasePortRequest.RegExp.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Request.TableRef.*;

@Slf4j
public abstract class BasePortRequest<R> implements Interpretation<R> {
    @Getter
    protected PortRequestWithCondition<R> selectNext;
    @Getter
    protected String tableName;
    @Getter
    protected String realTableName;
    @Getter
    protected String id = "id";
    @Getter
    protected String ref;
    @Getter
    boolean refAddressPortIsManyToMay=false;
    boolean isRefByRealName=true;
    TableRef tableRef = TableRef.DEFAULT;


    protected String getRefNameTable() {
       if(isRefByRealName) {
           return realTableName;
       }
       return tableName;
    }

    @AllArgsConstructor
    enum TableRef {
        MANY_TO_ONE(_MANY_TO_ONE),
        ONE_TO_MANY(_ONE_TO_MANY),

        DEFAULT(_DEFAULT);//ONE_TO_MANY

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

    protected abstract PortRequestWithCondition<R> makePortRequest(String tableName, PortRequestWithCondition<R> select, Endpoint parent, boolean isPathFound);

    record RegExp() {
        static final String IS_CORRECT_TABLE_NAME = "[a-zA-Z_]\\w*";
        static final String JOIN_SPLIT = ":";
        static final int JOIN_CURRENT_REF_PORT = 0;
        static final int JOIN_PREVIOUS_REF_PORT = 1;
        static final int JOIN_TABLE_PORT = 0;
    }

    String makeDefaultNextId() {
        if (Objects.requireNonNull(tableRef) == TableRef.MANY_TO_ONE) {
            return getRefNameTable() + "_id";
        }
        return "id";
    }

    String makeDefaultRef() {
        if (Objects.requireNonNull(tableRef) == TableRef.MANY_TO_ONE) {
            return "id";
        }
        return selectNext.getRefNameTable() + "_" + "id";
    }

    private boolean isJoinAndSetItIfJoin(List<String> joins) {
        TableRef joinRef = TableRef.getTableRef(joins.get(0));
        if (joinRef == TableRef.DEFAULT) {
            return false;
        }
        tableRef = joinRef;
        return true;
    }

    List<String> findJoins(Endpoint parent, boolean isTableRefFound) {
        List<String> joins = parent.getRealJoinName(tableName, selectNext.tableName);
        if (!isJoinsFound(joins)) {
            joins = parent.getRealJoinName(realTableName, selectNext.realTableName);
        }
        if ((joins.size() == 1 && (!isJoinAndSetItIfJoin(joins))) && (!isTableRefFound)) {
            makeSelectManyToMany(joins.get(JOIN_TABLE_PORT), parent);
            return findJoins(parent, true);
        }
        if (isTableRefFound && !isJoinsFound(joins)) {
            return List.of(":", ":");
        }
        return joins;
    }

    boolean isJoinsFound(List<String> joins) {
        return !joins.isEmpty();
    }

    void setJoins(List<String> joins) {
        if (!isJoinsFound(joins)) {
            ref = makeDefaultRef();
            selectNext.id = makeDefaultNextId();
            return;
        }
        selectNext.id = joins.get(JOIN_PREVIOUS_REF_PORT).equals(JOIN_SPLIT) ?
                (makeDefaultNextId()) : joins.get(JOIN_PREVIOUS_REF_PORT);
        ref = joins.get(JOIN_CURRENT_REF_PORT).equals(JOIN_SPLIT) ?
                (makeDefaultRef()) : joins.get(JOIN_CURRENT_REF_PORT);
    }


    void tryFindJoinsEndSetResult(Endpoint parent, boolean isPathFound) {
        List<String> joins = findJoins(parent, false);
        if (!isJoinsFound(joins) && !isPathFound) {
            findPath(parent);
            tryFindJoinsEndSetResult(parent, true);
            return;
        }
        setJoins(joins);
    }


    void makeSelectManyToMany(String tableName, Endpoint parent) {
        if (tableRef.equals(TableRef.DEFAULT)) {
            tableRef = TableRef.MANY_TO_ONE;
        }
        refAddressPortIsManyToMay=true;
        selectNext = makePortRequest(tableName, selectNext, parent, true);
    }

    protected abstract PortRequestWithCondition<R> makeSelect(String request, PortRequestWithCondition<R> select, Endpoint parent);


    void throwException(String tableName) throws IllegalArgumentException {
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("request port table is empty");
        }
        if (!Pattern.matches(IS_CORRECT_TABLE_NAME, tableName)) {
            throw new IllegalArgumentException(tableName + "must starts on later or _ and contains later or digit or _");
        }
    }

    boolean isNoPath(List<String> path) {
        return path.size() == 2;
    }

    List<String> makePath(Endpoint parent) {
        List<String> path = parent.findPath(selectNext.getTableName(), tableName, false);
        if (isNoPath(path)) {
            path = parent.findPath(selectNext.getRealTableName(), realTableName, true);
            return path;
        }
        return path;
    }

    void findPath(Endpoint parent) {
        List<String> path = makePath(parent);
        if (isNoPath(path)) {
            return;
        }
        final int START_PORT = 1;
        final int LAST_PORT = path.size() - 1;
        for (int urlPortIndex = START_PORT; urlPortIndex < LAST_PORT; urlPortIndex++) {
            selectNext = makePortRequest(path.get(urlPortIndex), selectNext, parent, true);
        }
    }

    protected void setJoins(Endpoint parent, boolean isPathFound) {
        if (isSelectExist()) {
            tryFindJoinsEndSetResult(parent, isPathFound);
        }
    }
    protected String getRealTableNameAndSetRef(Endpoint parent){
        String realName=parent.getRealTableName(tableName);
        if(realName.startsWith(_IN_ONE_WAY)){
            this.isRefByRealName=false;
            return realName.substring(_IN_ONE_WAY.length());
        }
        return realName;
    }

    protected boolean isSelectExist() {
        return selectNext != null;
    }


    protected void initTableName(String tableName, PortRequestWithCondition<R> select, Endpoint parent) throws IllegalArgumentException {
        tableRef = TableRef.getTableRef(tableName);
        tableName = TableRef.deleteTableRef(tableName, tableRef);
        throwException(tableName);
        this.tableName = tableName;
        realTableName = getRealTableNameAndSetRef(parent);
        this.selectNext = select;
    }
}
