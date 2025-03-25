package br.inf.ids.service;

import br.inf.ids.dto.ProductDTO;
import br.inf.ids.model.Product;
import jakarta.transaction.Transactional;
import java.util.List;

public interface ProductService {

    @Transactional
    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO findProductById(Long id);

    List<ProductDTO> findAllProducts();

    List<ProductDTO> findProductByCode(String productCode);

    @Transactional
    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    @Transactional
    void deleteProduct(Long id);
}
