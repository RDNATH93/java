package com.example;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;


/** 
* when using TestInstance annotattion with PER_CLASS lifecycle, static keyword is not need for
* @BeforeAll and @AfterAll annotated method 
*/
@TestInstance(Lifecycle.PER_CLASS)
public class ReverseStringTest5 {
   // static ReverseString rvs;
    ReverseStringTest5(){
        System.out.println("Test obj is created before test method");
    }
    @BeforeAll
    void init(){  //static void init(){
        // rvs=new ReverseString();
        System.out.println("Before all test");
    }

    @BeforeEach
    void testBeforeEarch(){
        System.out.println("Before each test");
    }

    @Test
    void testArrayEqual(){
            System.out.println("Actual test is running");
            int expected[]={2,4,6,8};
            int actual[]= {4,6,2,8};
            Arrays.sort(actual);

            assertArrayEquals(expected, actual);

    }

    @Test
    void testReverseString() {
        System.out.println("Actual test is running");
        ReverseString rvs =new ReverseString();
        String actual= rvs.reverseString("Java");
        String expected= "avaJ";

        assertEquals(expected, actual);
        //assertEquals("ava", actual);
    }

    @AfterEach
    void testAfterEarch(){
        System.out.println("After each test");
    }

    @AfterAll
    void testAfterAll(){  //static void testAfterAll(){
        System.out.println("After all test");
    }

}
