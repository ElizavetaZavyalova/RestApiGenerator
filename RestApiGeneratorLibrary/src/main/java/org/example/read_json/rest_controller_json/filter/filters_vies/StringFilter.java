package org.example.read_json.rest_controller_json.filter.filters_vies;

import lombok.Getter;

@Getter
public abstract class StringFilter<R> extends Filter<R> {
    protected String val;

    protected StringFilter(FilterNames names, String key, String val, String filter, String nameInRequest) {
        super(names, filter, nameInRequest, key);
        this.val = val;
    }
}
