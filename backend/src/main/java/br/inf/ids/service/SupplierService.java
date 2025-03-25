package br.inf.ids.service;

import br.inf.ids.dto.SupplierDTO;
import br.inf.ids.model.Supplier;
import jakarta.transaction.Transactional;
import java.util.List;

public interface SupplierService {

    @Transactional
    SupplierDTO createSupplier(SupplierDTO supplierDTO);

    SupplierDTO findSupplierById(Long id);

    List<SupplierDTO> findAllSuppliers();

    List<SupplierDTO> findSuppliersByName(String companyName);

    @Transactional
    SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO);

    @Transactional
    void deleteSupplier(Long id);
}
