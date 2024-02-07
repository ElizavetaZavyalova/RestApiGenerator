package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.info.VarInfo;
import org.example.read_json.rest_controller_json.filter.EndpointFilters;
import org.example.read_json.rest_controller_json.filter.Filters;

import org.example.read_json.rest_controller_json.filter.filters_vies.Filtering;
import org.example.read_json.rest_controller_json.filter.filters_vies.filters.list_filter.ListStringFilter;
import org.example.read_json.rest_controller_json.pseudonyms.EndpointPseudonyms;
import org.example.read_json.rest_controller_json.pseudonyms.Pseudonyms;

import javax.lang.model.element.Modifier;

import java.util.*;
import java.util.stream.Collectors;

import static org.example.file_code_gen.DefaultsVariablesName.Filter.*;
import static org.example.read_json.rest_controller_json.Endpoint.KeyWords.*;

@Slf4j
public class Endpoint {
    @Setter
    @Getter
    static class RequestInformation {
        String requestBd;
        String request;
        Type type;
        String call;
        InterpretDb interpretDb;
        List<VarInfo> varInfos;

        RequestInformation(Map<String, Object> enpointMap) {
            type = new Type(MakeCast.makeString(enpointMap, TYPES, true));
            request = MakeCast.makeString(enpointMap, REQUEST, true);
            requestBd = MakeCast.makeString(enpointMap, REQUEST_BD, false);
            if (requestBd.isEmpty()) {
                requestBd = request;
                request = null;
            } else if (requestBd.startsWith(CALL)) {
                call = requestBd.replace(CALL, "");
                requestBd = CALL;
            }
        }

        public void generateBd(Endpoint endpoint) {
            interpretDb = new InterpretDb(endpoint);
            varInfos = new ArrayList<>();
            interpretDb.getInterpretation().addParams(varInfos);
        }

        public MethodSpec makeDBMethod(String funcName) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(funcName)
                    .addModifiers(Modifier.PUBLIC);
            for (VarInfo parameterSpec : varInfos) {
                if (!parameterSpec.isFilter()) {
                    methodBuilder.addParameter(parameterSpec.getParameterSpec());
                }
            }
            methodBuilder.addParameter(ParameterizedTypeName.get(Map.class, String.class, Object.class), REQUEST_PARAM_MAP);
            return addCode(addReturns(methodBuilder)).build();
        }

        MethodSpec.Builder addCode(MethodSpec.Builder builder) {
            if (type.getRequestType().equals(RequestType.GET)) {
                builder.addCode("return ");
            }
            return builder.addCode(interpretDb.getInterpretation().interpret());
        }

        MethodSpec.Builder addReturns(MethodSpec.Builder builder) {
            if (type.getRequestType().equals(RequestType.GET)) {
                builder.returns(RESULT_OF_RECORD_CLASS);
            }
            return builder;
        }


    }

    @Getter
    RequestInformation requestInformation;
    Boolean perms = false;
    @Getter
    String funcName;
    String swagger;
    Filters filters;
    Pseudonyms pseudonyms;
    @Getter
    Endpoints parent;

    Endpoint(Map<String, Object> enpointMap, Endpoints parent, String funcName) {
        try {
            this.funcName = funcName;
            this.parent = parent;
            requestInformation = new RequestInformation(enpointMap);
            perms = MakeCast.makeBoolean(enpointMap, PERMS, false);
            swagger = MakeCast.makeString(enpointMap, SWAGGER, true);
            filters = new EndpointFilters(MakeCast.makeStringMap(enpointMap, FILTERS, false), this);
            pseudonyms = new EndpointPseudonyms(MakeCast.makeMapOfMapOfList(enpointMap, PSEUDONYMS, false), this);
        } catch (IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            //TODO stop compile
        }
    }

    public List<MethodSpec> getDBMethods() throws IllegalArgumentException{
        this.requestInformation.generateBd(this);
        List<MethodSpec> list = new ArrayList<>();
        List<String> useFilters = requestInformation.getVarInfos().stream()
                .filter(VarInfo::isFilter)
                .map(VarInfo::getName).distinct().toList();
        for (String useFilter : useFilters) {
            Filtering<String> filtering = getFilter(useFilter);
            if (filtering instanceof ListStringFilter) {
                ListStringFilter filter = (ListStringFilter) filtering;
                list.add(filter.makeFilterMethod(this));
            }
        }
        list.add(requestInformation.makeDBMethod(funcName));
        return list;
    }


    public Filtering<String> getFilter(String key) throws IllegalArgumentException {
        if (filters.isFilterExist(key)) {
            return filters.getFilterIfExist(key);
        }
        return parent.getParent().getFilters().getFilterIfExist(key);
    }

    public String getRealTableName(String key) {
        if (pseudonyms.isContainsTablePseudonym(key)) {
            return pseudonyms.getRealTableName(key);
        }
        if (parent.getParent().getPseudonyms().isContainsTablePseudonym(key)) {
            return parent.getParent().getPseudonyms().getRealTableName(key);
        }
        return key;
    }

    public String getRealFieldName(String key) {
        if (pseudonyms.isContainsFieldPseudonym(key)) {
            return pseudonyms.getRealFieldName(key);
        }
        if (parent.getParent().getPseudonyms().isContainsFieldPseudonym(key)) {
            return parent.getParent().getPseudonyms().getRealFieldName(key);
        }
        return key;
    }

    public List<String> getRealJoinName(String t1, String t2) {
        String key = t1 + ":" + t2;
        if (pseudonyms.isContainsJoinPseudonym(key)) {
            return pseudonyms.getRealJoinsName(key);
        }
        if (parent.getParent().getPseudonyms().isContainsJoinPseudonym(key)) {
            return parent.getParent().getPseudonyms().getRealJoinsName(key);
        }
        return List.of();
    }

    record KeyWords() {
        static String REQUEST = "request";
        static String PERMS = "perms";
        static String TYPES = "types";
        static String REQUEST_BD = "request_bd";
        static String CALL = "call:";
        static String FILTERS = "filters";
        static String PSEUDONYMS = "pseudonyms";


        record Regexp() {
            static String REQUEST_TYPE_SEPARATOR = "\\|";
        }

        static String SWAGGER = "swagger";
    }

}
