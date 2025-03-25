package br.inf.ids.controller;

import br.inf.ids.dto.InvoiceDTO;
import br.inf.ids.dto.InvoiceResponseDTO;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.exception.InvalidDataException;
import br.inf.ids.model.Invoice;
import br.inf.ids.service.InvoiceService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInvoice_Success() {
        InvoiceDTO invoiceDTO = createSampleInvoiceDTO();
        Invoice expectedInvoice = new Invoice();
        expectedInvoice.setId(1L);
        expectedInvoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        when(invoiceService.createInvoice(invoiceDTO)).thenReturn(expectedInvoice);

        Response response = invoiceController.createInvoice(invoiceDTO);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(expectedInvoice, response.getEntity());
        verify(invoiceService).createInvoice(invoiceDTO);
    }

    @Test
    void testCreateInvoice_InvalidData() {
        InvoiceDTO invoiceDTO = createSampleInvoiceDTO();
        when(invoiceService.createInvoice(invoiceDTO))
                .thenThrow(new InvalidDataException("Nota fiscal inválida"));

        Response response = invoiceController.createInvoice(invoiceDTO);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Nota fiscal inválida", response.getEntity());
    }

    @Test
    void testGetInvoiceById_Success() {
        Long invoiceId = 1L;
        InvoiceResponseDTO responseDTO = new InvoiceResponseDTO();
        responseDTO.setId(invoiceId);
        responseDTO.setInvoiceNumber("NF-001");

        when(invoiceService.getInvoiceById(invoiceId)).thenReturn(responseDTO);

        Response response = invoiceController.getInvoiceById(invoiceId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(responseDTO, response.getEntity());
        verify(invoiceService).getInvoiceById(invoiceId);
    }

    @Test
    void testGetInvoiceById_NotFound() {
        Long invoiceId = 1L;
        when(invoiceService.getInvoiceById(invoiceId))
                .thenThrow(new EntityNotFoundException("Nota fiscal não foi encontrada"));

        Response response = invoiceController.getInvoiceById(invoiceId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nota fiscal não foi encontrada", response.getEntity());
    }

    @Test
    void testGetAllInvoices_Success() {
        List<InvoiceResponseDTO> invoiceList = Arrays.asList(
                new InvoiceResponseDTO(),
                new InvoiceResponseDTO()
        );
        when(invoiceService.findAllInvoices()).thenReturn(invoiceList);

        Response response = invoiceController.getAllInvoices();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(invoiceList, response.getEntity());
        verify(invoiceService).findAllInvoices();
    }

    @Test
    void testGetInvoiceByNumber_Success() {
        String invoiceNumber = "NF-001";
        List<Invoice> invoices = Arrays.asList(new Invoice(), new Invoice());
        when(invoiceService.findInvoiceByNumber(invoiceNumber)).thenReturn(invoices);

        Response response = invoiceController.getInvoiceByNumber(invoiceNumber);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(invoices, response.getEntity());
        verify(invoiceService).findInvoiceByNumber(invoiceNumber);
    }

    @Test
    void testGetInvoiceByNumber_InvalidData() {
        String invoiceNumber = "NF-001";
        when(invoiceService.findInvoiceByNumber(invoiceNumber))
                .thenThrow(new InvalidDataException("Número da nota fiscal inválido"));

        Response response = invoiceController.getInvoiceByNumber(invoiceNumber);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Número da nota fiscal inválido", response.getEntity());
    }

    @Test
    void testUpdateInvoice_Success() {
        Long invoiceId = 1L;
        InvoiceDTO invoiceDTO = createSampleInvoiceDTO();
        Invoice updatedInvoice = new Invoice();
        updatedInvoice.setId(invoiceId);
        updatedInvoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());

        when(invoiceService.updateInvoice(invoiceId, invoiceDTO)).thenReturn(updatedInvoice);

        Response response = invoiceController.updateInvoice(invoiceId, invoiceDTO);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(updatedInvoice, response.getEntity());
        verify(invoiceService).updateInvoice(invoiceId, invoiceDTO);
    }

    @Test
    void testUpdateInvoice_InvalidData() {
        Long invoiceId = 1L;
        InvoiceDTO invoiceDTO = createSampleInvoiceDTO();

        when(invoiceService.updateInvoice(invoiceId, invoiceDTO))
                .thenThrow(new InvalidDataException("Nota fiscal inválida"));

        Response response = invoiceController.updateInvoice(invoiceId, invoiceDTO);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Nota fiscal inválida", response.getEntity());
    }

    @Test
    void testDeleteInvoice_Success() {
        Long invoiceId = 1L;
        doNothing().when(invoiceService).deleteInvoice(invoiceId);

        Response response = invoiceController.deleteInvoice(invoiceId);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(invoiceService).deleteInvoice(invoiceId);
    }

    @Test
    void testDeleteInvoice_NotFound() {
        Long invoiceId = 1L;
        doThrow(new EntityNotFoundException("Nota fiscal não foi encontrada"))
                .when(invoiceService).deleteInvoice(invoiceId);
        
        Response response = invoiceController.deleteInvoice(invoiceId);
        
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Nota fiscal não foi encontrada", response.getEntity());
    }
    
    private InvoiceDTO createSampleInvoiceDTO() {
        InvoiceDTO invoiceDTO = new InvoiceDTO();

        invoiceDTO.setInvoiceNumber("NF-001");
        invoiceDTO.setIssueDate(LocalDateTime.now());
        invoiceDTO.setSupplierId(1L);
        invoiceDTO.setAddress("Av. Teste");

        return invoiceDTO;
    }
}
