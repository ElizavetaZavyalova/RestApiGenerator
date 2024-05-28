package org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

import java.util.List;

public abstract class ListFilter extends BaseListFilter<CodeBlock, MethodSpec> {
    protected ListFilter(FilterNames names, String key, List<String> val, String filter, String nameInRequest) {
        super(names, key, val, filter, nameInRequest);
    }
    String getParam() {
        return filterName;
    }
}
