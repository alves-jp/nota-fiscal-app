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

    private ProductDTO productDTO;
    private Product product;

    @BeforeEach
    public void setUp() {
        productDTO = new ProductDTO();
        productDTO.setProductCode("P-00001-TESTE");
        productDTO.setDescription("Computador Teste V1");
        productDTO.setProductStatus(ProductStatus.valueOf("ACTIVE"));

        product = new Product();
        product.setId(1L);
        product.setProductCode("P-00002-TESTE");
        product.setDescription("Computador Teste V2");
        product.setProductStatus(ProductStatus.valueOf("ACTIVE"));
    }

    @Test
    public void testCreateProduct_Success() {
        doAnswer(invocation -> {
            Product productArg = invocation.getArgument(0);
            productArg.setId(1L);
            return null;
        }).when(productRepository).persist(any(Product.class));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductCode("P-00003-TESTE");
        productDTO.setDescription("Computador Teste V3");
        productDTO.setProductStatus(ProductStatus.ACTIVE);
        ProductDTO createdProductDTO = productService.createProduct(productDTO);

        assertNotNull(createdProductDTO);
        assertEquals("P-00003-TESTE", createdProductDTO.getProductCode());
        assertEquals("Computador Teste V3", createdProductDTO.getDescription());
        assertEquals(ProductStatus.ACTIVE, createdProductDTO.getProductStatus());
    }

    @Test
    public void testCreateProduct_Failure_DescriptionIsBlank() {
        productDTO.setDescription("");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.createProduct(productDTO);
        });

        assertEquals("A descrição do produto é obrigatória.", exception.getMessage());
    }

    @Test
    public void testFindProductById_Success() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(product));

        ProductDTO foundProductDTO = productService.findProductById(1L);

        assertNotNull(foundProductDTO);
        assertEquals(1L, foundProductDTO.getId());
        assertEquals("P-00002-TESTE", foundProductDTO.getProductCode());
    }

    @Test
    public void testFindProductById_Failure_ProductNotFound() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.findProductById(1L);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
    }

    @Test
    public void testFindAllProducts() {
        when(productRepository.listAll()).thenReturn(List.of(product));

        List<ProductDTO> products = productService.findAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("P-00002-TESTE", products.get(0).getProductCode());
    }

    @Test
    public void testFindProductByCode_Success() {
        when(productRepository.findByCode("P-00001-TESTE")).thenReturn(List.of(product));

        List<ProductDTO> products = productService.findProductByCode("P-00001-TESTE");

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("P-00001-TESTE", products.get(0).getProductCode());
    }

    @Test
    public void testFindProductByCode_Failure_CodeIsNull() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.findProductByCode(null);
        });

        assertEquals("O código do produto é obrigatório para a busca.", exception.getMessage());
    }

    @Test
    public void testUpdateProduct_Success() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).persist(any(Product.class));
        productDTO.setProductCode("P-00001-NOVO");
        productDTO.setDescription("Computador Novo");
        productDTO.setProductStatus(ProductStatus.valueOf("INACTIVE"));

        ProductDTO updatedProductDTO = productService.updateProduct(1L, productDTO);

        assertNotNull(updatedProductDTO);
        assertEquals("P-00001-NOVO", updatedProductDTO.getProductCode());
        assertEquals("Computador Novo", updatedProductDTO.getDescription());
        assertEquals(ProductStatus.INACTIVE, updatedProductDTO.getProductStatus());
    }

    @Test
    public void testUpdateProduct_Failure_ProductNotFound() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.updateProduct(1L, productDTO);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
    }

    @Test
    public void testDeleteProduct_Success() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(product));
        when(productRepository.hasInvoiceItems(1L)).thenReturn(false);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    public void testDeleteProduct_Failure_ProductNotFound() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Produto não encontrado.", exception.getMessage());
    }

    @Test
    public void testDeleteProduct_Failure_ProductHasInvoiceItems() {
        when(productRepository.findByIdOptional(1L)).thenReturn(Optional.of(product));
        when(productRepository.hasInvoiceItems(1L)).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals("Não é possível excluir um produto com movimentação.", exception.getMessage());
    }
}
