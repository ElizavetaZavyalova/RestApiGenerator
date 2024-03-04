package org.example.analize.premetive;

import org.example.analize.interpretation.Interpretation;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.Optional;

import static org.example.analize.premetive.BaseFieldParser.Action.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Request.Action.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Request.TypeVar.*;

public abstract class BaseFieldParser<R> implements Interpretation<R> {
    protected String fieldName;
    protected String nameInRequest;
    protected String realFieldName;

    public enum Type {
        STRING(_STRING),

        BOOLEAN(_BOOLEAN),

        INTEGER(_INTEGER),

        LONG(_LONG);
        final String ident;

        Type(String ident) {
            this.ident = ident;
        }

        static Type getType(String string) throws IllegalArgumentException {
            return getType(string,LONG);
        }
        public static Type getType(String string,Type defaultType) throws IllegalArgumentException {
            if (string.length() < 2) {
                throw new IllegalArgumentException(string + "is not must be bigger then 2");
            }
            for (Type type : Type.values()) {
                if (string.endsWith(type.ident)) {
                    return type;

                }
            }
            return defaultType;
        }

        public static boolean isTypeDigit(Type type) {
            return type.equals(INTEGER) || type.equals(LONG);
        }

        public static String deleteType(String string, Type type) {
            if (string.endsWith(type.ident)) {
                return string.substring(0, string.length() - type.ident.length());
            }
            return string;
        }

    }

    protected Type type;
    protected Action action;

    protected enum Action {

        EQ(_EQ),
        NE(_NE),
        LIKE(_LIKE),
        NOT_LIKE(_NOT_LIKE),
        GE(_GE),
        GT(_GT),
        LE(_LE),
        LT(_LT),
        IN(_IN),
        DEFAULT(_DEFAULT);

        final String names;

        Action(String str) {
            names = str;
        }

        static Action getAction(String s) {
            for (Action type : Action.values()) {
                if (s.startsWith(type.names)) {
                    return type;
                }
            }
            return getActionTypeIfDefault(DEFAULT);
        }

        static String deleteAction(String string, Action type) {
            if (type.equals(DEFAULT)) {
                return string.substring(type.names.length());
            } else if (string.startsWith(type.names)) {
                return string.substring(type.names.length() + 1);
            }
            return string;
        }

        static Action getActionTypeIfDefault(Action type) {
            if (type.equals(DEFAULT)) {
                return EQ;
            }
            return type;
        }


    }

    protected BaseFieldParser(String variable, Endpoint parent) throws IllegalArgumentException {
        nameInRequest = variable;
        type = Type.getType(variable);
        variable = Type.deleteType(variable, type);
        action = Action.getAction(variable);
        this.fieldName = variable;
        this.realFieldName = Optional.ofNullable(parent.getRealFieldName(Action.deleteAction(variable, action)))
                .orElse(fieldName);
        action = Action.getActionTypeIfDefault(action);
        throwExceptionIfTypeAndActionIsNotCorrect();
    }


    void throwExceptionIfTypeAndActionIsNotCorrect() throws IllegalArgumentException {
        if (type.equals(Type.BOOLEAN) && !(action.equals(EQ) || action.equals(NE) || action.equals(IN))) {
            throw new IllegalArgumentException("type boolean must be eq or ne");
        }
        if (Type.isTypeDigit(type) && (action.equals(LIKE) || action.equals(NOT_LIKE))) {
            throw new IllegalArgumentException("type number can't be like or not_like");
        }
    }
}
