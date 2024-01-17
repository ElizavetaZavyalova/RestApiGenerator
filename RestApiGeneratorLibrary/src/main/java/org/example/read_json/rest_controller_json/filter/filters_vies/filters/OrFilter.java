package org.example.read_json.rest_controller_json.filter.filters_vies.filters;

import org.example.read_json.rest_controller_json.filter.filters_vies.ListFilter;

import java.util.List;

public class OrFilter extends ListFilter<String> {
    public OrFilter(List<String> val) {
        super(FilterNames.OR, val);
    }

    @Override
    public String makeFilter(Object...args) {
        return null;
    }
}
