package com.example;

public class Employee extends Person {

    //Flexible Constructor - adds flexibility of calling super later 
    //No need to be first statement in constructor
    Employee(String name, int age) {
        if(age <18){
            throw new IllegalStateException("Can't be employed");
        }
        super(name, age);
    }
    
}
