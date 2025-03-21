package br.inf.ids.model;

import br.inf.ids.model.enums.ProductStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "produtos")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productId;

    private String description;

    @Column(nullable = false)
    private ProductStatus productStatus;


    public Product(String productId, String description, ProductStatus productStatus) {
        this.productId = productId;
        this.description = description;
        this.productStatus = productStatus;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }
}
