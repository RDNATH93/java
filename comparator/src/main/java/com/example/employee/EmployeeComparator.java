package com.example.employee;

import java.util.Comparator;

public class EmployeeComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee e1, Employee e2) {
        int nameComparisonResult = new NameComparator().compare(e1, e2);
        if (nameComparisonResult == 0) {
            int ageComparisonResult = new AgeComparator().compare(e1, e2);
            if (ageComparisonResult == 0) {
                return new AddressComparator().compare(e1, e2);
            }
            return ageComparisonResult;
        }
        return nameComparisonResult;
    }
}
