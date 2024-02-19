package org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter;

import lombok.Getter;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filter;

import java.util.List;
@Getter
public abstract class ListFilter<R> extends Filter<R> {
    protected List<String> val;
    protected ListFilter(FilterNames names,List<String> val,String filter) {
        super(names,filter);
        this.val=val;
    }
}
