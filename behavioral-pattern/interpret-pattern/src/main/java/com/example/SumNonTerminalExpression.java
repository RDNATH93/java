package com.example;

public class SumNonTerminalExpression extends AbstractExpression {

    AbstractExpression leftExpression;
    AbstractExpression rightExpression;

    SumNonTerminalExpression(AbstractExpression leftExpression, AbstractExpression rightExpression) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public int interpret(Context context) {
        return leftExpression.interpret(context)+rightExpression.interpret(context);
    }

}
