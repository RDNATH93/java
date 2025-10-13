// Compact Class - without class name

import java.util.Scanner;

int i = 5;

void main() {
    IO.println("main method");
    IO.println("Value of i is " + i);
    doSomthing();
}

void doSomthing() {

    //before Java 25
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter name: ");
    String name = scanner.nextLine();
    System.out.println("Name is : "+name);
    
    //Java 25
    name = IO.readln("Enter name: ");
    IO.println("Name is : "+name);
    
    scanner.close();
}