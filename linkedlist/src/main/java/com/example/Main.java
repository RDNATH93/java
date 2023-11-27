package com.example;

public class Main {
    public static void main(String[] args) {

        LinkedList list = new LinkedList();
        list.add(2);
        list.add("abc");
        list.add('f');
        list.add(true);

        list.print();
        list.remove();
        list.print();

        LinkedList<Integer> list2 = new LinkedList<Integer>();

        list2.print();
        Integer integer = list2.remove();
        System.out.println(integer);

        list2.add(4);
        list2.print();
        integer = list2.remove();
        System.out.println(integer);

        list2.print();
        integer = list2.remove();
        System.out.println(integer);

        list2.add(6);
        list2.add(7);
        list2.add(8);
        list2.print();
        integer = list2.remove();
        System.out.println(integer);
        list2.print();

        System.out.println("Length " + list2.length());
        System.out.println("Middle element " + list2.findMiddle());

        integer = list2.remove();
        //integer = list2.remove();
        System.out.println("Length " + list2.length());
        System.out.println("Middle element " + list2.findMiddle());
        
        list2.add(9);
        list2.add(10);
        list2.add(11);
        //list2.add(12);

        list2.print();
        System.out.println("Length " + list2.length());
        System.out.println("Middle element " + list2.findMiddle());
        

    }
}