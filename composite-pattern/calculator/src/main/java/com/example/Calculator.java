package com.example;

public class Calculator {

    
        //2*(1+7)

       /*

                         *
                       /   \
                     2      +
                           / \
                          1   7

        */
        
    public static void main(String[] args) {
        System.out.println("calculator");
        ArithmeticExpression leftExpression = new Number(2);

        ArithmeticExpression one = new Number(1);
        ArithmeticExpression seven = new Number(7);
        ArithmeticExpression rightExpression=new Expression(one, seven, Operator.ADD);

        ArithmeticExpression expression = new Expression(leftExpression, rightExpression, Operator.MULTIPLY);

        int result = expression.evaluate();

        System.out.println("Result "+result);

    }
}
