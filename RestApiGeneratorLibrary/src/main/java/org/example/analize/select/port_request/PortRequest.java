package org.example.analize.select.port_request;

import org.example.analize.interpretation.Interpretation;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;
import java.util.regex.Pattern;

import static java.lang.invoke.MethodHandles.throwException;
import static org.example.analize.select.port_request.PortRequest.RegExp.*;
import static org.example.analize.select.port_request.PortRequestWithCondition.RegExp.TABLE_PORT;

public abstract class PortRequest<R, C> implements Interpretation<R> {
    protected PortRequestWithCondition<R, C> selectNext;

    protected String tableName;
    protected String realTableName;
    protected String id = "id";
    protected String ref;

    record RegExp() {
        static final String IS_CORRECT_TABLE_NAME = "[a-zA-Z_][a-zA-Z0-9_]*";
        static final String JOIN_SPLIT = ":";
        static final int JOIN_CURRENT_REF_PORT = 1;
        static final int JOIN_PREVIOUS_REF_PORT = 0;
    }

    void tryFindJoinsEndSetResult(Endpoint parent) {
        List<String> joins = parent.getRealJoinName(tableName, selectNext.tableName);
        if (joins.isEmpty()) {
            joins = parent.getRealJoinName(realTableName, selectNext.realTableName);
        }
        if (joins.isEmpty()) {
            ref = selectNext.realTableName + "_" + selectNext.id;
            selectNext.id = "id";
            return;
        }
        selectNext.id = joins.get(JOIN_PREVIOUS_REF_PORT).equals(JOIN_SPLIT) ?
                ("id") : joins.get(JOIN_PREVIOUS_REF_PORT);
        ref = joins.get(JOIN_CURRENT_REF_PORT).equals(JOIN_SPLIT) ?
                (selectNext.realTableName + "_" + selectNext.id) : joins.get(JOIN_CURRENT_REF_PORT);
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
        throwException(tableName);
        this.tableName = tableName;
        realTableName = parent.getRealTableName(tableName);
        this.selectNext = select;
        if (select != null) {
            tryFindJoinsEndSetResult(parent);
        }
    }
}
