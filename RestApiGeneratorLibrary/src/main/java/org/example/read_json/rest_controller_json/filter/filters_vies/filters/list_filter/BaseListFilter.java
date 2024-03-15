package org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter;

import lombok.Getter;

import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filter;

import java.util.List;
import java.util.Optional;


public abstract class BaseListFilter<R,M> extends Filter<R> {
    @Getter
    protected List<String> val;
    protected String example;
    protected BaseListFilter(FilterNames names,String key, List<String> val, String filter, String nameInRequest) {
        super(names,filter,nameInRequest,key);
        this.val=val;
    }
    public abstract M makeFilterMethod(Endpoint parent);

    public String getExample(){
        return Optional.ofNullable(example).orElse(createExample());
    }


    protected abstract String createExample();
}
