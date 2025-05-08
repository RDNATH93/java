package code.decode;

import lombok.Setter;
import lombok.ToString;
import lombok.Getter;

@Getter
@Setter
@ToString
class Product{
    private String productId;
    private String name;
    private String category;
    private boolean inStock;
    private double price;
 
    public Product(String productId, String name, String category, boolean inStock, double price) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.inStock = inStock;
        this.price = price;
    }

}