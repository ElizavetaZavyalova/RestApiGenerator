package org.example.read_json.rest_controller_json.filter.filters_vies.filters;

import com.squareup.javapoet.CodeBlock;
import org.example.read_json.rest_controller_json.filter.filters_vies.StringFilter;

import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames.CALL;

public class CallFilter extends StringFilter<CodeBlock> {
    public CallFilter(String val,String filter) {
        super(CALL, val,filter);
    }

    @Override
    public CodeBlock makeFilter(Object...args) {
        return CodeBlock.builder().add(val).build();
    }
}
