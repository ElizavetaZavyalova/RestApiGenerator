package org.example.read_json.rest_controller_json.filter.filters_vies;
@FunctionalInterface
public interface Filtering<Result> {
    Result makeFilter(Object...args);
}
