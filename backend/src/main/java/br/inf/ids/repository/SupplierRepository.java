package br.inf.ids.repository;

import br.inf.ids.model.Product;
import br.inf.ids.model.Supplier;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SupplierRepository implements PanacheRepository<Supplier> {

    public List<Supplier> findByCompanyName(String companyName) {
        return list("LOWER(companyName) LIKE LOWER(?1)", "%" + companyName + "%");
    }

    public List<Supplier> findByCode(String supplierCode) {
        return list("supplierCode = ?1", supplierCode.trim());
    }

    public Supplier findByCnpj(String cnpj) {
        return find("cnpj", cnpj).firstResult();
    }

    public boolean hasInvoices(Long supplierId) {
        return count("supplier.id", supplierId) > 0;
    }
}