package br.inf.ids.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nota_fiscal_itens")
public class InvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nota_fiscal_id", nullable = false)
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "codigo_produto", nullable = false)
    private Product product;

    @Column(name = "valor_unitario", nullable = false)
    private Double unitValue;

    @Column(name = "quantidade", nullable = false)
    private Integer quantity;


    public InvoiceItem(Invoice invoice, Product product, Double unitValue, Integer quantity) {
        this.invoice = invoice;
        this.product = product;
        this.unitValue = unitValue;
        this.quantity = quantity;
    }

    public InvoiceItem() {
    }

    public Long getId() {
        return id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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