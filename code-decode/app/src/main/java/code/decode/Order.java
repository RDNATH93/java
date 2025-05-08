package code.decode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class Order {
  
    private int orderId;
    private List<Product> products;
    private double totalValue;
    private LocalDateTime orderTime;

    public Order(List<Product> products, double totalValue, LocalDateTime orderTime) {
        
        this.products = products;
        this.totalValue = totalValue;
        this.orderTime = orderTime;
    }
}