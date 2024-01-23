package org.example.analize.request.post.insert;

import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.interpretation.InterpretationParams;
import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.StringField;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_MAP;

public class StringInsertRequest extends BaseInsertRequest<String,String>{

    public StringInsertRequest(String request, List<String> fields, PortRequestWithCondition<String, String> select, Endpoint parent) {
        super(request, fields,select, parent);

    }


    @Override
    protected BaseField<String> makeField(String name, String table, Endpoint parent) {
        return  new StringField(name,table,parent);
    }

    @Override
    public String interpret() {
       return  "context.insertInto(" + makeInsert() + ")" + makeValues();
    }
    String makeValues(){
        if (selectNext != null) {
            StringBuilder builder=new StringBuilder("DSL.select(\n");
            builder.append("DSL.field(").append(toString(tableName + "." + id)).append(")");
            if(!fields.isEmpty()) {
                 builder.append(",\n ").append(values());

            }
            builder.append(")\n.from(").append(selectNext.interpret()).append(")");
            return builder.toString();
        }
        if(!fields.isEmpty()) {
            StringBuilder builder = new StringBuilder("\n.values(").append(values()).append(")");
            return builder.toString();
        }
        return ".defaultValues()";
    }
    String values(){
    return  fields.stream().map(BaseField::getName)
                .map(name -> "DSL.val(Optional.ofNullable("+REQUEST_PARAM_MAP+".get(" + toString(name) + "))\n.orElse(DSL.defaultValue()))")
            .collect(Collectors.joining(",\n "));
    }
    String makeInsert(){
        StringBuilder insertBuilder = new StringBuilder("DSL.table(").append(toString(realTableName)).append(")");
        if(!realTableName.equals(tableName)){
            insertBuilder.append(".as(").append(toString(tableName)).append(")");
        }
        if (selectNext != null) {
            insertBuilder.append(", DSL.field(").append(toString(tableName + "." + ref)).append(")");
        }
        if(!fields.isEmpty()){
            insertBuilder.append(", ").append(makeFields());
        }

        return insertBuilder.toString();
    }

    String makeFields(){
        return fields.stream().map(InterpretationParams::getParams).collect(Collectors.joining(", "));
    }

    String toString(String string) {
        return "\"" + string + "\"";
    }


    @Override
    public String requestInterpret() {
        return null;
    }

    @Override
    public String getParams() {
        return null;
    }
}
