package org.example.analize.postfix_infix;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import static org.example.analize.postfix_infix.Converter.RegExp.FIND_OPERATOR_OR_BRACKET;
import static org.example.analize.postfix_infix.Converter.RegExp.NOT_DELETE_EMPTY_STRING_ON_END;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Request.*;

@Slf4j
@UtilityClass
public class Converter {
    public record RegExp() {
        static final int NOT_DELETE_EMPTY_STRING_ON_END = -1;
        public static final String FIND_OPERATOR_OR_BRACKET = "([|)&(])";
    }


    public static boolean isOperator(String operator) {
        return isAND(operator) || isOR(operator);
    }

    public static  boolean isAND(String operator) {
        return operator.equals(AND_);
    }

    public static boolean isOR(String operator) {
        return operator.equals(OR_);
    }

    public static  Queue<String> toPostfix(String condition) throws IllegalArgumentException {
        Stack<String> stack = new Stack<>();
        Queue<String> queue = new LinkedList<>();
        String[] elements = condition.split(FIND_OPERATOR_OR_BRACKET, NOT_DELETE_EMPTY_STRING_ON_END);
        int operatorIndex = 0;
        int elementCount = 0;
        final int OPERATORS_LENGTH = elements.length - 1;
        for (String element : elements) {
            operatorIndex += element.length();
            addElementOnQueue(queue, element);
            if (elementCount < OPERATORS_LENGTH) {
                String operator = String.valueOf(condition.charAt(operatorIndex));
                if (isLeftBracket(operator)) {
                    pushOperatorOnStack(stack, operator);
                } else if (isRightBracket(operator)) {
                    pushStackOnQueryWhileLeftBracketNotFound(stack, queue);
                } else {
                    //operator=operator+condition.charAt(operatorIndex+1);
                    operatorProcessing(stack, queue, operator);
                }
                operatorIndex += operator.length();
            }
            elementCount++;
        }
        addAllToStack(stack, queue);
        return queue;
    }

    void operatorProcessing(Stack<String> stack, Queue<String> queue, String operand) throws IllegalArgumentException {
        if (stack.isEmpty()) {
            pushOperatorOnStack(stack, operand);
        } else if (getPriority(operand) > getPriority(stack.peek())) {
            pushOperatorOnStack(stack, operand);
        } else {
            pushStackOnQueryWhilePriorityIsBigger(stack, queue, operand);
        }
    }

    static void addAllToStack(Stack<String> stack, Queue<String> queue) throws IllegalArgumentException {
        while (!stack.isEmpty()) {
            if (isLeftBracket(stack.peek())) {
                throw new IllegalArgumentException(RIGHT_BRACKET + " NOT FOUND");
            }
            addElementOnQueue(queue, stack.pop());
        }
    }

    static void pushOperatorOnStack(Stack<String> stack, String operand) {
        stack.push(operand);
    }

    static void addElementOnQueue(Queue<String> queue, String element) {
        if (!element.isEmpty()) {
            queue.add(element);
        }
    }

    static void pushStackOnQueryWhilePriorityIsBigger(Stack<String> stack, Queue<String> queue, String operand) {
        if (stack.isEmpty()) {
            pushOperatorOnStack(stack, operand);
            return;
        }
        while ((getPriority(operand) <= getPriority(stack.peek())) && !isLeftBracket(stack.peek())) {
            addElementOnQueue(queue, stack.pop());
            if (stack.isEmpty()) {
                break;
            }
        }
        pushOperatorOnStack(stack, operand);
    }

    static void pushStackOnQueryWhileLeftBracketNotFound(Stack<String> stack, Queue<String> queue) throws IllegalArgumentException {
        boolean isLeftBracketFound = false;
        while (!stack.isEmpty()) {
            String stackOperand = stack.pop();
            if (isLeftBracket(stackOperand)) {
                isLeftBracketFound = true;
                break;
            }
            addElementOnQueue(queue, stackOperand);
        }
        if (!isLeftBracketFound) {
            throw new IllegalArgumentException(LEFT_BRACKET + " NOT FOUND");
        }
    }

    static boolean isLeftBracket(String operand) {
        return operand.equals(LEFT_BRACKET);
    }

    static boolean isRightBracket(String operand) {
        return operand.equals(RIGHT_BRACKET);
    }

    static int getPriority(String operand) throws IllegalArgumentException {
        return switch (operand) {
            case AND_ -> 2;
            case OR_ -> 1;
            case LEFT_BRACKET -> -1;
            default -> throw new IllegalStateException("NOT CORRECT: " + operand);
        };
    }
}
