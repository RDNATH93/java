package com.example;

import java.util.ArrayList;
import java.util.List;

public class Library implements Aggregrator<Book> {

    private List<Book> bookList = new ArrayList<>();

    public Library(List<Book> bookList) {
        this.bookList = bookList;
    }

    void addBook(Book book) {
        bookList.add(book);
    }

    @Override
    public Iterator<Book> createIterator() {
        return new BookIterator(bookList);
    }

}
