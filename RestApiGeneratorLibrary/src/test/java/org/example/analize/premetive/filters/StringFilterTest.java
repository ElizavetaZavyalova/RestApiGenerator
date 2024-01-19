package org.example.analize.premetive.filters;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.fields.StringField;
import org.example.read_json.rest_controller_json.Endpoint;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filter;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.SqlFilter;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter.ListStringFilter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Stream;

import static org.example.analize.premetive.filters.StringFilterTest.Filters.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
@Slf4j
public class StringFilterTest {
    static final String realFieldName = "fieldName";
    static final String fieldName = "name";
    static final String tableName = "MyTable";
    static final String filterNameOr = "filterOr";
    static final String filterNameAnd = "filterAnd";
    static final String filterNameSql = "filterSql";
    static final String noFilter = "filterNo";
    static final List<String> listVal=List.of("i:eq_" + fieldName, "i:ne_" + fieldName,"s:like_" + fieldName);
    record Filters(){
        static final ListStringFilter filterAnd=new ListStringFilter(Filter.FilterNames.AND,listVal,filterNameAnd);
        static final ListStringFilter filterOr=new ListStringFilter(Filter.FilterNames.AND,listVal,filterNameOr);
        static final SqlFilter filterSQL=new SqlFilter("(name=9 or age=7)",filterNameSql);
    }

    Endpoint make(String name) throws IllegalArgumentException {
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(realFieldName).when(endpoint).getRealFieldName(fieldName);
        Mockito.doReturn(filterAnd).when(endpoint).getFilter(filterNameAnd);
        Mockito.doReturn(filterOr).when(endpoint).getFilter(filterNameOr);
        Mockito.doReturn(filterSQL).when(endpoint).getFilter(filterNameSql);
        Mockito.doThrow(new IllegalArgumentException("NO FILTER")).when(endpoint).getFilter(noFilter);
        //Mockito.doReturn(new IllegalArgumentException("NO FILTER")).when(endpoint).getFilter(noFilter);
        return endpoint;
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsFilters")
    void ConstructorTestFilter(String filterName,String cond) {
        log.info(filterName);
        StringFilter filter=new StringFilter(filterName);
        filter.makeFilter(make(filterName),cond,tableName);
        log.info(filter.interpret());

    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsFiltersThrow")
    void ConstructorTestThrowFilter(String filterName,String cond) {
        log.info(filterName);
        StringFilter filter=new StringFilter(filterName);
        var ex = assertThrows(IllegalArgumentException.class, () -> filter.makeFilter(make(filterName),cond,tableName));
        log.info(ex.getMessage());
    }
    static public Stream<Arguments> constructorParamsFiltersThrow() {
        return Stream.of(
                Arguments.of(noFilter,"DSL.trueCondition()"),
                Arguments.of(noFilter,"DSL.falseCondition()"));
    }

    static public Stream<Arguments> constructorParamsFilters() {
        return Stream.of(
                Arguments.of(filterNameOr,"DSL.trueCondition()"),
                Arguments.of(filterNameAnd,"DSL.trueCondition()"),
                Arguments.of(filterNameSql,"DSL.trueCondition()"),
                Arguments.of(filterNameOr,"DSL.falseCondition()"),
                Arguments.of(filterNameAnd,"DSL.falseCondition()"),
                Arguments.of(filterNameSql,"DSL.falseCondition()"));
    }
    static public Stream<Arguments> constructorParamsNoFilter() {
        return Stream.of(Arguments.of(noFilter));
    }

}
