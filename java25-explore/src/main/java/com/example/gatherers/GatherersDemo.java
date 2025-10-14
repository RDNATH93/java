package com.example.gatherers;

import module java.base;

class GatherersDemo {
    void main() {
        // Gatherers.fold - collapse elements into single element
        List<Integer> numbers = List.of(10, 20, 30, 40, 50);

        Integer sum = numbers.stream().reduce(0, Integer::sum);
        IO.println("Sum is " + sum);

        int summ = numbers.stream().mapToInt(i -> i).sum();
        IO.println("Sum is " + summ);

        int sums = numbers.stream()
                .gather(Gatherers.fold(() -> 0, Integer::sum))
                .findFirst().orElse(0);

        IO.println("Sum is " + sums);

        // Gatherers.scan(seed,op) - running total/accumulation
        // (like reduce, but you get intemediate result at each step of the stream)

        List<Integer> transactions = List.of(1000, -200, -500, 300, -200);

        AtomicInteger amount = new AtomicInteger();
        var transactionHist = transactions.stream().map(amount::addAndGet).toList();

        IO.println("using stream " + transactionHist);

        var transactionHistory = transactions.stream().gather(Gatherers.scan(() -> 0, Integer::sum)).toList();
        IO.println("using gathrer " + transactionHistory);

        // Gathrers.windowFixed(size) - split the stream into fixed-size batches

        List<Integer> Orders = List.of(100, 101, 102, 103, 104, 105);

        // using stream
        int batchSize = 2;
        var subLists = IntStream.range(0, batchSize)
                .mapToObj(i -> Orders.subList(i * batchSize, Math.min((i + 1) * batchSize, Orders.size())))
                .toList();
        IO.println("SubLists using stream " + subLists);

        Orders.stream()
                .gather(Gatherers.windowFixed(3)).forEach(IO::println);

        // Gatheres.windowSliding(size) - create overlapping slidding windows
        var streamSubList = IntStream.range(0, Orders.size())
                .mapToObj(i -> Orders.subList(i, Math.min(i + batchSize, Orders.size())))
                .toList();
        IO.println("sliding window using stream " + streamSubList);

        var slidingWindowUsingGatherer = Orders.stream()
                .gather(Gatherers.windowSliding(batchSize))
                .toList();

        IO.println("sliding window using gatherer " + slidingWindowUsingGatherer);

        // Gatherer.mapConcurrent(size) - run mapping funtion concurrently (great for
        // IO/network tasks)

        var userIds = IntStream.rangeClosed(1, 10).boxed().toList();

        List<CompletableFuture<String>> futures = userIds.stream()
                .map(id -> CompletableFuture.supplyAsync(() -> fetchUserDetails(id)))
                .toList();

        List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();

        IO.println(results);

        userIds.stream()
                .gather(Gatherers.mapConcurrent(3, GatherersDemo::fetchUserDetails))
                .forEach(IO::println);

    }

    static String fetchUserDetails(Integer id) {
        try {
            Thread.sleep(10);
        } catch (Exception _) {

        }
        return "Fetched user profile for id " + id;
    }
}
