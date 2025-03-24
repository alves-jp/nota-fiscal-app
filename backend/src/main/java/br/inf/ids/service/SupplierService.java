package br.inf.ids.service;

import br.inf.ids.dto.SupplierDTO;
import br.inf.ids.model.Supplier;
import jakarta.transaction.Transactional;
import java.util.List;

public interface SupplierService {

    @Transactional
    Supplier createSupplier(SupplierDTO supplierDTO);

    Supplier findSupplierById(Long id);

    List<Supplier> findAllSuppliers();

    List<Supplier> findSuppliersByName(String companyName);

    @Transactional
    Supplier updateSupplier(Long id, SupplierDTO supplierDTO);

    @Transactional
    void deleteSupplier(Long id);
}