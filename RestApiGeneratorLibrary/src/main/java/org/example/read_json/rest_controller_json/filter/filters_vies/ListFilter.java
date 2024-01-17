package org.example.read_json.rest_controller_json.filter.filters_vies;

import lombok.Getter;

import java.util.List;
@Getter
public abstract class ListFilter<Result> extends Filter<Result>{
    protected List<String> val;
    protected ListFilter(FilterNames names,List<String> val) {
        super(names);
        this.val=val;
    }
}
