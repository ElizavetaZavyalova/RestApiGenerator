package org.example.read_json.rest_controller_json.filter.filters_vies;


public interface Filtering<R> {
    R makeFilter(Object... args);

    String getExample();

    String getVarName();

    String getNameInRequest();

}
