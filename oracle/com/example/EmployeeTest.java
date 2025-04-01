package com.exmaple;
import java.util.*;
import java.util.stream.Collectors;

class Employee{
    private int id;
    private String name;
    private int age;
    private int salary;
    
    Employee(int id,String name,int age,int salary){
         this.id =id;
         this.name=name;
         this.age=age;
         this.salary= salary;
    }
    
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getAge(){
        return age;
    }
    public int getSalary(){
       return salary;
    }

    @Override
    public String toString() {
        return "Employee= [ id="+getId()+",name="+getName()+",age="+getAge()+",salary="+getSalary() +"]\n";
    }
     
 }
 
 
 public class EmployeeTest{
     public static void main(String[]args){
         List<Employee>employees = new ArrayList<>();
         employees.add(new Employee(1,"abc",34,454522));
         employees.add(new Employee(2,"def",23,56522));
         employees.add(new Employee(3,"ghi",20,56522));
         
         List<Employee> sortedBySalary = employees.stream()
                 .sorted((e1,e2) -> e2.getSalary() - e1.getSalary())
                 .collect(Collectors.toList());
         
         System.out.println(sortedBySalary);

         List<Employee> sortedByAge = employees.stream()
         .sorted((e1,e2) -> e1.getAge() - e2.getAge())
         .collect(Collectors.toList());
 
         System.out.println(sortedByAge);

         //wrong
         List<Employee> sortedBySalartThenAge = employees.stream()
         .sorted((e1,e2)->e2.getSalary() - e1.getSalary())
         .sorted((e1,e2) -> e1.getAge() - e2.getAge())
         .collect(Collectors.toList());

         System.out.println(sortedBySalartThenAge);


         //right
         employees.sort(
            Comparator.comparing(Employee::getSalary, Comparator.reverseOrder())
                      .thenComparing(Employee::getAge)
        );
        
        // Print the sorted list
        employees.forEach(System.out::println);
     }
 
}