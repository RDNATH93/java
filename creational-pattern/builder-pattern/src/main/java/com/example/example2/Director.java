package com.example.example2;

import java.util.List;

public class Director {

    StudentBuilder studentBuilder;
    
    Student createStudent(String type){
        if("engineer".equalsIgnoreCase(type)){
          studentBuilder=new EngineeringStudentBuilder();
          return createEngineeringStudent(studentBuilder);
        }
        if("MBA".equalsIgnoreCase(type)){
           studentBuilder=new MBAStudentBuilder();
           return createMBAStudent(studentBuilder);
        }
        throw new IllegalArgumentException(); 
    }

    private Student createMBAStudent(StudentBuilder studentBuilder) {
        return studentBuilder.roll("123").name("abc").subjects().build();
    }

    private Student createEngineeringStudent(StudentBuilder studentBuilder) {
        return studentBuilder.roll("454").name("dfdf")
        .gurdian(List.of("gg","df")).subjects().build();
    }
}
