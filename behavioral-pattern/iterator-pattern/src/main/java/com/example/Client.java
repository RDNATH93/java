package com.example;

import java.util.Arrays;
import java.util.List;

/**
 * It's a Behavioral Pattern
 *It provides a way to access element of a Collection sequentially without exposing 
 * the underlying representation of the collection 
 */
public class Client {
    public static void main( String[] args ) {
         List<Book> bookList = Arrays.asList(
                new Book("A","Author A"),
                new Book("B", "Author B")
         );
         Library library = new Library(bookList);

         Iterator<Book> iterator = library.createIterator();
        
         while(iterator.hasNext()){
            Book book = iterator.next();
            System.out.println(book);
         }
    }
}
