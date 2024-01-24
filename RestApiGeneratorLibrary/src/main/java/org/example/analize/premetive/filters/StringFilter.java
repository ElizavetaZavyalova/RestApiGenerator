package org.example.analize.premetive.filters;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.premetive.filters.FilterCreation;
import org.example.read_json.rest_controller_json.Endpoint;
import org.jooq.Block;

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
        def = (def == null ? "DSL.trueCondition()" : def);
        result = parent.getFilter(filterName).makeFilter(table, def);
    }

    @Override
    public String requestInterpret() {
        return filterName;
    }

    @Override
    public String getParams() {
        return null;
    }
}
