package com.example;

public class BinaryNonTerminalExpression extends AbstractExpression {

    AbstractExpression leftExpression;
    AbstractExpression rightExpression;
    String operator;

    BinaryNonTerminalExpression(AbstractExpression leftExpression, AbstractExpression rightExpression,
            String operator) {
                this.leftExpression=leftExpression;
                this.rightExpression=rightExpression;
                this.operator = operator;
    }

    @Override
    public int interpret(Context context) {
        switch (operator) {
            case "+":
               return leftExpression.interpret(context) + rightExpression.interpret(context);
            
            case "*":
                return leftExpression.interpret(context) * rightExpression.interpret(context);
            default:
                return 0;
        }
    }

}
