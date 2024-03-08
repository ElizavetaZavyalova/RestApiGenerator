package org.example.analize.request.get.select;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.Field;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.Select;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.port_request.PortRequest;
import org.example.analize.select.port_request.WereInterpret;
import org.example.analize.where.BaseWhere;
import org.example.analize.where.Where;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.CONTEXT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.FIELD_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_NAME;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.FIELDS;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.Ports.LIMIT;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.Ports.OFFSET;


public class Get extends BaseGet<CodeBlock, MethodSpec.Builder> {
    public Get(String request, PortRequestWithCondition<CodeBlock> select, List<String> fields, Endpoint parent) {
        super(request, select, fields, parent);
    }
    @Override
    protected PortRequestWithCondition<CodeBlock> makePortRequest(String tableName, PortRequestWithCondition<CodeBlock> select, Endpoint parent, boolean isPathFound) {
        return new PortRequest(tableName, select, parent, isPathFound);
    }

    @Override
    protected BaseField<CodeBlock> makeField(String name, String table, Endpoint parent) {
        return  new Field(name,table,parent);
    }

    @Override
    public CodeBlock interpret() {
        var block= CodeBlock.builder();
                block.add(CONTEXT+".select(")
                .add(isFieldsExist()?LIST_NAME_DEFAULT:"")
                .add(").from($T.table($S)",DSL_CLASS, realTableName);
        if (!realTableName.equals(tableName)) {
            block.add(".as($S)", tableName);
        }
        block.add(")");
        block.add(WereInterpret.makeWhere(where,selectNext,tableName,ref));
        return block.build();
    }
    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Select(request,select,parent);
    }
    CodeBlock makeFieldsName(){
        if(!isFieldsExist()){
            return CodeBlock.builder().build();
        }
        return fields.stream().map(BaseField::makeNameInfo)
                .reduce((v,h)-> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(fields.get(0).interpret()).toBuilder().build();
    }
   @Override
    public MethodSpec.Builder createFieldsPort(MethodSpec.Builder method){
        if(isFieldsExist()) {
            CodeBlock defaultMapBlock = CodeBlock.builder().add("$T<$T,$T>", MAP_CLASS, STRING_CLASS, STRING_CLASS).add(FIELDS_NAME).add("=$T.of(", MAP_CLASS).add(makeFieldsName()).add(")").build();
            method.addStatement(defaultMapBlock);
            CodeBlock defaultListBlock = CodeBlock.builder().add("$T<$T<$T>>", LIST_CLASS, FIELD_CLASS, OBJECT_CLASS).add(LIST_NAME_DEFAULT).add("=$T.of(", LIST_CLASS).add(makeFields()).add(")").build();
            method.addStatement(defaultListBlock);
            method.beginControlFlow("if(" + REQUEST_PARAM_NAME + ".containsKey($S))", FIELDS);
            CodeBlock createPort = CodeBlock.builder().add("$T<$T<$T>>", LIST_CLASS, FIELD_CLASS, OBJECT_CLASS).add(LIST_NAME).add("=").add(REQUEST_PARAM_NAME).add(".get($S).parallelStream()", FIELDS).
                    add(".filter(f->").add(FIELDS_NAME).add(".containsKey(f))").add(".map(f->").
                    add("$T.field($S+",DSL_CLASS,tableName+".").add(FIELDS_NAME).add(".get(f)).as(f)).toList()").build();
            method.addStatement(createPort);
            method.beginControlFlow("if(!" + LIST_NAME+ ".isEmpty())");
            method.addStatement(LIST_NAME_DEFAULT+"="+LIST_NAME);
            method.endControlFlow();
            method.endControlFlow();
        }
        return method;
    }




    CodeBlock makeFields(){
        if(!isFieldsExist()){
            return CodeBlock.builder().build();
        }
        return fields.stream().map(InterpretationBd::interpret)
                .reduce((v,h)-> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(fields.get(0).interpret()).toBuilder().build();
    }



    @Override
    protected BaseWhere<CodeBlock> makeWhere(String request, String tableName, Endpoint parent) {
        return new Where(request, tableName, parent);
    }

    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        if (isSelectExist()) {
            selectNext.addParams(params,filters);
        }
        if (isWhereExist()) {
            where.addParams(params,filters);
        }
    }
}
