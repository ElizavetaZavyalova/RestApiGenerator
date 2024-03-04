package org.example.analize.select.port_request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.analize.where.BaseWhere;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;


import static org.example.analize.select.port_request.PortRequestWithCondition.AggregationFunction.NO;
import static org.example.analize.select.port_request.PortRequestWithCondition.RegExp.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Request.AggregationFunction.*;

public abstract class PortRequestWithCondition<R> extends BasePortRequest<R> {
    @Getter
    protected BaseWhere<R> where;

    record RegExp() {
        static final String SPLIT = "/";
        static final int SPLIT_LIMIT = 2;
        static final int TABLE_PORT = 0;
        static final int WHERE_PORT = 1;
        static final int REQUEST_PORTS_MAX_LENGTH = 2;
    }
    protected boolean isWhereExist(){
        return where!=null;
    }

    protected PortRequestWithCondition(String request, PortRequestWithCondition<R> select, Endpoint parent) throws IllegalArgumentException {
        String[] requestPorts = request.split(SPLIT, SPLIT_LIMIT);
        aggregationFunction = AggregationFunction.getAggregationFunction(requestPorts[TABLE_PORT]);
        super.initTableName(AggregationFunction.deleteAggregationFunction(requestPorts[TABLE_PORT], aggregationFunction), select, parent);
        super.setJoins(parent,false);
        if (requestPorts.length == REQUEST_PORTS_MAX_LENGTH) {
            where = makeWhere(requestPorts[WHERE_PORT], tableName, parent);
        }
    }
    protected PortRequestWithCondition(String tableName, PortRequestWithCondition<R> select, Endpoint parent,boolean isPathFound) throws IllegalArgumentException {
        super.initTableName(tableName, select, parent);
        super.setJoins(parent,isPathFound);
    }

    protected AggregationFunction aggregationFunction = NO;

    @AllArgsConstructor
    public enum AggregationFunction {
        MAX(_MAX), MIN(_MIN), NO("");
        private final String name;

        public static AggregationFunction getAggregationFunction(String tableName) {
            for (AggregationFunction type : AggregationFunction.values()) {
                if (tableName.startsWith(type.name) && !type.equals(NO)) {
                    return type;
                }
            }
            return NO;
        }

        public static String deleteAggregationFunction(String tableName, AggregationFunction func) {
            return tableName.substring(func.name.length());
        }
    }

    protected abstract BaseWhere<R> makeWhere(String request, String tableName, Endpoint parent);
}
