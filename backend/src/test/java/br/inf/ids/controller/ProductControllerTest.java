package br.inf.ids.controller;

import br.inf.ids.dto.ProductDTO;
import br.inf.ids.exception.BusinessException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.service.ProductService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct_Success() {
        ProductDTO productDTO = createSampleProductDTO();
        ProductDTO createdProduct = new ProductDTO();
        createdProduct.setId(1L);
        createdProduct.setDescription(productDTO.getDescription());
        when(productService.createProduct(productDTO)).thenReturn(createdProduct);

        Response response = productController.createProduct(productDTO);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(createdProduct, response.getEntity());
        verify(productService).createProduct(productDTO);
    }

    @Test
    void testCreateProduct_Failure() {
        ProductDTO productDTO = createSampleProductDTO();
        when(productService.createProduct(productDTO))
                .thenThrow(new RuntimeException("Descrição do produto inválida"));

        Response response = productController.createProduct(productDTO);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Descrição do produto inválida", response.getEntity());
    }

    @Test
    void testGetProductById_Success() {
        Long productId = 1L;
        ProductDTO productDTO = createSampleProductDTO();
        productDTO.setId(productId);
        when(productService.findProductById(productId)).thenReturn(productDTO);

        Response response = productController.getProductById(productId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(productDTO, response.getEntity());
        verify(productService).findProductById(productId);
    }

    @Test
    void testGetProductById_NotFound() {
        Long productId = 1L;
        when(productService.findProductById(productId))
                .thenThrow(new RuntimeException("Produto não encontrado"));

        Response response = productController.getProductById(productId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Produto não encontrado", response.getEntity());
    }

    @Test
    void testGetAllProducts_Success() {
        List<ProductDTO> products = Arrays.asList(
                createSampleProductDTO(),
                createSampleProductDTO()
        );
        when(productService.findAllProducts()).thenReturn(products);

        Response response = productController.getAllProducts();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(products, response.getEntity());
        verify(productService).findAllProducts();
    }

    @Test
    void testGetProductByCode_Success() {
        String productCode = "PROD-001";
        List<ProductDTO> products = Arrays.asList(
                createSampleProductDTO(),
                createSampleProductDTO()
        );
        when(productService.findProductByCode(productCode)).thenReturn(products);

        Response response = productController.getProductByCode(productCode);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(products, response.getEntity());
        verify(productService).findProductByCode(productCode);
    }

    @Test
    void testGetProductByCode_Failure() {
        String productCode = "PROD-001";
        when(productService.findProductByCode(productCode))
                .thenThrow(new RuntimeException("Código do produto inválido"));

        Response response = productController.getProductByCode(productCode);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Código do produto inválido", response.getEntity());
    }

    @Test
    void testUpdateProduct_Success() {
        Long productId = 1L;
        ProductDTO productDTO = createSampleProductDTO();
        ProductDTO updatedProduct = new ProductDTO();
        updatedProduct.setId(productId);
        updatedProduct.setDescription(productDTO.getDescription());
        when(productService.updateProduct(productId, productDTO)).thenReturn(updatedProduct);

        Response response = productController.updateProduct(productId, productDTO);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(updatedProduct, response.getEntity());
        verify(productService).updateProduct(productId, productDTO);
    }

    @Test
    void testUpdateProduct_Failure() {
        Long productId = 1L;
        ProductDTO productDTO = createSampleProductDTO();
        when(productService.updateProduct(productId, productDTO))
                .thenThrow(new RuntimeException("Descrição do produto inválida"));

        Response response = productController.updateProduct(productId, productDTO);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Descrição do produto inválida", response.getEntity());
    }

    @Test
    void testDeleteProduct_Success() {
        Long productId = 1L;
        doNothing().when(productService).deleteProduct(productId);

        Response response = productController.deleteProduct(productId);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(productService).deleteProduct(productId);
    }

    @Test
    void testDeleteProduct_Failure() {
        Long productId = 1L;
        doThrow(new EntityNotFoundException("Produto não encontrado"))
                .when(productService).deleteProduct(productId);

        Response response = productController.deleteProduct(productId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Produto não encontrado", response.getEntity());
    }


    private ProductDTO createSampleProductDTO() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription("Produto Teste");
        productDTO.setProductCode("PROD-001");

        return productDTO;
    }
}