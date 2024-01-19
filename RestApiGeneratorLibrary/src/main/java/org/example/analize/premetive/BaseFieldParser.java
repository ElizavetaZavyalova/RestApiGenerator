package org.example.analize.premetive;

import org.example.analize.interpretation.Interpretation;
import org.example.analize.premetive.fields.BaseField;
import org.example.read_json.rest_controller_json.Endpoint;

import static org.example.analize.premetive.BaseFieldParser.Action.*;
import static org.example.analize.premetive.BaseFieldParser.Action.NOT_LIKE;

public abstract class BaseFieldParser<R> implements Interpretation<R> {
    protected String fieldName;
    protected String realFieldName;

    protected enum Type {
        STRING("s:"), BOOLEAN("b:"), INTEGER("i:");
        String ident;

        Type(String ident) {
            this.ident = ident;
        }

        static Type getType(String string) throws IllegalArgumentException {
            if (string.length() < 2) {
                throw new IllegalArgumentException(string + "is must be bigger then 2");
            }
            for (Type type : Type.values()) {
                if (string.startsWith(type.ident) || string.startsWith(type.ident.toUpperCase())) {
                    return type;

                }
            }
            return INTEGER;
        }

        static String deleteType(String string, Type type) {
            if (string.startsWith(type.ident) || string.startsWith(type.ident.toUpperCase())) {
                return string.substring(type.ident.length());
            }
            return string;
        }

    }

    protected Type type;
    protected Action action;

    protected enum Action {

        EQ("eq"),
        NE("ne"),
        LIKE("like"),
        NOT_LIKE("notLike"),
        GE("ge"),
        GT("gt"),
        LE("le"),
        LT("lt");

        final String names;

        Action(String str) {
            names = str;
        }

        static Action getAction(String s) {
            for ( Action type :  Action.values()) {
                if (s.startsWith(type.names)) {
                    return type;
                }
            }
            return EQ;
        }

        static String deleteAction(String string, Action type) {
            if (string.startsWith(type.names)) {
                return string.substring(type.names.length() + 1);
            }
            return string;
        }

    }

    protected BaseFieldParser(String variable, Endpoint parent) throws IllegalArgumentException {
        type =  Type.getType(variable);
        variable =  Type.deleteType(variable, type);
        action =  Action.getAction(variable);
        this.fieldName = variable;
        this.realFieldName = parent.getRealFieldName(Action.deleteAction(variable, action));
        throwExceptionIfTypeAndActionIsNotCorrect();

    }

    void throwExceptionIfTypeAndActionIsNotCorrect() throws IllegalArgumentException {
        if (type.equals(BaseField.Type.BOOLEAN) && !(action.equals(EQ) || action.equals(NE))) {
            throw new IllegalArgumentException("TYPE BOOLEAN MUST BE EQ OR NE");
        }
        if (type.equals(BaseField.Type.INTEGER) && (action.equals(LIKE) || !action.equals(NOT_LIKE))) {
            throw new IllegalArgumentException("TYPE STRING CAN BE  ONLY LIKE");
        }
    }
}
