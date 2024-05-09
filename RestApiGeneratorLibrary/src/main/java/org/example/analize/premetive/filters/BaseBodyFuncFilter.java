package org.example.analize.premetive.filters;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.BaseFieldParser;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.LONG_CLASS;

public abstract class BaseBodyFuncFilter extends BaseFieldParser<CodeBlock> {

    protected BaseBodyFuncFilter(String variable) throws IllegalArgumentException {
        super(variable);
    }

    protected BaseBodyFuncFilter(String variable, Endpoint parent) throws IllegalArgumentException {
        super(variable, parent);
    }

    public ClassName makeType() {
        return switch (type) {
            case STRING -> STRING_CLASS;
            case BOOLEAN -> BOOLEAN_CLASS;
            case INTEGER -> INTEGER_CLASS;
            case FLOAT -> FLOAT_CLASS;
            case DOUBLE -> DOUBLE_CLASS;
            default -> LONG_CLASS;
        };
    }

    public abstract String defaultValue();


    @Override
    public void addParams(List<VarInfo> params, List<FilterInfo> filters) {
        //not in request
    }
}
