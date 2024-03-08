package org.example.analize.request.get;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.Address;

import org.example.analize.request.LoggerCreator;
import org.example.analize.request.get.select.BaseGet;
import org.example.analize.request.get.select.Get;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.RequestType;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.impl.DSL;

import java.util.List;
import java.util.Map;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RESULT_NAME;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.SORT_FIELD_CLASS;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.Ports.*;

public class GetRequest extends BaseGetRequest<CodeBlock,MethodSpec.Builder, TypeName>{
    public GetRequest(String url, List<String> fields, Endpoint parent,boolean isPorts,boolean isSort) throws IllegalArgumentException {
        super(url, fields, parent,isPorts,isSort);
    }
    @Override
    protected BaseAddress<CodeBlock> make(String url, Endpoint parent) {
        return new Address(url,parent);
    }

    @Override
    protected BaseGet<CodeBlock,MethodSpec.Builder> makeSelect(String request, List<String> fields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Get(request,select,fields,parent);
    }

    @Override
    public MethodSpec.Builder makeMethodBody(MethodSpec.Builder method) {
        method=select.createFieldsPort(method);
        CodeBlock.Builder result=CodeBlock.builder().add(select.interpret());
        if(isSort){
            result.add(".orderBy(").add(DIRECTION).add("?")
                    .add("$T.field(",DSL_CLASS).add(SORT).add(").desc():")
                    .add("$T.field(",DSL_CLASS).add(SORT).add(").asc()")
                    .add(")");
        }
        if(isPorts){
            result.add(".limit(").add(OFFSET).add(", ").add(LIMIT).add(")");
        }
        method.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + " = ")
                .add(result.build()).build());
        method= LoggerCreator.createLog(method,RequestType.GET,RESULT_NAME);
        return  method.addStatement("return " + RESULT_NAME+".fetch().intoMaps()");
    }

    @Override
    public TypeName returnParam() {
        return PARAMETERIZED_LIST;
    }
}
