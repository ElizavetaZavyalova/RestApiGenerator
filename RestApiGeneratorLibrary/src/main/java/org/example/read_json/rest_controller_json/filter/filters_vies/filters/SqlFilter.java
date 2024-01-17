package org.example.read_json.rest_controller_json.filter.filters_vies.filters;

import org.example.read_json.rest_controller_json.filter.filters_vies.StringFilter;

import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames.SQL;

public class SqlFilter extends StringFilter<String> {
    public SqlFilter(String val) {
        super(SQL, val);
    }

    @Override
    public String makeFilter(Object...args) {
        return "\""+val+"\"";
    }
}
