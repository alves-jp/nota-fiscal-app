package br.inf.ids.model;

import br.inf.ids.model.enums.ProductStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "produtos")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_produto")
    private Long id;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;


    public Product(String description, ProductStatus productStatus) {
        this.description = description;
        this.productStatus = productStatus;
    }

    public Product() {
    }

    public Long getId() {
        return id;
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
