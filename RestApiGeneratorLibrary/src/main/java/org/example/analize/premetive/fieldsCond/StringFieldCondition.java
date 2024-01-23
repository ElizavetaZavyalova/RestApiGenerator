package org.example.analize.premetive.fieldsCond;

import org.example.read_json.rest_controller_json.Endpoint;


public class StringFieldCondition extends BaseFieldCondition<String> {
    public StringFieldCondition(String variable, String tableName, Endpoint parent) throws IllegalArgumentException {
        super(variable, tableName, parent);
    }

    @Override
    public String interpret() {
        StringBuilder builder = new StringBuilder("\nDSL.field(" + toString(tableName + "." + realFieldName) + ").");
        switch (action) {
            case EQ -> builder.append("eq");
            case NE -> builder.append("ne");
            case LE -> builder.append("le");
            case LT -> builder.append("lt");
            case GE -> builder.append("ge");
            case GT -> builder.append("gt");
            case LIKE -> builder.append("like");
            case NOT_LIKE -> builder.append("not_like");
        }
        return builder.append("(").append(fieldName).append(")").toString();
    }

    String toString(String string) {
        return "\"" + string + "\"";
    }

    @Override
    public String getParams() {
        return "";
    }
}
