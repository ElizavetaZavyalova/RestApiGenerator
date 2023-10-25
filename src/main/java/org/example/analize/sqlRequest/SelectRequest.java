package org.example.analize.sqlRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.Variables;
import org.example.analize.address.FullAddressSelect;
import org.example.analize.connections.ConditionInterpret;
import org.example.analize.connections.WhenCondition;
import org.jooq.*;
import org.jooq.impl.DSL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SelectRequest implements SqlInterpret {
    ConditionInterpret conditionInterpret = null;
    FullAddressSelect fullAddressSelect = null;
    String limit = null;
    String groupBy = null;

    String idNext = null;
    String tableName = null;
    List<String> fields = new ArrayList<>();

    public Select makeSelect(DSLContext dsl) {
        Condition condition = getCondition(dsl);
        SelectFieldOrAsterisk[] fieldOrAsterisks = makeFieldsId();
        if (condition != null && fieldOrAsterisks.length != 0) {
            return dsl.select(fieldOrAsterisks).from(tableName).
                    where(condition);
        } else if (fieldOrAsterisks.length != 0) {
            return dsl.select().from(tableName);
        } else if (condition != null) {
            return dsl.select().from(tableName).where(condition);
        }
        return dsl.select().from(tableName);
    }

    Condition getCondition(DSLContext dsl) {
        if (fullAddressSelect != null && conditionInterpret != null) {
            return DSL.field(idNext).in(fullAddressSelect.makeSelect(dsl)).and(conditionInterpret.makeCondition(dsl));
        } else if (fullAddressSelect != null) {
            return DSL.field(idNext).in(fullAddressSelect.makeSelect(dsl));
        } else if (conditionInterpret != null) {
            return conditionInterpret.makeCondition(dsl);
        }
        return null;
    }

    String makeConditionString() {
        StringBuilder condition = new StringBuilder();
        if (fullAddressSelect != null && conditionInterpret != null) {
            condition.append(makeField(idNext)).append(".in(")
                    .append(fullAddressSelect.makeSelect())
                    .append(").and(").append(conditionInterpret.makeCondition()).append(")");
        } else if (fullAddressSelect != null) {
            condition.append(makeField(idNext)).append(".in(").append(fullAddressSelect.makeSelect()).append(")");
        } else if (conditionInterpret != null) {
            condition.append(conditionInterpret.makeCondition());
        }
        log.debug("makeConditionString " + condition.toString());
        return condition.toString();
    }

    @Override
    public String interpret() {
        StringBuilder requestGet = new StringBuilder();
        String condition = makeConditionString();
        String fieldOrAsterisks = makeFields();
        requestGet.append("dsl.select(").append(fieldOrAsterisks).append(").from(").append(tableName).append(")");
        if (!condition.isEmpty()) {
            requestGet.append(".where(").append(condition).append(")");
        }
        requestGet.append(addGroupByAndLimit()).append(";");
        log.debug("interpret:" + requestGet.toString());
        return requestGet.toString();
    }

    String addGroupByAndLimit() {
        StringBuilder stringBuilder = new StringBuilder();
        if (groupBy != null) {
            stringBuilder.append(".groupBy(").append(makeField(groupBy)).append(")");
        }
        if (limit != null) {
            stringBuilder.append(".limit(").append(limit).append(")");
        }
        log.debug("addGroupByAndLimit: " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    Field makeFieldId(String id) {
        return DSL.field(id);
    }

    String makeField(String field) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DSL.field(").append(field).append(")");
        log.debug("makeField: " + stringBuilder.toString() + " field" + field);
        return stringBuilder.toString();
    }

    String makeFields() {
        if (fields.isEmpty()) {
            log.debug("makeField: ");
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < fields.size() - 1; i++) {
            stringBuilder.append(makeField(fields.get(i))).append(",");
        }
        stringBuilder.append(makeField(fields.get(fields.size() - 1)));
        log.debug("makeField: " + stringBuilder.toString());
        return stringBuilder.toString();
    }

    SelectFieldOrAsterisk[] makeFieldsId() {
        SelectFieldOrAsterisk[] fieldsArray = new SelectFieldOrAsterisk[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            fieldsArray[i] = makeFieldId(fields.get(i));
        }
        return fieldsArray;
    }

    public SelectRequest(String request, Variables variables, FullAddressSelect fullAddressSelect) {
        log.debug("request:" + request);
        this.fullAddressSelect = fullAddressSelect;
        String[] input = request.split(":");
        input[input.length - 1] = parseWhere(input[input.length - 1], variables);
        for (String port : input) {
            if (isIdNext(port)) {
                idNext = variables.addVariableField(makeId(port));
            } else if (isGroupBy(port)) {
                groupBy = variables.addVariableField(makeId(port));
            } else if (isLimit(port)) {
                limit = variables.addVariableLong(makeId(port));
            } else if (isField(port)) {
                fields.add(variables.addVariableField(makeId(port)));
            } else {
                tableName = variables.addVariableField(port);
            }
        }
        if (fullAddressSelect != null && idNext == null) {
            idNext = variables.makeString(variables.makeVariableFromString(fullAddressSelect.getTableName()) + "Id");
        }
        if (tableName == null) {
            //TODO exeption tableName=variables.makeTableName(id,idNext);
        }
    }

    record Port() {
        static final int TABLE = 0;
        static final int WHEN = 1;
    }

    String parseWhere(String request, Variables variables) {
        log.debug("request:" + request);
        String[] input = request.split("\\?");
        if (input.length == 1) {
            return request;
        }
        if (input.length != 2) {
            //TODO exception
        }
        conditionInterpret = new WhenCondition(input[Port.WHEN], variables);
        return input[Port.TABLE];
    }

    String makeId(String id) {
        return id.substring(1);
    }
    boolean isIdNext(String id) {
        return id.charAt(0) == '&';
    }
    boolean isField(String id) {
        return id.charAt(0) == '=';
    }
    boolean isLimit(String limit) {
        return limit.charAt(0) == '%';
    }
    boolean isGroupBy(String field) {
        return field.charAt(0) == '#';
    }

}
