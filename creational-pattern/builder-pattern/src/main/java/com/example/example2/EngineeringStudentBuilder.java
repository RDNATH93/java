package com.example.example2;

import java.util.List;

public class EngineeringStudentBuilder extends StudentBuilder {

    @Override
    public StudentBuilder subjects() {
        this.subjects=List.of("Math","Statistics");
       return this;
    }
    
}
