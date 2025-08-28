package com.example;

import java.util.List;

public class BookIterator implements Iterator<Book> {

    private final List<Book> bookList;
    private int index = 0;

    BookIterator(List<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public boolean hasNext() {
        return index != bookList.size(); 
    }

    @Override
    public Book next() {
        return bookList.get(index++);
    }

}
