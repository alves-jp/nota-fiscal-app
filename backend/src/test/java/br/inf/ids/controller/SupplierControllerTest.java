package br.inf.ids.controller;

import br.inf.ids.dto.SupplierDTO;
import br.inf.ids.service.SupplierService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupplierControllerTest {

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSupplier_Success() {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCompanyName("Fornecedora Teste");

        when(supplierService.createSupplier(supplierDTO)).thenReturn(supplierDTO);
        
        Response response = supplierController.createSupplier(supplierDTO);
        
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(supplierDTO, response.getEntity());
        verify(supplierService).createSupplier(supplierDTO);
    }

    @Test
    void testCreateSupplier_Failure() {
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCompanyName("Fornecedora Teste");

        when(supplierService.createSupplier(supplierDTO)).thenThrow(new RuntimeException("Erro ao criar novo fornecedor"));

        Response response = supplierController.createSupplier(supplierDTO);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Erro ao criar novo fornecedor", response.getEntity());
    }

    @Test
    void testGetSupplierById_Success() {
        Long supplierId = 1L;
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCompanyName("Fornecedora Teste");
        when(supplierService.findSupplierById(supplierId)).thenReturn(supplierDTO);

        Response response = supplierController.getSupplierById(supplierId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(supplierDTO, response.getEntity());
        verify(supplierService).findSupplierById(supplierId);
    }

    @Test
    void testGetSupplierById_NotFound() {
        Long supplierId = 1L;
        when(supplierService.findSupplierById(supplierId)).thenThrow(new RuntimeException("Fornecedor não encontrado"));

        Response response = supplierController.getSupplierById(supplierId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Fornecedor não encontrado", response.getEntity());
    }

    @Test
    void testGetAllSuppliers() {
        List<SupplierDTO> suppliers = Arrays.asList(
                new SupplierDTO(),
                new SupplierDTO()
        );
        when(supplierService.findAllSuppliers()).thenReturn(suppliers);

        Response response = supplierController.getAllSuppliers();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(suppliers, response.getEntity());
        verify(supplierService).findAllSuppliers();
    }

    @Test
    void testGetSupplierByName_Success() {
        String companyName = "Fornecedora Teste";
        List<SupplierDTO> suppliers = Arrays.asList(
                new SupplierDTO(),
                new SupplierDTO()
        );
        when(supplierService.findSuppliersByName(companyName)).thenReturn(suppliers);

        Response response = supplierController.getSupplierByName(companyName);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(suppliers, response.getEntity());
        verify(supplierService).findSuppliersByName(companyName);
    }

    @Test
    void testGetSupplierByName_Failure() {
        String companyName = "Fornecedora Teste";
        when(supplierService.findSuppliersByName(companyName)).thenThrow(new RuntimeException("Erro na pesquisa"));

        Response response = supplierController.getSupplierByName(companyName);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Erro na pesquisa", response.getEntity());
    }

    @Test
    void testUpdateSupplier_Success() {
        Long supplierId = 1L;
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCompanyName("Fornecedora Nova");

        SupplierDTO updatedSupplier = new SupplierDTO();
        updatedSupplier.setCompanyName("Fornecedora Nova");
        when(supplierService.updateSupplier(supplierId, supplierDTO)).thenReturn(updatedSupplier);

        Response response = supplierController.updateSupplier(supplierId, supplierDTO);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(updatedSupplier, response.getEntity());
        verify(supplierService).updateSupplier(supplierId, supplierDTO);
    }

    @Test
    void testUpdateSupplier_Failure() {
        Long supplierId = 1L;
        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setCompanyName("Fornecedora Nova");

        when(supplierService.updateSupplier(supplierId, supplierDTO)).thenThrow(new RuntimeException("Erro ao atualizar"));

        Response response = supplierController.updateSupplier(supplierId, supplierDTO);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Erro ao atualizar", response.getEntity());
    }

    @Test
    void testDeleteSupplier_Success() {
        Long supplierId = 1L;
        doNothing().when(supplierService).deleteSupplier(supplierId);

        Response response = supplierController.deleteSupplier(supplierId);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(supplierService).deleteSupplier(supplierId);
    }

    @Test
    void testDeleteSupplier_Failure() {
        Long supplierId = 1L;
        doThrow(new RuntimeException("Erro ao deletar fornecedor")).when(supplierService).deleteSupplier(supplierId);

        Response response = supplierController.deleteSupplier(supplierId);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Erro ao deletar fornecedor", response.getEntity());
    }
}