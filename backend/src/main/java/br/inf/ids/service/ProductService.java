package br.inf.ids.service;

import br.inf.ids.dto.ProductDTO;
import br.inf.ids.model.Product;
import jakarta.transaction.Transactional;
import java.util.List;

public interface ProductService {

    @Transactional
    Product createProduct(ProductDTO productDTO);

    Product findProductById(Long id);

    List<Product> findAllProducts();

    List<Product> findProductByCode(String productCode);

    @Transactional
    Product updateProduct(Long id, ProductDTO productDTO);

    @Transactional
    void deleteProduct(Long id);
}