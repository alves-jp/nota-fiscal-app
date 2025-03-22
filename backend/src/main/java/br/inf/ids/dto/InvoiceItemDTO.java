package br.inf.ids.dto;

public class InvoiceItemDTO {

    private Long id;
    private Long invoiceId;
    private Long productId;
    private Double unitValue;
    private Integer quantity;

    public InvoiceItemDTO(Long id, Long invoiceId, Long productId, Double unitValue, Integer quantity) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.productId = productId;
        this.unitValue = unitValue;
        this.quantity = quantity;
    }

    public InvoiceItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getUnitValue() {
        return unitValue;
    }

    public void setUnitValue(Double unitValue) {
        this.unitValue = unitValue;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}