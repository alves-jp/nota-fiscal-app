package br.inf.ids.dto;

import br.inf.ids.model.enums.ProductStatus;

public class ProductDTO {

    private Long id;
    private String description;
    private ProductStatus productStatus;

    public ProductDTO(Long id, String description, ProductStatus productStatus) {
        this.id = id;
        this.description = description;
        this.productStatus = productStatus;
    }

    public ProductDTO() {
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