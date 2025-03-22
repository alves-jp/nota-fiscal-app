package br.inf.ids.service.impl;

import br.inf.ids.model.Supplier;
import br.inf.ids.model.enums.CompanyStatus;
import br.inf.ids.repository.SupplierRepository;
import br.inf.ids.service.SupplierService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SupplierServiceImpl implements SupplierService {

    @Inject
    SupplierRepository supplierRepository;

    @Override
    @Transactional
    public Supplier createSupplier(Supplier supplier) {
        if (supplierRepository.findByCnpj(supplier.getCnpj()) != null) {
            throw new IllegalArgumentException("Erro: Já existe um fornecedor com o CNPJ informado.");

        }
        supplierRepository.persist(supplier);

        return supplier;
    }

    @Override
    public Optional<Supplier> findSupplierById(Long id) {
        return supplierRepository.findByIdOptional(id);
    }

    @Override
    public List<Supplier> findAllSuppliers() {
        return supplierRepository.listAll();
    }

    @Override
    public List<Supplier> findSupplierByStatus(CompanyStatus status) {
        return supplierRepository.findByCompanyStatus(status);
    }

    @Override
    public Optional<Supplier> findSupplierByCnpj(String cnpj) {
        return Optional.ofNullable(supplierRepository.findByCnpj(cnpj));
    }

    @Override
    public List<Supplier> findSuppliersByName(String companyName) {
        return supplierRepository.findByCompanyName(companyName);
    }

    @Override
    @Transactional
    public Supplier updateSupplier(Long id, Supplier supplier) {
        Supplier existingSupplier = supplierRepository.findById(id);

        if (existingSupplier != null) {
            Supplier supplierWithSameCnpj = supplierRepository.findByCnpj(supplier.getCnpj());

            if (supplierWithSameCnpj != null && !supplierWithSameCnpj.getId().equals(id)) {
                throw new IllegalArgumentException("Erro: Já existe um fornecedor com este CNPJ.");

            }
            existingSupplier.setCompanyName(supplier.getCompanyName());
            existingSupplier.setsupplierEmail(supplier.getsupplierEmail());
            existingSupplier.setsupplierPhone(supplier.getsupplierPhone());
            existingSupplier.setCnpj(supplier.getCnpj());
            existingSupplier.setCompanyStatus(supplier.getCompanyStatus());

        }
        return existingSupplier;
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id);

        if (supplier != null) {
            if (supplierRepository.hasInvoices(id)) {
                throw new IllegalStateException("Erro: Não é possível excluir um fornecedor com movimentação.");

            }
            supplierRepository.delete(supplier);
        }
    }
}