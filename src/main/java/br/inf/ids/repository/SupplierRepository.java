package br.inf.ids.repository;

import br.inf.ids.model.Supplier;
import br.inf.ids.model.enums.CompanyStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SupplierRepository implements PanacheRepository<Supplier> {

    public List<Supplier> findByCompanyStatus(CompanyStatus companyStatus) {
        return list("companyStatus", companyStatus);
    }

    public Supplier findByCnpj(String cnpj) {
        return find("cnpj", cnpj).firstResult();
    }
}