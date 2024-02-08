package org.example.analize.premetive.filters;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;
import java.util.Optional;

public class StringFilter implements Interpretation<CodeBlock>, FilterCreation<String> {
    String result;
    String filterName;


    @Override
    public CodeBlock interpret() {
        return CodeBlock.builder().add(result).build();
    }

    public StringFilter(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public void makeFilter(Endpoint parent, String def, String table) {
        def = Optional.ofNullable(def).orElse("DSL.trueCondition()");
        result = parent.getFilter(filterName).makeFilter(parent.getFuncName(),table, def);
    }

    @Override
    public String requestInterpret() {
        return filterName;
    }

    @Override
    public void addParams(List<VarInfo> params) {
        params.add(new VarInfo(filterName));
    }
}
