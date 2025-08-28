package com.example;

public class NumberTerminalExpression extends AbstractExpression {

    private String key;

    NumberTerminalExpression(String stringValue) {
        this.key = stringValue;
    }

    public int interpret(Context context) {
        return context.getValue(key);
    }
}
