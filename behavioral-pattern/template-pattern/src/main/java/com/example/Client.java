package com.example;

/**
 * why it's required and when to use ?
 * when you want all classes to follow the specific steps to process the task
 * but also 
 * need to provide the flexibility that each class can have their own logic in that specific steps.
*/
public class Client {
    public static void main( String[] args ){
     
        PaymentFlow payToFriend = new PaymentToFriend();
        PaymentFlow payToMerchant = new PaymentToMerchant();

        
        payToFriend.sendMony();
        payToMerchant.sendMony();
    }
}
