package br.inf.ids.service.impl;

import br.inf.ids.dto.InvoiceDTO;
import br.inf.ids.dto.InvoiceResponseDTO;
import br.inf.ids.exception.InvalidDataException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.model.Invoice;
import br.inf.ids.model.Supplier;
import br.inf.ids.repository.InvoiceRepository;
import br.inf.ids.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private InvoiceDTO invoiceDTO;
    private Supplier supplier;
    private Invoice invoice;

    @BeforeEach
    public void setUp() {
        invoiceDTO = new InvoiceDTO();
        invoiceDTO.setInvoiceNumber("NF-123.4");
        invoiceDTO.setIssueDate(LocalDateTime.parse("2025-03-25T00:00:00", DateTimeFormatter.ISO_DATE_TIME));
        invoiceDTO.setSupplierId(1L);


        supplier = new Supplier();
        supplier.setId(1L);
        supplier.setCompanyName("Fornecedor Teste");


        invoice = new Invoice();
        invoice.setId(1L);
        invoice.setInvoiceNumber("NF-123.4");
    }

    @Test
    public void testCreateInvoice_Success() throws InvalidDataException {
        when(supplierRepository.findById(1L)).thenReturn(supplier);
        doAnswer(invocation -> {
            Invoice invoice = invocation.getArgument(0);
            invoice.setId(1L);
            return null;
        }).when(invoiceRepository).persist(any(Invoice.class));

        Invoice createdInvoice = invoiceService.createInvoice(invoiceDTO);

        assertNotNull(createdInvoice);
        assertEquals("NF-123.4", createdInvoice.getInvoiceNumber());
        assertEquals(supplier, createdInvoice.getSupplier());
        assertNotNull(createdInvoice.getId());
        verify(invoiceRepository, times(1)).persist(any(Invoice.class));
    }

    @Test
    public void testCreateInvoice_Failure_SupplierNotFound() {
        when(supplierRepository.findById(1L)).thenReturn(null);

        InvalidDataException exception = assertThrows(InvalidDataException.class, () -> {
            invoiceService.createInvoice(invoiceDTO);
        });

        assertEquals("Fornecedor com ID 1 não encontrado.", exception.getMessage());
    }

    @Test
    public void testGetInvoiceById_Success() throws EntityNotFoundException {
        when(invoiceRepository.findByIdOptional(1L)).thenReturn(Optional.of(invoice));

        InvoiceResponseDTO result = invoiceService.getInvoiceById(1L);

        assertNotNull(result);
        assertEquals("NF-123.4", result.getInvoiceNumber());
        assertEquals(1L, result.getId());
    }

    @Test
    public void testGetInvoiceById_Failure_InvoiceNotFound() {
        when(invoiceRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            invoiceService.getInvoiceById(1L);
        });

        assertEquals("Nota fiscal não encontrada.", exception.getMessage());
    }

    @Test
    public void testUpdateInvoice_Success() throws InvalidDataException, EntityNotFoundException {
        when(invoiceRepository.findByIdOptional(1L)).thenReturn(Optional.of(invoice));
        invoiceDTO.setInvoiceNumber("NF-456.7");
        invoiceDTO.setIssueDate(LocalDateTime.parse("2025-03-26T00:00:00", DateTimeFormatter.ISO_DATE_TIME));
        invoiceDTO.setAddress("Av. Teste");

        Invoice updatedInvoice = invoiceService.updateInvoice(1L, invoiceDTO);

        assertNotNull(updatedInvoice);
        assertEquals("NF-456.7", updatedInvoice.getInvoiceNumber());
        assertEquals("Av. Teste", updatedInvoice.getAddress());
    }

    @Test
    public void testUpdateInvoice_Failure_InvoiceNotFound() {
        when(invoiceRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            invoiceService.updateInvoice(1L, invoiceDTO);
        });

        assertEquals("Nota fiscal não encontrada.", exception.getMessage());
    }

    @Test
    public void testDeleteInvoice_Success() throws EntityNotFoundException {
        when(invoiceRepository.findByIdOptional(1L)).thenReturn(Optional.of(invoice));

        invoiceService.deleteInvoice(1L);

        verify(invoiceRepository, times(1)).delete(invoice);
    }

    @Test
    public void testDeleteInvoice_Failure_InvoiceNotFound() {
        when(invoiceRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            invoiceService.deleteInvoice(1L);
        });

        assertEquals("Nota fiscal não encontrada.", exception.getMessage());
    }
}
