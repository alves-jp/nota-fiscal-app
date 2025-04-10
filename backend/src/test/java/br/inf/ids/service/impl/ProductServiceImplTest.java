package br.inf.ids.service.impl;

import br.inf.ids.dto.ProductDTO;
import br.inf.ids.exception.BusinessException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.model.Product;
import br.inf.ids.model.enums.ProductStatus;
import br.inf.ids.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDTO productDTOInput;
    private Product productEntity;

    @BeforeEach
    public void setUp() {
        productDTOInput = new ProductDTO();
        productDTOInput.setProductCode("P-00001");
        productDTOInput.setDescription("Computador Teste V1");
        productDTOInput.setProductStatus(ProductStatus.ACTIVE);

        productEntity = new Product();
        productEntity.setId(1L);
        productEntity.setProductCode("P-00002");
        productEntity.setDescription("Notebook V2");
        productEntity.setProductStatus(ProductStatus.ACTIVE);
    }

    @Test
    public void testCreateProduct_Success() {
        doAnswer(invocation -> {
            Product productArg = invocation.getArgument(0);
            productArg.setId(2L);
            return null;
        }).when(productRepository).persist(any(Product.class));

        ProductDTO productToCreate = new ProductDTO();
        productToCreate.setProductCode("P-00003");
        productToCreate.setDescription("Computador Teste V3");
        productToCreate.setProductStatus(ProductStatus.ACTIVE);

        ProductDTO createdProductDTO = productService.createProduct(productToCreate);

        assertNotNull(createdProductDTO);
        assertEquals("P-00003", createdProductDTO.getProductCode());
        assertEquals("Computador Teste V3", createdProductDTO.getDescription());
        assertEquals(ProductStatus.ACTIVE, createdProductDTO.getProductStatus());

        verify(productRepository, times(1)).persist(any(Product.class));
    }

    @Test
    public void testCreateProduct_Failure() {
        productDTOInput.setDescription("");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.createProduct(productDTOInput);
        });

        assertEquals("A descrição do produto é obrigatória.", exception.getMessage());
        verify(productRepository, never()).persist(any(Product.class));
    }

    @Test
    public void testFindProductById_Success() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(productEntity));

        ProductDTO foundProductDTO = productService.findProductById(1L);

        assertNotNull(foundProductDTO);
        assertEquals(productEntity.getId(), foundProductDTO.getId());
        assertEquals(productEntity.getProductCode(), foundProductDTO.getProductCode());
        assertEquals(productEntity.getDescription(), foundProductDTO.getDescription());
        assertEquals(productEntity.getProductStatus(), foundProductDTO.getProductStatus());
    }

    @Test
    public void testFindProductById_Failure() {
        when(productRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.findProductById(99L);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
    }

    @Test
    public void testFindAllProducts() {
        when(productRepository.listAll()).thenReturn(List.of(productEntity));

        List<ProductDTO> products = productService.findAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(productEntity.getId(), products.get(0).getId());
        assertEquals(productEntity.getProductCode(), products.get(0).getProductCode());
        assertEquals(productEntity.getDescription(), products.get(0).getDescription());
        assertEquals(productEntity.getProductStatus(), products.get(0).getProductStatus());
    }

    @Test
    public void testFindAllProducts_EmptyList() {
        when(productRepository.listAll()).thenReturn(Collections.emptyList());

        List<ProductDTO> products = productService.findAllProducts();

        assertNotNull(products);
        assertTrue(products.isEmpty());
    }


    @Test
    public void testFindProductByCode_Success() {
        Product productForThisTest = new Product();
        productForThisTest.setId(3L);
        productForThisTest.setProductCode("P-00004");
        productForThisTest.setDescription("Smartphone Teste V4");
        productForThisTest.setProductStatus(ProductStatus.INACTIVE);

        when(productRepository.findByCode("P-00004")).thenReturn(List.of(productForThisTest));

        List<ProductDTO> products = productService.findProductByCode("P-00004");

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(productForThisTest.getId(), products.get(0).getId());
        assertEquals(productForThisTest.getProductCode(), products.get(0).getProductCode());
        assertEquals(productForThisTest.getDescription(), products.get(0).getDescription());
        assertEquals(productForThisTest.getProductStatus(), products.get(0).getProductStatus());
    }

    @Test
    public void testFindProductByCode_NotFound() {
        when(productRepository.findByCode("P-00000")).thenReturn(Collections.emptyList());

        List<ProductDTO> products = productService.findProductByCode("P-00000");

        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    public void testFindProductByCode_Failure() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.findProductByCode(null);
        });

        assertEquals("O código do produto é obrigatório para a busca.", exception.getMessage());
        verify(productRepository, never()).findByCode(any());
    }


    @Test
    public void testUpdateProduct_Success() {
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setProductCode("P-00005");
        existingProduct.setDescription("Notebook Velho V1");
        existingProduct.setProductStatus(ProductStatus.ACTIVE);

        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(existingProduct));
        doNothing().when(productRepository).persist(any(Product.class));

        ProductDTO updatedInfoDTO = new ProductDTO();
        updatedInfoDTO.setProductCode("P-000007");
        updatedInfoDTO.setDescription("Notebook Novo V1.1");
        updatedInfoDTO.setProductStatus(ProductStatus.INACTIVE);

        ProductDTO resultDTO = productService.updateProduct(1L, updatedInfoDTO);

        assertNotNull(resultDTO);
        assertEquals(1L, resultDTO.getId());
        assertEquals("P-000007", resultDTO.getProductCode());
        assertEquals("Notebook Novo V1.1", resultDTO.getDescription());
        assertEquals(ProductStatus.INACTIVE, resultDTO.getProductStatus());

        verify(productRepository, times(1)).findByIdOptional(1L);
        verify(productRepository, times(1)).persist(any(Product.class));
    }

    @Test
    public void testUpdateProduct_Failure() {
        when(productRepository.findByIdOptional(99L)).thenReturn(Optional.empty());
        ProductDTO updatedInfoDTO = new ProductDTO();
        updatedInfoDTO.setProductCode("P-000006");
        updatedInfoDTO.setDescription("Notebook Teste V1");
        updatedInfoDTO.setProductStatus(ProductStatus.ACTIVE);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.updateProduct(99L, updatedInfoDTO);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
        verify(productRepository, times(1)).findByIdOptional(99L);
        verify(productRepository, never()).persist(any(Product.class));
    }

    @Test
    public void testDeleteProduct_Success() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(productEntity));
        when(productRepository.hasInvoiceItems(1L)).thenReturn(false);
        doNothing().when(productRepository).delete(any(Product.class));

        assertDoesNotThrow(() -> {
            productService.deleteProduct(1L);
        });

        verify(productRepository, times(1)).findByIdOptional(1L);
        verify(productRepository, times(1)).hasInvoiceItems(1L);
        verify(productRepository, times(1)).delete(productEntity);
    }

    @Test
    public void testDeleteProduct_Failure_ProductNotFound() {
        when(productRepository.findByIdOptional(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.deleteProduct(99L);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
        verify(productRepository, times(1)).findByIdOptional(99L);
        verify(productRepository, never()).hasInvoiceItems(anyLong());
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    public void testDeleteProduct_Failure_ProductHasInvoiceItems() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(productEntity));
        when(productRepository.hasInvoiceItems(1L)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Não é possível excluir um produto com movimentação.", exception.getMessage());
        verify(productRepository, times(1)).findByIdOptional(1L);
        verify(productRepository, times(1)).hasInvoiceItems(1L);
        verify(productRepository, never()).delete(any(Product.class));
    }
}