package com.example;

//Before Java 25
//public class Main {
//    public static void main(String[] args) {
//        System.out.println("Hello world!");
//    }
//}


//Java 25
//Instance main method
//No need to create static variable or instance of Main calss to access varaible i inside main method 
class Main {
    int i=10;
    void main() {
        IO.println("Hello world!");
        IO.println("Value of i is "+i);
    }

}
