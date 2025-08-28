package com.example;

public abstract class PaymentFlow {
    //abstract methods can be implemented in any order 
    abstract void validateRquest();
    abstract void calculateFees();
    abstract void debitAmount();
    abstract void creaditAmount();

    //this is Template method, which defines the order of steps to execute the task
    public final void sendMony(){

        //step1
        validateRquest();

        //step2
        calculateFees();

        //step3
        debitAmount();

        //step4
        creaditAmount();
    }
}
