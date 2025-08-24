package com.example;
public class Number implements ArithmeticExpression {
    int number;
    Number(int number){
        this.number=number;
    }

    public int evaluate(){
        return number;
    }
}
