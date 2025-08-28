package com.example;

public interface Aggregrator<E> {
    Iterator<E> createIterator();
}
