package org.example.read_json.rest_controller_json.filter.filters_vies.filters;

import com.squareup.javapoet.CodeBlock;
import org.example.read_json.rest_controller_json.filter.filters_vies.StringFilter;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames.SQL;

public class SqlFilter extends StringFilter<CodeBlock> {
    public SqlFilter(String val,String filter) {
        super(SQL, val,filter);
    }

    @Override
    public CodeBlock makeFilter(Object...args) {
        return CodeBlock.builder().add("$T.sql($S)",DSL_CLASS,val).build();
    }
}
