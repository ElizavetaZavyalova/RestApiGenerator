package org.example.analize.request.get.select;

import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.StringField;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.StringWhere;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

public class StringGetSelect extends GetBaseSelect<String,String>{
    public StringGetSelect(String request, PortRequestWithCondition<String, String> select, List<String> fields, Endpoint parent) {
        super(request, select, fields, parent);
    }

    @Override
    protected BaseField<String> makeField(String name, String table, Endpoint parent) {
        return  new StringField(name,table,parent);
    }

    @Override
    public String interpret() {
        StringBuilder selectBuilder = new StringBuilder()
                .append("context.select(")
                .append(makeFields())
                .append(").from(").append(toString(realTableName))
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
    String toString(String string) {
        return "\"" + string + "\"";
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
    String makeFields(){
        if(fields==null||fields.isEmpty()){
            return "";
        }
        return fields.stream().map(InterpretationBd::interpret).collect(Collectors.joining(", "));
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
