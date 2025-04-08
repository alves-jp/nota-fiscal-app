package br.inf.ids.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import br.inf.ids.dto.SupplierDTO;
import br.inf.ids.exception.BusinessException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.model.Supplier;
import br.inf.ids.model.enums.CompanyStatus;
import br.inf.ids.repository.InvoiceRepository;
import br.inf.ids.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.util.Optional;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private SupplierDTO supplierDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        supplierDTO = new SupplierDTO();
        supplierDTO.setSupplierCode("F-12345");
        supplierDTO.setCompanyName("Fornecedora Teste");
        supplierDTO.setsupplierEmail("teste@mail.com");
        supplierDTO.setsupplierPhone("123456789");
        supplierDTO.setCnpj("12345678000195");
        supplierDTO.setCompanyStatus(CompanyStatus.valueOf("ACTIVE"));
    }

    @Test
    public void testCreateSupplier_Success() {
        when(supplierRepository.findByCnpj(supplierDTO.getCnpj())).thenReturn(null);
        doNothing().when(supplierRepository).persist(any(Supplier.class));

        SupplierDTO createdSupplier = supplierService.createSupplier(supplierDTO);

        assertNotNull(createdSupplier);
        assertEquals("F-12345", createdSupplier.getSupplierCode());
        assertEquals("Fornecedora Teste", createdSupplier.getCompanyName());
    }

    @Test
    public void testCreateSupplier_Failure_CnpjExists() {
        when(supplierRepository.findByCnpj(supplierDTO.getCnpj())).thenReturn(new Supplier());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.createSupplier(supplierDTO);
        });

        assertEquals("Erro: Já existe um fornecedor com o CNPJ informado.", exception.getMessage());
    }

    @Test
    public void testUpdateSupplier_Success() {
        Supplier existingSupplier = new Supplier();
        existingSupplier.setId(1L);
        existingSupplier.setSupplierCode("F-12345");
        existingSupplier.setCompanyName("Fornecedora Teste");
        existingSupplier.setsupplierEmail("fornecedor.teste@mail.com");
        existingSupplier.setsupplierPhone("1234567890");
        existingSupplier.setCnpj("12345678000195");
        existingSupplier.setCompanyStatus(CompanyStatus.ACTIVE);
        when(supplierRepository.findByIdOptional(1L)).thenReturn(Optional.of(existingSupplier));
        doNothing().when(supplierRepository).persist(any(Supplier.class));

        supplierDTO.setSupplierCode("S54321");
        supplierDTO.setCompanyName("Fornecedora Nova");
        supplierDTO.setsupplierEmail("fornecedora.nova@mail.com");
        supplierDTO.setsupplierPhone("9876543210");
        supplierDTO.setCnpj("12345678000195");
        supplierDTO.setCompanyStatus(CompanyStatus.INACTIVE);
        SupplierDTO updatedSupplier = supplierService.updateSupplier(1L, supplierDTO);

        assertNotNull(updatedSupplier);
        assertEquals("S54321", updatedSupplier.getSupplierCode());
        assertEquals("Fornecedora Nova", updatedSupplier.getCompanyName());
        assertEquals("fornecedora.nova@mail.com", updatedSupplier.getsupplierEmail());
        assertEquals("9876543210", updatedSupplier.getsupplierPhone());
        assertEquals("12345678000195", updatedSupplier.getCnpj());
        assertEquals(CompanyStatus.INACTIVE, updatedSupplier.getCompanyStatus());
    }

    @Test
    public void testDeleteSupplier_Failure_WithMovements() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        when(supplierRepository.findByIdOptional(1L)).thenReturn(Optional.of(supplier));
        when(invoiceRepository.count("supplier.id", 1L)).thenReturn(1L);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.deleteSupplier(1L);
        });

        assertEquals("Erro: Não é possível excluir um fornecedor com movimentação.", exception.getMessage());
    }

    @Test
    public void testDeleteSupplier_Failure_SupplierNotFound() {
        when(supplierRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            supplierService.deleteSupplier(1L);
        });

        assertEquals("Fornecedor não encontrado.", exception.getMessage());
    }
}
