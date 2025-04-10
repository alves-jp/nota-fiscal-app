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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SupplierServiceImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private SupplierDTO supplierDTO;
    private Supplier supplierEntity;

    @BeforeEach
    public void setUp() {
        supplierDTO = new SupplierDTO();
        supplierDTO.setId(1L);
        supplierDTO.setSupplierCode("F-00001");
        supplierDTO.setCompanyName("Fornecedora Teste");
        supplierDTO.setsupplierEmail("fornecedora.01@mail.com");
        supplierDTO.setsupplierPhone("111111111");
        supplierDTO.setCnpj("11111111000111");
        supplierDTO.setCompanyStatus(CompanyStatus.ACTIVE);
        supplierDTO.setCompanyDeactivationDate(null);

        supplierEntity = new Supplier();
        supplierEntity.setId(1L);
        supplierEntity.setSupplierCode("F-00002");
        supplierEntity.setCompanyName("Teste Empreitadas");
        supplierEntity.setsupplierEmail("teste.empreitadas@gmail.com");
        supplierEntity.setsupplierPhone("222222222");
        supplierEntity.setCnpj("22222222000122");
        supplierEntity.setCompanyStatus(CompanyStatus.ACTIVE);
        supplierEntity.setcompanyDeactivationDate(null);
    }

    @Test
    public void createSupplier_Success() {
        SupplierDTO dtoToCreate = new SupplierDTO();
        dtoToCreate.setSupplierCode("F-00003");
        dtoToCreate.setCompanyName("Nova Fornecedora");
        dtoToCreate.setCnpj("33333333000133");
        dtoToCreate.setCompanyStatus(CompanyStatus.ACTIVE);
        dtoToCreate.setsupplierEmail("nova.fornecedora@mail.com");
        dtoToCreate.setsupplierPhone("333333333");

        when(supplierRepository.findByCnpj(dtoToCreate.getCnpj())).thenReturn(null);
        when(supplierRepository.findByCode(dtoToCreate.getSupplierCode())).thenReturn(Collections.emptyList());
        doAnswer(invocation -> {
            Supplier persisted = invocation.getArgument(0);
            persisted.setId(5L); // Simulate ID generation
            return null;
        }).when(supplierRepository).persist(any(Supplier.class));

        SupplierDTO createdSupplier = supplierService.createSupplier(dtoToCreate);

        assertNotNull(createdSupplier);
        assertEquals(5L, createdSupplier.getId());
        assertEquals("F-00003", createdSupplier.getSupplierCode());
        assertEquals("Nova Fornecedora", createdSupplier.getCompanyName());
        assertEquals("33333333000133", createdSupplier.getCnpj());
        verify(supplierRepository).persist(any(Supplier.class));
    }

    @Test
    public void createSupplier_Failure_CnpjExists() {
        when(supplierRepository.findByCnpj(supplierDTO.getCnpj())).thenReturn(new Supplier());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.createSupplier(supplierDTO);
        });

        assertEquals("Já existe um fornecedor com o CNPJ informado.", exception.getMessage());
        verify(supplierRepository, never()).persist(any(Supplier.class));
    }

    @Test
    public void createSupplier_Failure_CodeExists() {
        when(supplierRepository.findByCnpj(supplierDTO.getCnpj())).thenReturn(null);
        when(supplierRepository.findByCode(supplierDTO.getSupplierCode())).thenReturn(List.of(new Supplier()));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.createSupplier(supplierDTO);
        });

        assertEquals("Já existe um fornecedor cadastrado com o código informado.", exception.getMessage());
        verify(supplierRepository, never()).persist(any(Supplier.class));
    }

    @Test
    public void createSupplier_Failure_CompanyNameBlank() {
        supplierDTO.setCompanyName("");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.createSupplier(supplierDTO);
        });

        assertEquals("A razão social do fornecedor é obrigatória.", exception.getMessage());
        verify(supplierRepository, never()).persist(any(Supplier.class));
    }

    @Test
    public void createSupplier_Failure_CnpjBlank() {
        supplierDTO.setCnpj(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.createSupplier(supplierDTO);
        });

        assertEquals("O CNPJ é obrigatório.", exception.getMessage());
        verify(supplierRepository, never()).persist(any(Supplier.class));
    }

    @Test
    public void createSupplier_Failure_StatusNull() {
        supplierDTO.setCompanyStatus(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.createSupplier(supplierDTO);
        });
        assertEquals("A situação da empresa é obrigatória.", exception.getMessage());
        verify(supplierRepository, never()).persist(any(Supplier.class));
    }

    @Test
    public void createSupplier_Failure_SupplierCodeNull() {
        supplierDTO.setSupplierCode(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.createSupplier(supplierDTO);
        });

        assertEquals("O código do fornecedor é obrigatório.", exception.getMessage());
        verify(supplierRepository, never()).persist(any(Supplier.class));
    }

    @Test
    public void createSupplier_Failure_InactiveStatus() {
        supplierDTO.setCompanyStatus(CompanyStatus.INACTIVE);
        supplierDTO.setCompanyDeactivationDate(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.createSupplier(supplierDTO);
        });
        assertEquals("A data da desativação é obrigatória quando a situação da empresa estiver como Baixada.", exception.getMessage());
        verify(supplierRepository, never()).persist(any(Supplier.class));
    }

    @Test
    public void findSupplierById_Success() {
        when(supplierRepository.findByIdOptional(1L)).thenReturn(Optional.of(supplierEntity));

        SupplierDTO foundSupplier = supplierService.findSupplierById(1L);

        assertNotNull(foundSupplier);
        assertEquals(supplierEntity.getId(), foundSupplier.getId());
        assertEquals(supplierEntity.getCompanyName(), foundSupplier.getCompanyName());
    }

    @Test
    public void findSupplierById_Failure() {
        when(supplierRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            supplierService.findSupplierById(99L);
        });

        assertEquals("Fornecedor não encontrado.", exception.getMessage());
    }

    @Test
    public void findAllSuppliers_Success() {
        when(supplierRepository.listAll()).thenReturn(List.of(supplierEntity));

        List<SupplierDTO> suppliers = supplierService.findAllSuppliers();

        assertNotNull(suppliers);
        assertEquals(1, suppliers.size());
        assertEquals(supplierEntity.getId(), suppliers.get(0).getId());
    }

    @Test
    public void findAllSuppliers_Success_EmptyList() {
        when(supplierRepository.listAll()).thenReturn(Collections.emptyList());

        List<SupplierDTO> suppliers = supplierService.findAllSuppliers();

        assertNotNull(suppliers);
        assertTrue(suppliers.isEmpty());
    }

    @Test
    public void findSuppliersByName_Success() {
        String name = "Fornecedora Teste";
        when(supplierRepository.findByCompanyName(name)).thenReturn(List.of(supplierEntity));

        List<SupplierDTO> suppliers = supplierService.findSuppliersByName(name);

        assertNotNull(suppliers);
        assertEquals(1, suppliers.size());
        assertEquals(supplierEntity.getId(), suppliers.get(0).getId());
        assertEquals(supplierEntity.getCompanyName(), suppliers.get(0).getCompanyName());
    }

    @Test
    public void findSuppliersByName_Failure_BlankName() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.findSuppliersByName(" ");
        });

        assertEquals("A razão social do fornecedor é obrigatória para a busca.", exception.getMessage());
    }

    @Test
    public void findSuppliersByName_Success_NotFound() {
        String name = "Fornecedora Inexistente";

        when(supplierRepository.findByCompanyName(name)).thenReturn(Collections.emptyList());
        List<SupplierDTO> suppliers = supplierService.findSuppliersByName(name);

        assertNotNull(suppliers);
        assertTrue(suppliers.isEmpty());
    }

    @Test
    public void updateSupplier_Success() {
        Long supplierId = 1L;
        String existingCnpj = "11111111000111";
        Supplier existingSupplier = new Supplier();
        existingSupplier.setId(supplierId);
        existingSupplier.setSupplierCode("F-OLD");
        existingSupplier.setCompanyName("Fornecedora Antiga");
        existingSupplier.setCnpj(existingCnpj);
        existingSupplier.setCompanyStatus(CompanyStatus.ACTIVE);
        when(supplierRepository.findByIdOptional(supplierId)).thenReturn(Optional.of(existingSupplier));
        when(supplierRepository.findByCnpj(existingCnpj)).thenReturn(existingSupplier);

        SupplierDTO dtoToUpdate = new SupplierDTO();
        dtoToUpdate.setId(supplierId);
        dtoToUpdate.setSupplierCode("F-NOVA");
        dtoToUpdate.setCompanyName("Fornecedora Atualizada");
        dtoToUpdate.setCnpj(existingCnpj);
        dtoToUpdate.setCompanyStatus(CompanyStatus.INACTIVE);
        dtoToUpdate.setCompanyDeactivationDate(LocalDate.now());
        dtoToUpdate.setsupplierEmail("novo@mail.com");
        dtoToUpdate.setsupplierPhone("999999999");
        SupplierDTO updatedSupplier = supplierService.updateSupplier(supplierId, dtoToUpdate);

        assertNotNull(updatedSupplier);
        assertEquals(supplierId, updatedSupplier.getId());
        assertEquals("F-NOVA", updatedSupplier.getSupplierCode());
        assertEquals("Fornecedora Atualizada", updatedSupplier.getCompanyName());
        assertEquals(existingCnpj, updatedSupplier.getCnpj());
        assertEquals(CompanyStatus.INACTIVE, updatedSupplier.getCompanyStatus());
        assertNotNull(updatedSupplier.getCompanyDeactivationDate());
        assertEquals("novo@mail.com", updatedSupplier.getsupplierEmail());
        verify(supplierRepository, never()).persist(any(Supplier.class));
    }

    @Test
    public void updateSupplier_Failure_NotFound() {
        Long supplierId = 99L;
        when(supplierRepository.findByIdOptional(supplierId)).thenReturn(Optional.empty());
        SupplierDTO validDto = new SupplierDTO();
        validDto.setSupplierCode("F-00004");
        validDto.setCompanyName("Fornecedora Valida");
        validDto.setCnpj("44444444000144");
        validDto.setCompanyStatus(CompanyStatus.ACTIVE);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            supplierService.updateSupplier(supplierId, validDto);
        });

        assertEquals("Fornecedor não encontrado.", exception.getMessage());
        verify(supplierRepository, never()).persist(any(Supplier.class));
    }

    @Test
    public void updateSupplier_Failure_CnpjError() {
        Long supplierId = 1L;
        String newCnpj = "55555555000155";
        Supplier existingSupplier = new Supplier();
        existingSupplier.setId(supplierId);
        existingSupplier.setCnpj("11111111000111");
        existingSupplier.setCompanyName("Fornecedora A");
        existingSupplier.setCompanyStatus(CompanyStatus.ACTIVE);
        existingSupplier.setSupplierCode("F-A");
        Supplier otherSupplier = new Supplier();
        otherSupplier.setId(2L);
        otherSupplier.setCnpj(newCnpj);
        when(supplierRepository.findByIdOptional(supplierId)).thenReturn(Optional.of(existingSupplier));
        when(supplierRepository.findByCnpj(newCnpj)).thenReturn(otherSupplier);

        SupplierDTO dtoToUpdate = new SupplierDTO();
        dtoToUpdate.setId(supplierId);
        dtoToUpdate.setSupplierCode("F-A2");
        dtoToUpdate.setCompanyName("Fornecedora A Nova");
        dtoToUpdate.setCnpj(newCnpj);
        dtoToUpdate.setCompanyStatus(CompanyStatus.ACTIVE);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.updateSupplier(supplierId, dtoToUpdate);
        });
        assertEquals("Já existe um fornecedor com este CNPJ.", exception.getMessage());
        verify(supplierRepository, never()).persist(any(Supplier.class));
    }

    @Test
    public void updateSupplier_Failure_CompanyNameBlank() {
        Long supplierId = 1L;
        supplierDTO.setCompanyName("");
        
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.updateSupplier(supplierId, supplierDTO);
        });
        
        assertEquals("A razão social do fornecedor é obrigatória.", exception.getMessage());
        verify(supplierRepository, never()).findByIdOptional(anyLong());
        verify(supplierRepository, never()).persist(any(Supplier.class));
    }


    @Test
    public void deleteSupplier_Success() {
        Long supplierId = 1L;
        when(supplierRepository.findByIdOptional(supplierId)).thenReturn(Optional.of(supplierEntity));
        when(invoiceRepository.count("supplier.id", supplierId)).thenReturn(0L);
        doNothing().when(supplierRepository).delete(any(Supplier.class));

        assertDoesNotThrow(() -> {
            supplierService.deleteSupplier(supplierId);
        });

        verify(supplierRepository).findByIdOptional(supplierId);
        verify(invoiceRepository).count("supplier.id", supplierId);
        verify(supplierRepository).delete(supplierEntity);
    }

    @Test
    public void deleteSupplier_Failure() {
        Long supplierId = 1L;
        when(supplierRepository.findByIdOptional(supplierId)).thenReturn(Optional.of(supplierEntity));
        when(invoiceRepository.count("supplier.id", supplierId)).thenReturn(1L);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            supplierService.deleteSupplier(supplierId);
        });

        assertEquals("Não é possível excluir um fornecedor com movimentação.", exception.getMessage());
        verify(supplierRepository).findByIdOptional(supplierId);
        verify(invoiceRepository).count("supplier.id", supplierId);
        verify(supplierRepository, never()).delete(any(Supplier.class));
    }

    @Test
    public void deleteSupplier_Failure_NotFound() {
        Long supplierId = 99L;
        when(supplierRepository.findByIdOptional(supplierId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            supplierService.deleteSupplier(supplierId);
        });

        assertEquals("Fornecedor não encontrado.", exception.getMessage());
        verify(invoiceRepository, never()).count(anyString(), anyLong());
        verify(supplierRepository, never()).delete(any(Supplier.class));
    }
}