package br.inf.ids.service.impl;

import br.inf.ids.model.Product;
import br.inf.ids.model.enums.ProductStatus;
import br.inf.ids.repository.ProductRepository;
import br.inf.ids.service.ProductService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    @Inject
    ProductRepository productRepository;

    @Override
    @Transactional
    public Product createProduct(Product product) {
        productRepository.persist(product);

        return product;
    }

    @Override
    public Optional<Product> findProductById(Long id) {
        return productRepository.findByIdOptional(id);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.listAll();
    }

    @Override
    public List<Product> findProductsByStatus(ProductStatus status) {
        return productRepository.findByProductStatus(status);
    }

    @Override
    public List<Product> findProductByDescription(String description) {
        return productRepository.findByDescription(description);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id);

        if (existingProduct != null) {
            existingProduct.setDescription(product.getDescription());
            existingProduct.setProductStatus(product.getProductStatus());

        }
        return existingProduct;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id);

        if (product != null) {
            if (productRepository.hasInvoiceItems(id)) {
                throw new IllegalStateException("Erro: Não é possível excluir um produto com movimentação.");

            }
            productRepository.delete(product);
        }
    }
}