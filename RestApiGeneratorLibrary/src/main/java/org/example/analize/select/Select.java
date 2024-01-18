package org.example.analize.select;

import org.example.analize.interpretation.Interpretation;
import org.example.analize.where.Where;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;

import static org.example.analize.select.Select.RegExp.*;

public abstract class Select<R> extends Interpretation<R> {
    protected final Select<R> selectNext;
    protected Where<R> where;
    protected final String tableName;
    protected final String realTableName;
    protected String id="id";
    protected String ref;
    record RegExp() {
        static final String SPLIT = "/";
        static final String JOIN_SPLIT = ":";
        static final int SPLIT_LIMIT=1;
        static final int TABLE_PORT=0;
        static final int WHERE_PORT=1;
        static final int REQUEST_PORTS_MAX_LENGTH=2;
        static final int JOIN_CURRENT_REF_PORT=1;
        static final int JOIN_PREVIOUS_REF_PORT =0;
    }
    Select(String request, Select<R> select, Endpoint parent){
        String[] requestPorts=request.split(SPLIT,SPLIT_LIMIT);
        tableName=requestPorts[TABLE_PORT];
        realTableName=parent.getRealTableName(tableName);
        if(requestPorts.length==REQUEST_PORTS_MAX_LENGTH){
            where=makeWhere(requestPorts[WHERE_PORT],tableName,parent);
        }
        this.selectNext=select;
        if(select!=null) {
            tryFindJoinsEndSetResult(parent);
        }
    }
    void tryFindJoinsEndSetResult(Endpoint parent){
        List<String> joins=parent.getRealJoinName(tableName, selectNext.tableName);
        if(joins.isEmpty()){
            joins= parent.getRealJoinName(realTableName, selectNext.realTableName);
        }
        if(joins.isEmpty()) {
            ref=selectNext.realTableName+"_"+selectNext.id;
            selectNext.id="id";
            return;
        }
        selectNext.id=joins.get(JOIN_PREVIOUS_REF_PORT).equals(JOIN_SPLIT)?
                ("id") :joins.get(JOIN_PREVIOUS_REF_PORT);
        ref=joins.get(JOIN_CURRENT_REF_PORT).equals(JOIN_SPLIT)?
                (selectNext.realTableName+"_"+selectNext.id):joins.get(JOIN_CURRENT_REF_PORT);
    }

    abstract Where<R> makeWhere(String request, String tableName,Endpoint parent);
}
