package com.example;

/**
 * It's a Behavioral Pattern
 * it's based on CONTEXT what we interpret
 *
 */
public class App {
    public static void main(String[] args) {

        Context context = new Context();
        context.addValue("a", 2);
        context.addValue("b", 4);
        context.addValue("c", 5);
        context.addValue("d", 7);

        // a+b
        AbstractExpression expression1 = new SumNonTerminalExpression(
                new NumberTerminalExpression("a"),
                new NumberTerminalExpression("b"));

        System.out.println("(a+b) = " + expression1.interpret(context));

        // (a+b)*c
        AbstractExpression expression2 = new MultiplyNonTerminalExpression(
                new NumberTerminalExpression("c"),
                new SumNonTerminalExpression(
                        new NumberTerminalExpression("a"),
                        new NumberTerminalExpression("b")));

        System.out.println("((a+b)*c) = " + expression2.interpret(context));

        // ((a+b)*(c+d))
        AbstractExpression expression3 = new BinaryNonTerminalExpression(
                new BinaryNonTerminalExpression(
                        new NumberTerminalExpression("a"),
                        new NumberTerminalExpression("b"), "+"),
                new BinaryNonTerminalExpression(
                        new NumberTerminalExpression("c"),
                        new NumberTerminalExpression("d"), "+"),
                "*");

        System.out.println("((a+b)*(c+d)) = " + expression3.interpret(context));
    }

}
