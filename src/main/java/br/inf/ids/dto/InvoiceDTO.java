package br.inf.ids.dto;

import java.time.LocalDateTime;

public class InvoiceDTO {

    private Long id;
    private String invoiceNumber;
    private LocalDateTime issueDate;
    private Long supplierId;
    private String address;
    private Double totalValue;

    public InvoiceDTO(Long id, String invoiceNumber, LocalDateTime issueDate, Long supplierId, String address, Double totalValue) {
        this.id = id;
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.supplierId = supplierId;
        this.address = address;
        this.totalValue = totalValue;
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

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
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
}