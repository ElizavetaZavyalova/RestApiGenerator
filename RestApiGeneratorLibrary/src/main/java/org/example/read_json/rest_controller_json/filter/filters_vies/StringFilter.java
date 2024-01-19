package org.example.read_json.rest_controller_json.filter.filters_vies;

import lombok.Getter;

@Getter
public abstract class StringFilter<Result> extends Filter<Result> {
    protected String val;
    protected StringFilter(FilterNames names,String val,String filter) {
        super(names,filter);
        this.val=val;
    }
}
