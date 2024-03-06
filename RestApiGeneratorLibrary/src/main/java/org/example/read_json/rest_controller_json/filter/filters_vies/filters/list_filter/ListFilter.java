package org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter;

import lombok.Getter;
import org.example.analize.premetive.filters.BaseBodyFuncFilter;
import org.example.analize.premetive.filters.BodyFuncFilterManyParams;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ListFilter<R> extends Filter<R> {
    @Getter
    protected List<String> val;
    protected String example;
    protected ListFilter(FilterNames names,List<String> val,String filter) {
        super(names,filter);
        this.val=val;
    }

    public String getExample(){
        return Optional.ofNullable(example).orElse(createExample());
    }


    protected abstract String createExample();
}
