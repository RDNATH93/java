package com.example;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static List<Employee> employeeList = new ArrayList<Employee>();
    static {
        employeeList.add(new Employee(1, "Ram","IT", 32));
        employeeList.add(new Employee(1, "Shyam","ADMIN", 28));
        employeeList.add(new Employee(1, "Kishan","STAFF", 24));
        employeeList.add(new Employee(1, "Mohan","IT", 34));
        employeeList.add(new Employee(1, "Siva","ADMIN", 27));
    }

    public static void main(String[] args) {
        IntSummaryStatistics statistics = employeeList.stream().mapToInt(emp -> emp.getAge()).summaryStatistics();
        System.out.println("Average age of all Employee " + statistics.getAverage());

        System.out.println("Min age of employee " + statistics.getMin());
        System.out.println("Max age of employee " + statistics.getMax());
        System.out.println("Sum of employees age " + statistics.getSum());
        System.out.println("Employee count " + statistics.getCount());

        // print all employees age in sorted order
        System.out.println("Employee age in sorted order");
        employeeList.stream().map(emp -> emp.getAge())
                .collect(Collectors.toList())
                .stream()
                .sorted()
                .forEach(age -> System.out.print(age + " "));

        System.out.println();

        // print second and thrid youngest employee details
        System.out.println("Second and third youngest employee");
        employeeList.stream()
                .sorted()
                .skip(1)
                .limit(2)
                .forEach(System.out::println);

        // print all employess name seperated by comma
        String names = employeeList.stream()
                .map(emp -> emp.getName().toUpperCase())
                .collect(Collectors.joining(", "));

        System.out.println("Employees name -> " + names);


       Map<String,List<Employee>> employeeMapByDept= employeeList.stream()
        .collect(Collectors.groupingBy(emp->emp.getDepartment()));

        employeeMapByDept.entrySet()
        .stream()
        .forEach(System.out::println);
    }
}