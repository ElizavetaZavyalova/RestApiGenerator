package org.example.analize.select;

import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.StringWhere;
import org.example.read_json.rest_controller_json.Endpoint;

public class StringSelect extends PortRequestWithCondition<String,String> {
    public StringSelect(String request, PortRequestWithCondition<String,String> select, Endpoint parent) {
        super(request, select, parent);
    }

    @Override
    public String interpret() {
        StringBuilder selectBuilder = new StringBuilder()
                .append("context.select(DSL.field(")
                .append(toString(tableName + "." + id))
                .append(")\n.from(").append(toString(realTableName))
                .append(")");
        if(!realTableName.equals(tableName)){
            selectBuilder.append(".as(").append(toString(tableName)).append(")");
        }
        selectBuilder.append(")");
        String whereString = makeWhere();
        if (!whereString.isEmpty()) {
            selectBuilder.append(".where(").append(makeWhere()).append(")");
        }
        return selectBuilder.toString();
    }

    String makeWhere() {
        if (where != null && selectNext != null) {
            return "DSL.field(" + toString(tableName + "." + ref) + ")\n.in(" +
                    selectNext.interpret() + ")\n.and("+where.interpret()+")";
        }
        if (where != null) {
            return where.interpret();
        }
        if (selectNext != null) {
            return "DSL.field(" + toString(tableName + "." + ref) + ")\n.in(" +
                    selectNext.interpret() + ")";
        }
        return "";
    }

    String toString(String string) {
        return "\"" + string + "\"";
    }

    @Override
    public String getParams() {
        return null;
    }

    @Override
    protected BaseWhere<String,String> makeWhere(String request, String tableName, Endpoint parent) {
        return new StringWhere(request, tableName, parent);
    }
}
