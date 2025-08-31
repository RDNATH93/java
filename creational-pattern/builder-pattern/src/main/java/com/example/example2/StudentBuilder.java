package com.example.example2;

import java.util.List;

public abstract class StudentBuilder {
     String roll;
     String name;
     List<String> gurdian;
     String address;
     List<String> subjects;

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
     abstract StudentBuilder subjects();

     public Student build(){
          return new Student(this);
     }
}
