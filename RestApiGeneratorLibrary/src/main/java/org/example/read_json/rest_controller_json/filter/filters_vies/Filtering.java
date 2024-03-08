package org.example.read_json.rest_controller_json.filter.filters_vies;


public interface Filtering<R> {
    R makeFilter(Object... args);
    public String getExample();
    public String getVarName();
    public String getNameInRequest();

}
