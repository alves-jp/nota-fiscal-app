package br.inf.ids.model;

import br.inf.ids.model.enums.CompanyStatus;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "fornecedores")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_fornecedor")
    private Long id;

    @Column(name = "razao_social", nullable = false)
    private String companyName;

    @Column(name = "email", nullable = false)
    private String supplierEmail;

    @Column(name = "telefone", nullable = false)
    private String supplierPhone;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(name = "situacao_fornecedor", nullable = false)
    @Enumerated(EnumType.STRING)
    private CompanyStatus companyStatus;

    @Column(name = "data_da_baixa")
    private LocalDate companyDeactivationDate;


    public Supplier(String companyName, String supplierEmail, String supplierPhone,
                    String cnpj, CompanyStatus companyStatus, LocalDate companyDeactivationDate) {
        this.companyName = companyName;
        this.supplierEmail = supplierEmail;
        this.supplierPhone = supplierPhone;
        this.cnpj = cnpj;
        this.companyStatus = companyStatus;
        this.companyDeactivationDate = companyDeactivationDate;
    }

    public Supplier() {
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