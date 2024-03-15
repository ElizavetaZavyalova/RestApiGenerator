package org.example.analize.request.post;

import com.squareup.javapoet.*;
import org.example.analize.address.BaseAddress;
import org.example.analize.address.Address;
import org.example.analize.request.LoggerCreator;
import org.example.analize.request.post.insert.BaseInsert;
import org.example.analize.request.post.insert.Insert;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.RequestType;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public class PostRequest extends BasePostRequest<CodeBlock, MethodSpec.Builder,TypeName,ClassName> {


    public PostRequest(String url, List<String> params,List<String> returnParams, Endpoint parent) throws IllegalArgumentException {
        super(url, params,returnParams, parent);
    }
    @Override
    protected BaseAddress<CodeBlock> make(String url, Endpoint parent) {
        return new Address(url,parent);
    }

    @Override
    BaseInsert<CodeBlock,ClassName,List<CodeBlock>,MethodSpec.Builder> makeBaseInsertRequest(String request, List<String> fields,List<String> returnFields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Insert(request,fields,returnFields,select,parent);
    }

    @Override
    public MethodSpec.Builder makeMethodBody(MethodSpec.Builder method) {
        List<CodeBlock> inserts=insert.interpret();
        method.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + "1 = ")
                .add(inserts.get(0)).build());
        method= LoggerCreator.createLog(method,RequestType.POST,RESULT_NAME+"1");
        if(!insert.isReturn()) {
            return method.addStatement(RESULT_NAME + "1.execute()");
        }
        else if(insert.isFetchOne()){
            method.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + " = ")
                    .add(RESULT_NAME + "1").add(".fetchOne()").build());
        }
        else if(insert.isFetch()){
            method.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + " = ")
                    .add(RESULT_NAME + "1").add(".fetch()").build());
        }
        if(insert.isAlwaysReturn()){
            method.addStatement(CodeBlock.builder().add("var " +insert.getFieldNameChose() + " = ")
                    .add(RESULT_NAME)
                    .add(".getValue($T.field($S))",DSL_CLASS,insert.getFieldNameChose()).build());
        }
        if(inserts.size()==2) {
            method.addStatement(CodeBlock.builder().add("var " + RESULT_NAME + "2 = ")
                    .add(inserts.get(1)).build());
            method = LoggerCreator.createLog(method, RequestType.POST, RESULT_NAME + "2");
            method.addStatement(RESULT_NAME + "2.execute()");
        }
        return insert.addReturn(method);
    }

    @Override
    public TypeName returnParam() {
        if(insert.isExecute()){
            return TypeName.VOID;
        }
        if(insert.isFetchOne()){
            return  PARAMETRIZED_MAP;
        }
        return PARAMETERIZED_LIST;

    }
}
