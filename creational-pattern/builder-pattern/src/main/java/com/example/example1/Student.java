package com.example.example1;

import java.util.List;

public class Student {
    private String roll;
    private String name;
    private List<String> gurdian;
    private String address;

    private Student(StudentBuilder builder) {
        this.roll = builder.roll;
        this.name = builder.name;
        this.gurdian = builder.gurdian;
        this.address = builder.address;
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

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    @Override
    public String toString() {
        return "Student [roll=" + roll + ", name=" + name + ", gurdian=" + gurdian + ", address=" + address + "]";
    }



    public static class StudentBuilder {
        private String roll;
        private String name;
        private List<String> gurdian;
        private String address;

        public StudentBuilder roll(String roll) {
            this.roll = roll;
            return this;
        }

        public StudentBuilder name(String name) {
            this.name = name;
            return this;
        }

        public StudentBuilder gurdian(List<String> gurdian) {
            this.gurdian = gurdian;
            return this;
        }

        public StudentBuilder address(String address) {
            this.address = address;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }

}
