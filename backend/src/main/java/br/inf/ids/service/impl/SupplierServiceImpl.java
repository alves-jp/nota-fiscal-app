package br.inf.ids.service.impl;

import br.inf.ids.dto.SupplierDTO;
import br.inf.ids.exception.BusinessException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.model.Supplier;
import br.inf.ids.model.enums.CompanyStatus;
import br.inf.ids.repository.InvoiceRepository;
import br.inf.ids.repository.SupplierRepository;
import br.inf.ids.service.SupplierService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SupplierServiceImpl implements SupplierService {

    @Inject
    SupplierRepository supplierRepository;

    @Inject
    InvoiceRepository invoiceRepository;

    @Transactional
    @Override
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        validateSupplierDTO(supplierDTO);

        Supplier supplier = new Supplier();
        supplier.setId(supplierDTO.getId());
        supplier.setSupplierCode(supplierDTO.getSupplierCode());
        supplier.setCompanyName(supplierDTO.getCompanyName());
        supplier.setsupplierEmail(supplierDTO.getsupplierEmail());
        supplier.setsupplierPhone(supplierDTO.getsupplierPhone());
        supplier.setCnpj(supplierDTO.getCnpj());
        supplier.setCompanyStatus(supplierDTO.getCompanyStatus());
        supplier.setcompanyDeactivationDate(supplierDTO.getCompanyDeactivationDate());

        if (supplierRepository.findByCnpj(supplier.getCnpj()) != null) {
            throw new BusinessException("Erro: Já existe um fornecedor com o CNPJ informado.");
        }

        supplierRepository.persist(supplier);
        return mapToDTO(supplier);
    }

    @Override
    public SupplierDTO findSupplierById(Long id) {
        Supplier supplier = supplierRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado."));

        return mapToDTO(supplier);
    }

    @Override
    public List<SupplierDTO> findAllSuppliers() {
        return supplierRepository.listAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SupplierDTO> findSuppliersByName(String companyName) {
        if (companyName == null || companyName.isBlank()) {
            throw new BusinessException("A razão social do fornecedor é obrigatória para a busca.");

        }
        return supplierRepository.findByCompanyName(companyName).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        validateSupplierDTO(supplierDTO);

        Supplier existingSupplier = supplierRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado."));

        Supplier supplierWithSameCnpj = supplierRepository.findByCnpj(supplierDTO.getCnpj());

        if (supplierWithSameCnpj != null && !supplierWithSameCnpj.getId().equals(id)) {
            throw new BusinessException("Erro: Já existe um fornecedor com este CNPJ.");
        }

        existingSupplier.setId(supplierDTO.getId());
        existingSupplier.setSupplierCode(supplierDTO.getSupplierCode());
        existingSupplier.setCompanyName(supplierDTO.getCompanyName());
        existingSupplier.setsupplierEmail(supplierDTO.getsupplierEmail());
        existingSupplier.setsupplierPhone(supplierDTO.getsupplierPhone());
        existingSupplier.setCnpj(supplierDTO.getCnpj());
        existingSupplier.setCompanyStatus(supplierDTO.getCompanyStatus());
        existingSupplier.setcompanyDeactivationDate(supplierDTO.getCompanyDeactivationDate());

        return mapToDTO(existingSupplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Fornecedor não encontrado."));

        boolean hasMovements = invoiceRepository.count("supplier.id", id) > 0;

        if (hasMovements) {
            throw new BusinessException("Erro: Não é possível excluir um fornecedor com movimentação.");
        }

        supplierRepository.delete(supplier);
    }

    private void validateSupplierDTO(SupplierDTO supplierDTO) {
        if (supplierDTO.getCompanyName() == null || supplierDTO.getCompanyName().isBlank()) {
            throw new BusinessException("A razão social do fornecedor é obrigatória.");

        } if (supplierDTO.getCnpj() == null || supplierDTO.getCnpj().isBlank()) {
            throw new BusinessException("O CNPJ é obrigatório.");

        } if (supplierDTO.getCompanyStatus() == null) {
            throw new BusinessException("O status da empresa é obrigatório.");

        } if (supplierDTO.getSupplierCode() == null) {
            throw new BusinessException("O código do fornecedor é obrigatório.");

        } if (supplierDTO.getCompanyStatus() == CompanyStatus.INACTIVE &&
                supplierDTO.getCompanyDeactivationDate() == null) {
            throw new BusinessException("A data de desativação é obrigatória quando o status é INACTIVE.");
        }
    }

    private SupplierDTO mapToDTO(Supplier supplier) {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setId(supplier.getId());
        supplierDTO.setSupplierCode(supplier.getSupplierCode());
        supplierDTO.setCompanyName(supplier.getCompanyName());
        supplierDTO.setsupplierEmail(supplier.getsupplierEmail());
        supplierDTO.setsupplierPhone(supplier.getsupplierPhone());
        supplierDTO.setCnpj(supplier.getCnpj());
        supplierDTO.setCompanyStatus(supplier.getCompanyStatus());
        supplierDTO.setCompanyDeactivationDate(supplier.getcompanyDeactivationDate());

        return supplierDTO;
    }
}
