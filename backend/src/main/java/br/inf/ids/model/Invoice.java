package br.inf.ids.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.json.bind.annotation.JsonbTransient;
import java.util.List;

@Entity
@Table(name = "notas_fiscais")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_nf", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "data_emissao", nullable = false)
    private LocalDateTime issueDate;

    @ManyToOne
    @JoinColumn(name = "codigo_fornecedor", nullable = false)
    private Supplier supplier;

    @Column(name = "endereco", nullable = false)
    private String address;

    @Column(name = "valor_total", nullable = false)
    private Double totalValue;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonbTransient
    private List<InvoiceItem> items;


    public Invoice(String invoiceNumber, LocalDateTime issueDate, Supplier supplier, String address, Double totalValue, List<InvoiceItem> items) {
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.supplier = supplier;
        this.address = address;
        this.totalValue = totalValue;
        this.items = items;
    }

    public Invoice() {
    }

    public Long getId() {
        return id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }
}