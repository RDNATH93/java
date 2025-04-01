package com.exmaple;

public class Test{
     public static void main(String [] args){
        method(null);
     }
     public static void method(Object o){
        System.out.print("object method");
     }
     
   //   public static void method(Object o){
   //      System.out.print(" another object method");
   //   }
     public static void method(String str){
        System.out.print("string method");
     }

}