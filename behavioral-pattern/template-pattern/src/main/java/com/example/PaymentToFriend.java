package com.example;

public class PaymentToFriend extends PaymentFlow {

    @Override
    void validateRquest() {
        System.out.println("Validate payment requst");
    }

    @Override
    void creaditAmount() {
        System.out.println("Credit full amount");
    }

    @Override
    void calculateFees() {
        System.out.println("0% fees will be applied");
    }

    @Override
    void debitAmount() {
        System.out.println("Debit calculated amount");
    }

}
