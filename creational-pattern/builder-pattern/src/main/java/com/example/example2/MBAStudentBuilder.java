package com.example.example2;

import java.util.List;

public class MBAStudentBuilder extends StudentBuilder {

    @Override
    StudentBuilder subjects() {
        this.subjects=List.of("English","Economics");
        return this;
    }
}
