package com.example;

public class Expression implements ArithmeticExpression{
    
    private ArithmeticExpression leftExpression;
    private ArithmeticExpression rightExpression;
    private Operator operator;

    Expression(ArithmeticExpression leftExpression,ArithmeticExpression rightExpression,Operator operator){
        this.leftExpression=leftExpression;
        this.rightExpression=rightExpression;
        this.operator=operator;
    }
    
    public int evaluate(){
      
        switch (operator) {
            case ADD:
                return leftExpression.evaluate() + rightExpression.evaluate();
            case SUBSTRACT:
                return leftExpression.evaluate() - rightExpression.evaluate();  
            case MULTIPLY:
                return leftExpression.evaluate() * rightExpression.evaluate();
            case DIVIDE:
                return leftExpression.evaluate() / rightExpression.evaluate();                                  
        }
        return 0;
    }
}
