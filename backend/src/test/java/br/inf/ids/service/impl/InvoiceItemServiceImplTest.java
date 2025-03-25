package br.inf.ids.service.impl;

import br.inf.ids.dto.InvoiceItemDTO;
import br.inf.ids.exception.InvalidDataException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.model.Invoice;
import br.inf.ids.model.InvoiceItem;
import br.inf.ids.model.Product;
import br.inf.ids.repository.InvoiceItemRepository;
import br.inf.ids.repository.InvoiceRepository;
import br.inf.ids.repository.ProductRepository;
import br.inf.ids.service.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class InvoiceItemServiceImplTest {

    @Mock
    InvoiceItemRepository invoiceItemRepository;

    @Mock
    InvoiceRepository invoiceRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    InvoiceService invoiceService;

    @InjectMocks
    InvoiceItemServiceImpl invoiceItemService;

    private InvoiceItemDTO invoiceItemDTO;
    private Invoice invoice;
    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        invoice = new Invoice();
        product = new Product();
        product.setId(1L);
        product.setDescription("Notebook Teste");

        invoiceItemDTO = new InvoiceItemDTO();
        invoiceItemDTO.setInvoiceId(1L);
        invoiceItemDTO.setProductId(1L);
        invoiceItemDTO.setUnitValue(100.0);
        invoiceItemDTO.setQuantity(2);
    }

    @Test
    public void testCreateInvoiceItem_Success() throws InvalidDataException {
        when(invoiceRepository.findById(invoiceItemDTO.getInvoiceId())).thenReturn(invoice);
        when(productRepository.findById(invoiceItemDTO.getProductId())).thenReturn(product);
        doNothing().when(invoiceItemRepository).persist(any(InvoiceItem.class));

        InvoiceItem result = invoiceItemService.createInvoiceItem(invoiceItemDTO);

        assertNotNull(result);
        assertEquals(invoice, result.getInvoice());
        assertEquals(product, result.getProduct());
        assertEquals(invoiceItemDTO.getUnitValue(), result.getUnitValue());
        assertEquals(invoiceItemDTO.getQuantity(), result.getQuantity());

        verify(invoiceRepository).findById(invoiceItemDTO.getInvoiceId());
        verify(productRepository).findById(invoiceItemDTO.getProductId());
        verify(invoiceItemRepository).persist(any(InvoiceItem.class));
        verify(invoiceService).updateTotalValue(invoice);
    }

    @Test
    public void testCreateInvoiceItem_InvoiceNotFound() {
        when(invoiceRepository.findById(invoiceItemDTO.getInvoiceId())).thenReturn(null);

        InvalidDataException thrown = assertThrows(InvalidDataException.class, () -> {
            invoiceItemService.createInvoiceItem(invoiceItemDTO);
        });

        assertEquals("Nota fiscal com ID 1 não encontrada.", thrown.getMessage());
    }

    @Test
    public void testCreateInvoiceItem_ProductNotFound() {
        when(invoiceRepository.findById(invoiceItemDTO.getInvoiceId())).thenReturn(invoice);
        when(productRepository.findById(invoiceItemDTO.getProductId())).thenReturn(null);

        InvalidDataException thrown = assertThrows(InvalidDataException.class, () -> {
            invoiceItemService.createInvoiceItem(invoiceItemDTO);
        });

        assertEquals("Produto com ID 1 não encontrado.", thrown.getMessage());
    }

    @Test
    public void testUpdateInvoiceItem_Success() throws InvalidDataException, EntityNotFoundException {
        InvoiceItem existingInvoiceItem = new InvoiceItem();
        existingInvoiceItem.setId(1L);
        existingInvoiceItem.setUnitValue(50.0);
        existingInvoiceItem.setQuantity(1);
        existingInvoiceItem.setInvoice(invoice);
        existingInvoiceItem.setProduct(product);

        when(invoiceItemRepository.findById(1L)).thenReturn(existingInvoiceItem);

        doNothing().when(invoiceService).updateTotalValue(invoice);

        invoiceItemDTO.setUnitValue(120.0);
        invoiceItemDTO.setQuantity(3);

        InvoiceItem result = invoiceItemService.updateInvoiceItem(1L, invoiceItemDTO);

        assertNotNull(result);
        assertEquals(120.0, result.getUnitValue(), 0.01);
        assertEquals(3, result.getQuantity());
    }

    @Test
    public void testDeleteInvoiceItem_Success() throws EntityNotFoundException {
        InvoiceItem existingInvoiceItem = new InvoiceItem();
        existingInvoiceItem.setId(1L);
        existingInvoiceItem.setInvoice(invoice);
        invoice.getItems().add(existingInvoiceItem);

        when(invoiceItemRepository.findByIdOptional(1L)).thenReturn(Optional.of(existingInvoiceItem));
        doNothing().when(invoiceItemRepository).delete(existingInvoiceItem);

        invoiceItemService.deleteInvoiceItem(1L);

        verify(invoiceItemRepository).delete(existingInvoiceItem);
        verify(invoiceService).updateTotalValue(invoice);
    }

    @Test
    public void testDeleteInvoiceItem_NotFound() {
        when(invoiceItemRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            invoiceItemService.deleteInvoiceItem(1L);
        });

        assertEquals("Item não encontrado.", thrown.getMessage());
    }
}
