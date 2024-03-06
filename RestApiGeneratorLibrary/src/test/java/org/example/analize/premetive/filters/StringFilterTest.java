package org.example.analize.premetive.filters;

import com.squareup.javapoet.CodeBlock;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.filter.filters_vies.Filter;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter.ListManyParamsFilter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Stream;

import static org.example.analize.premetive.filters.StringFilterTest.Filters.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.DSL_CLASS;
import static org.junit.jupiter.api.Assertions.assertThrows;
@Slf4j
public class StringFilterTest {
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }
    static final String realFieldName = "fieldName";
    static final String fieldName = "name";
    static final String tableName = "MyTable";
    static final String filterNameOr = "filterOr";
    static final String filterNameAnd = "filterAnd";
    static final String noFilter = "filterNo";
    static final List<String> listVal=List.of("i:eq_" + fieldName, "i:ne_" + fieldName,"s:like_" + fieldName);
    record Filters(){
        static final ListManyParamsFilter filterAnd=new ListManyParamsFilter(Filter.FilterNames.AND,listVal,filterNameAnd);
        static final ListManyParamsFilter filterOr=new ListManyParamsFilter(Filter.FilterNames.AND,listVal,filterNameOr);
    }

    Endpoint make(String name) throws IllegalArgumentException {
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(realFieldName).when(endpoint).getRealFieldName(fieldName);
        Mockito.doReturn(filterAnd).when(endpoint).getFilter(filterNameAnd);
        Mockito.doReturn(filterOr).when(endpoint).getFilter(filterNameOr);
        Mockito.doReturn("funcName").when(endpoint).getFuncName();
        Mockito.doThrow(new IllegalArgumentException("NO FILTER")).when(endpoint).getFilter(noFilter);
        return endpoint;
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsFilters")
    void ConstructorTestFilter(String filterName,CodeBlock cond) {
        log.info(filterName);
        CallPortFilter filter=new CallPortFilter(filterName);
        filter.makeFilter(make(filterName), CodeBlock.builder().add(cond).build(),tableName);
        log.info(filter.interpret().toString());

    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParamsFiltersThrow")
    void ConstructorTestThrowFilter(String filterName,CodeBlock cond) {
        log.info(filterName);
        CallPortFilter filter=new CallPortFilter(filterName);
        var ex = assertThrows(IllegalArgumentException.class, () -> filter.makeFilter(make(filterName), CodeBlock.builder().add(cond).build(),tableName));
        log.info(ex.getMessage());
    }
    static CodeBlock trueBlock= CodeBlock.builder().add("$T.trueCondition()",DSL_CLASS).build();
    static CodeBlock falseBlock= CodeBlock.builder().add("$T.trueCondition()",DSL_CLASS).build();
    static public Stream<Arguments> constructorParamsFiltersThrow() {
        return Stream.of(
                Arguments.of(noFilter,trueBlock),
                Arguments.of(noFilter,falseBlock));
    }

    static public Stream<Arguments> constructorParamsFilters() {
        return Stream.of(
                Arguments.of(filterNameOr,trueBlock),
                Arguments.of(filterNameAnd,trueBlock),
                Arguments.of(filterNameOr,falseBlock),
                Arguments.of(filterNameAnd,falseBlock));
    }
    static public Stream<Arguments> constructorParamsNoFilter() {
        return Stream.of(Arguments.of(noFilter));
    }

}
