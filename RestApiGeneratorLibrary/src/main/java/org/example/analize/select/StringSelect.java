package org.example.analize.select;

import org.example.analize.where.BaseWhere;
import org.example.analize.where.StringWhere;
import org.example.read_json.rest_controller_json.Endpoint;

public class StringSelect extends Select<String,String> {
    public StringSelect(String request, Select<String,String> select, Endpoint parent) {
        super(request, select, parent);
    }

    @Override
    public String interpret() {
        StringBuilder selectBuilder = new StringBuilder()
                .append("context.select(DSL.field(")
                .append(toString(tableName + "." + id))
                .append(").from(").append(toString(realTableName))
                .append(").as(").append(toString(tableName))
                .append(")");
        String whereString = makeWhere();
        if (!whereString.isEmpty()) {
            selectBuilder.append(".where(").append(makeWhere()).append(")");
        }
        return selectBuilder.toString();
    }

    String makeWhere() {
        if (where != null && selectNext != null) {
            return where.interpret() + "\n.and(DSL.field(" + toString(tableName + "." + ref) + ").\nin(" +
                    selectNext.interpret() + "))";
        }
        if (where != null) {
            return where.interpret();
        }
        if (selectNext != null) {
            return "DSL.field(" + toString(tableName + "." + ref) + ").\nin(" +
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
    BaseWhere<String,String> makeWhere(String request, String tableName, Endpoint parent) {
        return new StringWhere(request, tableName, parent);
    }
}
