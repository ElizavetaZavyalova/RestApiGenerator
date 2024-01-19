package org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import org.example.analize.premetive.filters.StringFilterField;
import org.example.read_json.rest_controller_json.Endpoint;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.example.file_code_gen.DefaultsVariablesName.Filter.*;


public class ListStringFilter extends ListFilter<String> {
    String func;

    public ListStringFilter(FilterNames name, List<String> val, String filter) {
        super(name, val, filter);
        if (Objects.requireNonNull(name) == FilterNames.OR) {
            func = "or";
        } else if (name == FilterNames.AND) {
            func = "and";
        }
    }
    MethodSpec makeFilterMethod(Endpoint parent)throws IllegalArgumentException {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("myMethod")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(Map.class, String.class, Object.class), REQUEST_PARAM_MAP_IN_FILTER)
                .addParameter(TypeName.get(String.class), TABLE_NAME_IN_FILTER)
                .addParameter(CONDITION_CLASS, DEFAULT_CONDITION_IN_FILTER)
                .returns(CONDITION_CLASS)
                .addStatement("$T<Condition> "+CONDITION_LIST_IN_FILTER+"=new $T<>()", List.class, ArrayList.class);
        val.forEach(v -> methodBuilder.addCode(new StringFilterField(v, parent).interpret()));
        methodBuilder.addStatement("return "+CONDITION_LIST_IN_FILTER+".stream().reduce(Condition::" + func + ")\n" +
                ".ofNullable("+DEFAULT_CONDITION_IN_FILTER+").get()");
        return methodBuilder.build();
    }

    @Override
    public String makeFilter(Object... args) {
        return filter + "("+REQUEST_PARAM_MAP_IN_FILTER+"," + (String) args[0] + "," + (String) args[1] + ")";
    }
}
