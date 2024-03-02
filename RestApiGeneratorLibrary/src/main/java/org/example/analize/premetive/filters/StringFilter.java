package org.example.analize.premetive.filters;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;
import java.util.Optional;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

public class StringFilter implements FilterInterpretation<CodeBlock> {
    CodeBlock result;
    String filterName;


    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder().add(result).build();
    }

    public StringFilter(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public void makeFilter(Endpoint parent, CodeBlock def, String table) {
        def = Optional.ofNullable(def).orElse(CodeBlock.builder().add("$T.trueCondition()",DSL_CLASS).build());
        result = parent.getFilter(filterName).makeFilter(parent.getFuncName(),table, def);
    }
    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        filters.add(new FilterInfo(filterName));
    }
}
