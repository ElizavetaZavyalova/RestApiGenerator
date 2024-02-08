package org.example.read_json.rest_controller_json.filter.filters_vies;

@FunctionalInterface
public interface Filtering<R> {
    R makeFilter(Object... args);

}
