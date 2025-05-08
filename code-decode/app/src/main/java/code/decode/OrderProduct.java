package code.decode;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderProduct {
    public static void main(String[] args) {
        // Sample test data
        List<Order> orders = List.of(
            new Order(
                List.of(
                    new Product("P001", "Laptop", "Electronics", true, 45000.0),
                    new Product("P002", "Mouse", "Electronics", true, 500.0),
                    new Product("P003", "Keyboard", "Electronics", false, 1500.0)
                ),
                45500.0,
                LocalDateTime.now()
            ),
            new Order(
                List.of(
                    new Product("P004", "Shirt", "Clothing", true, 800.0),
                    new Product("P005", "Jeans", "Clothing", true, 1200.0),
                    new Product("P006", "Shoes", "Clothing", false, 2000.0)
                ),
                2000.0,
                LocalDateTime.now()
            ),
            new Order(
                List.of(
                    new Product("P007", "Book", "Stationery", true, 300.0),
                    new Product("P008", "Pen", "Stationery", true, 50.0),
                    new Product("P009", "Notebook", "Stationery", true, 150.0)
                ),
                500.0,
                LocalDateTime.now()
            )
        );

        // Print the test data for verification
        // orders.forEach(order -> {
        //     System.out.println("Order ID: " + order.getOrderId());
        //     System.out.println("Total Value: " + order.getTotalValue());
        //     System.out.println("Products:");
        //     order.getProducts().forEach(product -> {
        //         System.out.println("  - " + product.getName() + " (" + product.getCategory() + "), In Stock: " + product.isInStock() + ", Price: " + product.getPrice());
        //     });
        //     System.out.println();
        // });


        //Consider only orders with a total value  ₹500.
        orders.stream()
        .filter(order->Duration.between(LocalDateTime.now(), order.getOrderTime()).toHours() < 24L)
        .filter(order -> order.getTotalValue() >= 500)
        .flatMap(order->order.getProducts().stream())
        .filter(product->product.isInStock())
        .collect(Collectors.groupingBy(Product::getCategory,Collectors.counting()))
        .entrySet().stream()
        .sorted((e1,e2)->(int)(e2.getValue()-e1.getValue()))
        .forEach(entry->System.out.println(entry.getKey()+"="+entry.getValue()));


    }
}