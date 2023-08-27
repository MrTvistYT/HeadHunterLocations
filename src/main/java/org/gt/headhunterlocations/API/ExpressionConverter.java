package org.gt.headhunterlocations.API;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ExpressionConverter {
    private static final Map<String, Integer> OPERATOR_PRECEDENCE;

    static {
        OPERATOR_PRECEDENCE = new HashMap<>();
        OPERATOR_PRECEDENCE.put("+", 1);
        OPERATOR_PRECEDENCE.put("-", 1);
        OPERATOR_PRECEDENCE.put("*", 2);
        OPERATOR_PRECEDENCE.put("/", 2);
        OPERATOR_PRECEDENCE.put("%", 2);
    }

    public static int evaluateExpression(String expression, int karma) {
        String[] tokens = expression.split(" ");
        Stack<Integer> values = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            if (isNumber(token)) {
                values.push(Integer.parseInt(token));
            } else if (token.equals("%karma%")) {
                values.push(karma);
            } else if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    evaluateOperation(values, operators);
                }
                operators.pop(); // Pop the opening parenthesis
            } else {
                while (!operators.isEmpty() && hasHigherPrecedence(token, operators.peek())) {
                    evaluateOperation(values, operators);
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            evaluateOperation(values, operators);
        }

        return values.pop();
    }

    private static boolean isNumber(String token) {
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean hasHigherPrecedence(String op1, String op2) {
        int prec1 = OPERATOR_PRECEDENCE.getOrDefault(op1, 0);
        int prec2 = OPERATOR_PRECEDENCE.getOrDefault(op2, 0);
        return prec1 > prec2;
    }

    private static void evaluateOperation(Stack<Integer> values, Stack<String> operators) {
        int operand2 = values.pop();
        int operand1 = values.pop();
        String operator = operators.pop();
        int result = performOperation(operand1, operand2, operator);
        values.push(result);
    }

    private static int performOperation(int operand1, int operand2, String operator) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand1 / operand2;
            case "%":
                return operand1 % operand2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}