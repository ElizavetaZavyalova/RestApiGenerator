package org.example.read_json.rest_controller_json.filter.filters_vies.filters;

import org.example.read_json.rest_controller_json.filter.filters_vies.StringFilter;

import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames.CALL;

public class CallFilter extends StringFilter<String> {
    public CallFilter(String val) {
        super(CALL, val);
    }

    @Override
    public String makeFilter(Object...args) {
        return val;
    }
}
