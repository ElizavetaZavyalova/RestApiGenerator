package org.example.read_json.rest_controller_json.filter.filters_vies.filters;

import org.example.read_json.rest_controller_json.filter.filters_vies.ListFilter;

import java.util.List;

public class AndFilter extends ListFilter<String> {
    public AndFilter(List<String> val) {
        super(FilterNames.AND, val);
    }

    @Override
    public String makeFilter(Object...args) {
        return null;
    }
}
