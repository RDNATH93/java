package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;


public class CalcTest {
    @Test
    public void testDivide() {
       // fail("Not yet implemented");
       Calc calc= new Calc();
      int actual= calc.divide(10,5);
      int expected=2;
       assertEquals(expected,actual);
    }
}
