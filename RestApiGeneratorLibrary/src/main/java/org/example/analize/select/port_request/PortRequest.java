package org.example.analize.select.port_request;

import lombok.AllArgsConstructor;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.premetive.BaseFieldParser;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


import static org.example.analize.select.port_request.PortRequest.RegExp.*;

public abstract class PortRequest<R, C> implements Interpretation<R> {
    protected PortRequestWithCondition<R, C> selectNext;

    protected String tableName;
    protected String realTableName;
    protected String id = "id";
    protected String ref;
    TableRef tableRef=TableRef.DEFAULT;
    @AllArgsConstructor
    enum TableRef{
        MANY_TO_ONE(">"),ONE_TO_MANY("<"),ONE_TO_ONE(":"), DEFAULT("");
        private final String name;
        static public TableRef getTableRef(String tableName ){
            for (TableRef type :TableRef.values()) {
                if (tableName.startsWith(type.name)&&!type.equals(DEFAULT)) {
                    return type;
                }
            }
            return DEFAULT;
        }
        static public String deleteTableRef(String tableName,TableRef tableRef){
            return tableName.substring(tableRef.name.length());
        }
    }

    record RegExp() {
        static final String IS_CORRECT_TABLE_NAME = "[a-zA-Z_][a-zA-Z0-9_]*";
        static final String JOIN_SPLIT = ":";
        static final int JOIN_CURRENT_REF_PORT = 1;
        static final int JOIN_PREVIOUS_REF_PORT = 0;
    }
    String makeDefaultNextId(){
        if (tableRef.equals(TableRef.MANY_TO_ONE)) {
            return realTableName+"_id";
        }
        return "id";
    }
    String makeDefaultRef(){
        switch (tableRef){
            case MANY_TO_ONE,ONE_TO_ONE -> {
                return  "id";
            }
            default->{return selectNext.realTableName + "_" + selectNext.id;}
        }
    }

    void tryFindJoinsEndSetResult(Endpoint parent) {
        List<String> joins = parent.getRealJoinName(tableName, selectNext.tableName);
        if (joins.isEmpty()) {
            joins = parent.getRealJoinName(realTableName, selectNext.realTableName);
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


    void throwException(String tableName) throws IllegalArgumentException {
        if (tableName.isEmpty()) {
            throw new IllegalArgumentException("REQUEST PORT TABLE NO TABLE");
        }
        if (!Pattern.matches(IS_CORRECT_TABLE_NAME, tableName)) {
            throw new IllegalArgumentException(tableName + "MUST STARTS ON LETTER OR _ AND CONTAINS LATTER OR _ OR DIGIT");
        }
    }


    protected void initTableName(String tableName, PortRequestWithCondition<R, C> select, Endpoint parent) throws IllegalArgumentException {
       tableRef= TableRef.getTableRef(tableName);
       tableName= TableRef.deleteTableRef(tableName,tableRef);
        throwException(tableName);
        this.tableName=tableName;
        realTableName = parent.getRealTableName(tableName);
        this.selectNext = select;
        if (select != null) {
            tryFindJoinsEndSetResult(parent);
        }
    }
}
