package com.example;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

public class ReverseStringTest {

    //Test using junit 4
    @Test
    public void testReverseString() {
        ReverseString rvs = new ReverseString();
        String actual= rvs.reverseString("Madam");
        String expected="madaM";

        assertEquals(expected,actual);
    }
}
