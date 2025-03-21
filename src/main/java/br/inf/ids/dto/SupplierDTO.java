package br.inf.ids.dto;

import br.inf.ids.model.enums.CompanyStatus;
import java.time.LocalDate;

public class SupplierDTO {

    private Long id;
    private String companyName;
    private String supplierEmail;
    private String supplierPhone;
    private String cnpj;
    private CompanyStatus companyStatus;
    private LocalDate companyDeactivationDate;
    
    public SupplierDTO(Long id, String companyName, String supplierEmail, String supplierPhone, String cnpj, CompanyStatus companyStatus, LocalDate companyDeactivationDate) {
        this.id = id;
        this.companyName = companyName;
        this.supplierEmail = supplierEmail;
        this.supplierPhone = supplierPhone;
        this.cnpj = cnpj;
        this.companyStatus = companyStatus;
        this.companyDeactivationDate = companyDeactivationDate;
    }

    public SupplierDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getsupplierEmail() {
        return supplierEmail;
    }

    public void setsupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public String getsupplierPhone() {
        return supplierPhone;
    }

    public void setsupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public CompanyStatus getCompanyStatus() {
        return companyStatus;
    }

    public void setCompanyStatus(CompanyStatus companyStatus) {
        this.companyStatus = companyStatus;
    }

    public LocalDate getcompanyDeactivationDate() {
        return companyDeactivationDate;
    }

    public void setcompanyDeactivationDate(LocalDate companyDeactivationDate) {
        this.companyDeactivationDate = companyDeactivationDate;
    }
}