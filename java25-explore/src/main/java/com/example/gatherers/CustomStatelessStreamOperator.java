package com.example.gatherers;

import module java.base;
import java.util.stream.Gatherer.Downstream;

class CustomStatelessStreamOperator {

    void main() {
        // print squre of numbers

        // using stream
        IO.println("Squre of numbers using stream ");
        Stream.of(1, 2, 3, 4, 5)
                .map(i -> i * i)
                .forEach(IO::println);

        IO.println("Squre of numbers using gatherer claass ");
        Stream.of(1, 2, 3, 4, 5)
                .gather(Gatherer.of(new SqureNumber()))
                .forEach(IO::println);

        IO.println("Squre of numbers using gatherer method ");
        Stream.of(1, 2, 3, 4, 5)
                .gather(squre())
                .forEach(IO::println);

        IO.println("Squre of numbers with custom filter using gatherer claass ");
        Stream.of(1, 2, 3, 4, 5)
                .gather(Gatherer.of(new CustomFilter()))
                .gather(Gatherer.of(new SqureNumber()))
                .forEach(IO::println);

    }

    Gatherer<Integer, Void, Integer> squre() {
        return Gatherer.of((_, element, downstream) -> {
            IO.println("Integrating number " + element);
            downstream.push(element * element);
            return true;
        });
    }
}

class SqureNumber implements Gatherer.Integrator<Void, Integer, Integer> {

    @Override
    public boolean integrate(Void state, Integer element, Downstream<? super Integer> downstream) {
        IO.println("Integrating number " + element);
        downstream.push(element * element);
        return true; // continue the processing
    }

}

class CustomFilter implements Gatherer.Integrator<Void, Integer, Integer> {

    @Override
    public boolean integrate(Void state, Integer element, Downstream<? super Integer> downstream) {
        IO.println("Filtering elements");
        if (element > 3) {
            downstream.push(element);
        }

        return true;
    }

}