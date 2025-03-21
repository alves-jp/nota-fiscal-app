package br.inf.ids.service;

import br.inf.ids.model.Product;
import br.inf.ids.model.enums.ProductStatus;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product createProduct(Product product);

    Optional<Product> findProductById(Long id);

    List<Product> searchProducts(String searchTerm);

    List<Product> findAllProducts();

    List<Product> findProductsByStatus(ProductStatus status);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
