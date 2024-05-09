package org.example.analize.premetive.filters;

import com.squareup.javapoet.CodeBlock;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filtering;

import java.util.List;
import java.util.Optional;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;

@Slf4j
public class CallPortFilter implements FilterInterpretation<CodeBlock> {
    CodeBlock result;
    String filterName;
    String example = "";
    String varName;
    String nameInRequest;


    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder().add(result).build();
    }

    public CallPortFilter(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public void makeFilter(Endpoint parent, CodeBlock def, String table) {
        def = Optional.ofNullable(def).orElse(CodeBlock.builder().add("$T.trueCondition()", DSL_CLASS).build());
        Filtering<CodeBlock> filtering = parent.getFilter(filterName);
        result = filtering.makeFilter(parent.getFuncName(), table, def);
        example = filtering.getExample();
        varName = filtering.getVarName();
        nameInRequest = Optional.ofNullable(filtering.getNameInRequest()).orElse(filterName);
    }

    @Override
    public void addParams(List<VarInfo> params, List<FilterInfo> filters) {
        filters.add(new FilterInfo(filterName, nameInRequest, example, varName));
    }
}
