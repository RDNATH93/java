package com.example.example2;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        Director director = new Director();
        Student student = director.createStudent("engineer"); 

        System.out.println(student);
    }
}
