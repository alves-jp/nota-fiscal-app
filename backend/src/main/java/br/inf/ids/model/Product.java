package br.inf.ids.model;

import br.inf.ids.model.enums.ProductStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "produtos")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", unique = true, nullable = false)
    private String productCode;

    @Column(name = "descricao")
    private String description;

    @Column(name = "situacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;


    public Product(String productCode, String description, ProductStatus productStatus) {
        this.productCode = productCode;
        this.description = description;
        this.productStatus = productStatus;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
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
