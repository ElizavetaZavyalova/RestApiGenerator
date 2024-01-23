package org.example.analize.request.update.update;

import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.StringField;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.StringWhere;
import org.example.read_json.rest_controller_json.Endpoint;
import org.jooq.impl.DSL;

import java.util.List;

public abstract class StringUpdate extends BaseUpdate<String,String>{
    protected StringUpdate(String request, List<String> fields, PortRequestWithCondition<String, String> select, Endpoint parent) throws IllegalArgumentException {
        super(request, fields, select, parent);
    }

    @Override
    public String interpret() {
        StringBuilder selectBuilder = new StringBuilder()
                .append("DSL.update(DSL.table(")
                .append(toString(realTableName))
                .append(")");
        if(!realTableName.equals(tableName)){
            selectBuilder.append(".as(").append(toString(tableName)).append(")");
        }
        selectBuilder.append(")");
        selectBuilder.append(makeChooseFields());
        String whereString = makeWhere();
        if (!whereString.isEmpty()) {
            selectBuilder.append(".where(").append(makeWhere()).append(")");
        }
        return selectBuilder.toString();
    }
    String makeWhere() {
        if (where != null && selectNext != null) {
            return "DSL.field(" + toString(tableName + "." + ref) + ").\nin(" +
                    selectNext.interpret() + ")\n.and("+where.interpret()+")";
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
    protected String toString(String string) {
        return "\"" + string + "\"";
    }
    protected abstract String makeChooseFields();

    @Override
    public String getParams() {
        return null;
    }

    @Override
    protected BaseField<String> makeField(String name, String table, Endpoint parent) {
        return  new StringField(name,table,parent);
    }

    @Override
    protected BaseWhere<String,String> makeWhere(String request, String tableName, Endpoint parent) {
        return new StringWhere(request, tableName, parent);
    }
}
