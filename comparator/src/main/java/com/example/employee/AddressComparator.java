package com.example.employee;

import java.util.Comparator;

public class AddressComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee e1, Employee e2) {
        if (e1.getAddress().getCity().compareTo(e2.getAddress().getCity()) == 0) {
            if (e1.getAddress().getState().compareTo(e2.getAddress().getState()) == 0) {
                if (e1.getAddress().getPincode().compareTo(e2.getAddress().getPincode()) == 0) {
                    return e1.getAddress().getCountry().compareTo(e2.getAddress().getCountry());
                } else {
                    return e1.getAddress().getPincode().compareTo(e2.getAddress().getPincode());
                }
            } else {
                return e1.getAddress().getState().compareTo(e2.getAddress().getState());
            }
        } else {
            return e1.getAddress().getCity().compareTo(e2.getAddress().getCity());
        }
    }

}
