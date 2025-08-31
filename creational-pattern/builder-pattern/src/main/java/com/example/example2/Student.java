package com.example.example2;

import java.util.List;

public class Student {
    private String roll;
    private String name;
    private List<String> gurdian;
    private String address;
    private List<String> subjects;

    Student(StudentBuilder builder) {
        this.roll = builder.roll;
        this.name = builder.name;
        this.gurdian = builder.gurdian; 
        this.address = builder.address;
        this.subjects = builder.subjects; 
    }

    public String getRoll() {
        return roll;
    }

    public String getName() {
        return name;
    }

    public List<String> getGurdian() {
        return gurdian;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    @Override
    public String toString() {
        return "Student [roll=" + roll + ", name=" + name + ", gurdian=" + gurdian + ", address=" + address
                + ", subjects=" + subjects + "]";
    }
}
