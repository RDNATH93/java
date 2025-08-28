package com.example;

public class PaymentToMerchant extends PaymentFlow {

    @Override
    void validateRquest() {
        System.out.println("Validate payment requst");
    }

    @Override
    void debitAmount() {
        System.out.println("Debit calculated amount");
    }

    @Override
    void calculateFees() {
        System.out.println("2% fees will be applied");
    }

    @Override
    void creaditAmount() {
        System.out.println("Credit remaining amount");
    }

}