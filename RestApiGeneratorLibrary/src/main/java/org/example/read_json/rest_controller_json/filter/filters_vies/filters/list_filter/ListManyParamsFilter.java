package org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

import org.example.analize.premetive.filters.BaseBodyFuncFilter;
import org.example.analize.premetive.filters.BodyFuncFilterManyParams;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import javax.lang.model.element.Modifier;

import java.util.List;

import java.util.Objects;
import java.util.stream.Collectors;


import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.CONDITION_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.*;


public class ListManyParamsFilter extends ListFilter {
    String func;
    boolean isNot;

    public ListManyParamsFilter(FilterNames name, String key,List<String> val, String filter,String nameInRequest) {
        super(name,key, val, filter,nameInRequest);
        if (FilterNames.isOr(Objects.requireNonNull(name))) {
            func = "or";
        } else if (FilterNames.isAnd(name)) {
            func = "and";
        }
        isNot=FilterNames.isNot(name);
    }

    public MethodSpec makeFilterMethod(Endpoint parent) throws IllegalArgumentException {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getFuncName(parent.getFuncName()))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(REQUEST_PARAMS, REQUEST_PARAM_NAME)
                .addParameter(STRING_CLASS, TABLE_NAME_IN_FILTER)
                .addParameter(CONDITION_CLASS, DEFAULT_CONDITION_IN_FILTER)
                .returns(CONDITION_CLASS)
                .addStatement("$T<$T> " + CONDITION_LIST_IN_FILTER + "=new $T<>()", LIST_CLASS, CONDITION_CLASS, ARRAY_LIST_CLASS);
        val.forEach(v -> methodBuilder.addCode(new BodyFuncFilterManyParams(v, parent).interpret()));
        methodBuilder.addStatement(makeCondition());
        return methodBuilder.build();
    }
    CodeBlock makeCondition(){
        CodeBlock.Builder block= CodeBlock.builder();
        block.add(CONDITION_LIST_IN_FILTER + ".stream().reduce($T::" + func + ")\n" +
                ".ofNullable(" + DEFAULT_CONDITION_IN_FILTER + ").get()", CONDITION_CLASS);
        if(isNot){
            return CodeBlock.builder().add("return $T.not(",DSL_CLASS).add(block.build()).add(")").build();
        }
        return CodeBlock.builder().add("return ").add(block.build()).build();
    }

    String getFuncName(String funcName) {
        return filterName + "Of" + (funcName).substring(0, 1).toUpperCase() + (funcName).substring(1);
    }
    @Override
    protected String createExample(){
        example="{"+val.stream().map(BodyFuncFilterManyParams::new).map(BaseBodyFuncFilter::defaultValue)
                .collect(Collectors.joining(", "))+"}";
        return example;
    }

    @Override
    public CodeBlock makeFilter(Object... args) {
        return CodeBlock.builder().add(getFuncName((String) args[0]))
                .add("(" + filterName + ", $S, ",  args[1])
                .add((CodeBlock) args[2]).add(")").build();
    }
}
