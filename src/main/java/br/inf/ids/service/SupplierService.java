package br.inf.ids.service;

import br.inf.ids.model.Supplier;
import br.inf.ids.model.enums.CompanyStatus;
import java.util.List;
import java.util.Optional;

public interface SupplierService {

    Supplier createSupplier(Supplier supplier);

    Optional<Supplier> findSupplierById(Long id);

    List<Supplier> findAllSuppliers();

    List<Supplier> findSuppliersByStatus(CompanyStatus status);

    Supplier updateSupplier(Long id, Supplier supplier);

    void deleteSupplier(Long id);

    Optional<Supplier> findSupplierByCnpj(String cnpj);

    List<Supplier> searchSuppliers(String searchTerm);
}