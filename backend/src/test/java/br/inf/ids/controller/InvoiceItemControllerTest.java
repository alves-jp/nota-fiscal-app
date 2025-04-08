package br.inf.ids.controller;

import br.inf.ids.dto.InvoiceItemDTO;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.exception.InvalidDataException;
import br.inf.ids.model.InvoiceItem;
import br.inf.ids.service.InvoiceItemService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InvoiceItemControllerTest {

    @Mock
    private InvoiceItemService invoiceItemService;

    @InjectMocks
    private InvoiceItemController invoiceItemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInvoiceItem_Success() {
        InvoiceItemDTO invoiceItemDTO = createSampleInvoiceItemDTO();
        InvoiceItem expectedInvoiceItem = new InvoiceItem();
        expectedInvoiceItem.setId(1L);
        when(invoiceItemService.createInvoiceItem(invoiceItemDTO)).thenReturn(expectedInvoiceItem);
        
        Response response = invoiceItemController.createInvoiceItem(invoiceItemDTO);
        
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(expectedInvoiceItem, response.getEntity());
        verify(invoiceItemService).createInvoiceItem(invoiceItemDTO);
    }

    @Test
    void testCreateInvoiceItem_InvalidData() {
        InvoiceItemDTO invoiceItemDTO = createSampleInvoiceItemDTO();
        when(invoiceItemService.createInvoiceItem(invoiceItemDTO))
                .thenThrow(new InvalidDataException("Item inválido"));
        
        Response response = invoiceItemController.createInvoiceItem(invoiceItemDTO);
        
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Item inválido", response.getEntity());
    }

    @Test
    void testGetInvoiceItemById_Success() {
        Long itemId = 1L;
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setId(itemId);
        when(invoiceItemService.findInvoiceItemById(itemId)).thenReturn(Optional.of(invoiceItem));
        
        Response response = invoiceItemController.getInvoiceItemById(itemId);
        
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(invoiceItem, response.getEntity());
        verify(invoiceItemService).findInvoiceItemById(itemId);
    }

    @Test
    void testGetInvoiceItemById_NotFound() {
        Long itemId = 1L;
        when(invoiceItemService.findInvoiceItemById(itemId)).thenReturn(Optional.empty());
        
        Response response = invoiceItemController.getInvoiceItemById(itemId);
        
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void testGetAllInvoiceItems_Success() {
        List<InvoiceItem> invoiceItems = Arrays.asList(
                new InvoiceItem(),
                new InvoiceItem()
        );
        when(invoiceItemService.findAllInvoiceItems()).thenReturn(invoiceItems);
        
        Response response = invoiceItemController.getAllInvoiceItems();
        
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(invoiceItems, response.getEntity());
        verify(invoiceItemService).findAllInvoiceItems();
    }

    @Test
    void testGetInvoiceItemByInvoiceId_Success() {
        Long invoiceId = 1L;
        List<InvoiceItem> invoiceItems = Arrays.asList(
                new InvoiceItem(),
                new InvoiceItem()
        );
        when(invoiceItemService.findInvoiceItemByInvoiceId(invoiceId)).thenReturn(invoiceItems);
        
        Response response = invoiceItemController.getInvoiceItemByInvoiceId(invoiceId);
        
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(invoiceItems, response.getEntity());
        verify(invoiceItemService).findInvoiceItemByInvoiceId(invoiceId);
    }

    @Test
    void testFindInvoiceItemByProductId_Success() {
        Long productId = 1L;
        List<InvoiceItem> invoiceItems = Arrays.asList(
                new InvoiceItem(),
                new InvoiceItem()
        );
        when(invoiceItemService.findInvoiceItemByProductId(productId)).thenReturn(invoiceItems);
        
        Response response = invoiceItemController.findInvoiceItemByProductId(productId);
        
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(invoiceItems, response.getEntity());
        verify(invoiceItemService).findInvoiceItemByProductId(productId);
    }

    @Test
    void testUpdateInvoiceItem_Success() {
        Long itemId = 1L;
        InvoiceItemDTO invoiceItemDTO = createSampleInvoiceItemDTO();
        InvoiceItem updatedInvoiceItem = new InvoiceItem();
        updatedInvoiceItem.setId(itemId);
        when(invoiceItemService.updateInvoiceItem(itemId, invoiceItemDTO)).thenReturn(updatedInvoiceItem);
        
        Response response = invoiceItemController.updateInvoiceItem(itemId, invoiceItemDTO);
        
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(updatedInvoiceItem, response.getEntity());
        verify(invoiceItemService).updateInvoiceItem(itemId, invoiceItemDTO);
    }

    @Test
    void testUpdateInvoiceItem_InvalidData() {
        Long itemId = 1L;
        InvoiceItemDTO invoiceItemDTO = createSampleInvoiceItemDTO();
        when(invoiceItemService.updateInvoiceItem(itemId, invoiceItemDTO))
                .thenThrow(new InvalidDataException("Item inválido"));
        
        Response response = invoiceItemController.updateInvoiceItem(itemId, invoiceItemDTO);
        
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Item inválido", response.getEntity());
    }

    @Test
    void testDeleteInvoiceItem_Success() {
        Long itemId = 1L;
        doNothing().when(invoiceItemService).deleteInvoiceItem(itemId);
        
        Response response = invoiceItemController.deleteInvoiceItem(itemId);
        
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(invoiceItemService).deleteInvoiceItem(itemId);
    }

    @Test
    void testDeleteInvoiceItem_NotFound() {
        Long itemId = 1L;
        doThrow(new EntityNotFoundException("Item não encontrado"))
                .when(invoiceItemService).deleteInvoiceItem(itemId);
        
        Response response = invoiceItemController.deleteInvoiceItem(itemId);
        
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Item não encontrado", response.getEntity());
    }
    
    private InvoiceItemDTO createSampleInvoiceItemDTO() {
        InvoiceItemDTO invoiceItemDTO = new InvoiceItemDTO();
        
        invoiceItemDTO.setInvoiceId(1L);
        invoiceItemDTO.setProductId(1L);
        invoiceItemDTO.setUnitValue(10.00);
        invoiceItemDTO.setQuantity(2);
        
        return invoiceItemDTO;
    }
}