package org.example.analize.select.port_request;
import org.example.analize.where.BaseWhere;
import org.example.read_json.rest_controller_json.Endpoint;


import static org.example.analize.select.port_request.PortRequestWithCondition.RegExp.*;

public abstract class PortRequestWithCondition<R,C> extends PortRequest<R,C> {

    protected BaseWhere<R,C> where;

    record RegExp() {
        static final String SPLIT = "/";
        static final int SPLIT_LIMIT=2;
        static final int TABLE_PORT=0;
        static final int WHERE_PORT=1;
        static final int REQUEST_PORTS_MAX_LENGTH=2;
    }
    protected PortRequestWithCondition(String request, PortRequestWithCondition<R,C> select, Endpoint parent) throws  IllegalArgumentException{
        String[] requestPorts=request.split(SPLIT,SPLIT_LIMIT);
        super.initTableName(requestPorts[TABLE_PORT],select, parent);
        if(requestPorts.length==REQUEST_PORTS_MAX_LENGTH){
            where=makeWhere(requestPorts[WHERE_PORT],tableName,parent);
        }
    }
    @Override
    public String requestInterpret() {
        StringBuilder builder=new StringBuilder();
        if(selectNext!=null){
            builder.append(selectNext.requestInterpret()).append("/");
        }
        builder.append(tableName).append("/");
        if(where!=null){
            builder.append(where.requestInterpret());
        }
        return builder.toString();
    }

    protected abstract BaseWhere<R,C> makeWhere(String request, String tableName, Endpoint parent);
}
