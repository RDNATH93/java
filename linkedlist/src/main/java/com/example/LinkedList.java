package com.example;



public class LinkedList<T> {

    class Node<T> {
        T data;
        Node<T> next;
    
        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }


    private Node<T> header;
    private Node<T> ptr;

    public void add(T data) {
        if (header == null) {
            ptr = new Node<T>(data);
            header = ptr;
        } else {
            ptr.next = new Node<T>(data);
            ptr = ptr.next;
        }
    }

    public T remove() {
        if (header !=null){
            T obj ;
            if (ptr != header) {
                Node<T> tempPtr = header;
                while (tempPtr.next != ptr) {
                    tempPtr = tempPtr.next;
                }
                tempPtr.next = null;
                 obj=ptr.data;
                ptr = tempPtr;
            } else {
                obj = ptr.data;
                ptr = null;
                header = null;
            }
            return obj;
        }

        return null;
    }

    public void print() {
        Node<T> tempPtr = header;
        while (tempPtr != null) {
            System.out.print(tempPtr.data +"->");
            tempPtr = tempPtr.next;
        }
        System.out.println();
    }

    public long length(){
        long count=0L;

        if(header!=null){
            Node<T> tempPtr=header;
            while(tempPtr!=null){
                count++;
                tempPtr=tempPtr.next;
            }
        }
        return count;
    }

    public T findMiddle(){
        if(header!=null){
            Node<T> fastPointer=header;
            Node<T> slowPointer=header;

            while(fastPointer!=null && fastPointer.next!=null){
                fastPointer=fastPointer.next.next;
                slowPointer=slowPointer.next;
            }
            return slowPointer.data;
        }
        return null;
    }
}
