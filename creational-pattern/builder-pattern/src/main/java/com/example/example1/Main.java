package com.example.example1;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Student student = Student.builder()
        .roll("123")
        .name("abc")
        .gurdian(List.of("def","ghi"))
        .address("2134 xyz st")
        .build();
        System.out.println(student);
    }
}
