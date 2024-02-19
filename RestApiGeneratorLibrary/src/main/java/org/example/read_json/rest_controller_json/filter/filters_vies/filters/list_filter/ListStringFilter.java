package org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

import com.squareup.javapoet.TypeName;
import org.example.analize.premetive.filters.StringFilterField;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import javax.lang.model.element.Modifier;

import java.util.List;

import java.util.Objects;


import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.CONDITION_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.*;


public class ListStringFilter extends ListFilter<CodeBlock> {
    String func;

    public ListStringFilter(FilterNames name, List<String> val, String filter) {
        super(name, val, filter);
        if (Objects.requireNonNull(name) == FilterNames.OR) {
            func = "or";
        } else if (name == FilterNames.AND) {
            func = "and";
        }
    }

    public MethodSpec makeFilterMethod(Endpoint parent) throws IllegalArgumentException {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getFuncName(parent.getFuncName()))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(REQUEST_PARAMS, REQUEST_PARAM_NAME)
                .addParameter(STRING_CLASS, TABLE_NAME_IN_FILTER)
                .addParameter(CONDITION_CLASS, DEFAULT_CONDITION_IN_FILTER)
                .returns(CONDITION_CLASS)
                .addStatement("$T<$T> " + CONDITION_LIST_IN_FILTER + "=new $T<>()", LIST_CLASS, CONDITION_CLASS, ARRAY_LIST_CLASS);
        val.forEach(v -> methodBuilder.addCode(new StringFilterField(v, parent).interpret()));
        methodBuilder.addStatement("return " + CONDITION_LIST_IN_FILTER + ".stream().reduce($T::" + func + ")\n" +
                ".ofNullable(" + DEFAULT_CONDITION_IN_FILTER + ").get()", CONDITION_CLASS);
        return methodBuilder.build();
    }

    String getFuncName(String funcName) {
        return filterName + "Of" + (funcName).substring(0, 1).toUpperCase() + (funcName).substring(1);
    }

    @Override
    public CodeBlock makeFilter(Object... args) {
        return CodeBlock.builder().add(getFuncName((String) args[0]))
                .add("(" + REQUEST_PARAM_NAME + ", $S, ",  args[1])
                .add((CodeBlock) args[2]).add(")").build();
    }
}
