package com.example.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EmployeeTest {

    private static List<Employee> employeeList;

    @BeforeAll
    static void init() {
        employeeList = new ArrayList<Employee>();
        employeeList.add(new Employee("Ram", 34, new Address("New Delhi", "Delhi", "India", "516204")));
        employeeList.add(new Employee("Ram", 33, new Address("New Delhi", "Delhi", "India", "516204")));
        employeeList.add(new Employee("Ram", 34, new Address("Kanpur", "Uttar Pradesh", "India", "483286")));
        employeeList.add(new Employee("Kishan", 34, new Address("New Delhi", "Delhi", "India", "516204")));
        employeeList.add(new Employee("Ram", 34, new Address("New Delhi", "Delhi", "India", "356204")));
        employeeList.add(new Employee("Ram", 34, new Address("New Delhi", "Delhi", "India", "516204")));

    }

    @Test
    @DisplayName("employee comparator")
    void employeeComparator() {
        EmployeeComparator employeeComparator = new EmployeeComparator();
        int actual = employeeComparator.compare(employeeList.get(0), employeeList.get(1));
        assertNotEquals(0, actual);

        actual = employeeComparator.compare(employeeList.get(0), employeeList.get(5));
        assertEquals(0, actual);
    }

    @Test
    @DisplayName("sort employee using comparator")
    void sortUsingComparator() {
        Collections.sort(employeeList, new EmployeeComparator());
        employeeList.forEach(System.out::println);
    }
}
