package br.inf.ids.service.impl;

import br.inf.ids.dto.ProductDTO;
import br.inf.ids.exception.BusinessException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.model.Product;
import br.inf.ids.repository.ProductRepository;
import br.inf.ids.service.ProductService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    @Inject
    ProductRepository productRepository;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) {
        validateProductDTO(productDTO);

        Product product = new Product();

        product.setProductCode(productDTO.getProductCode());
        product.setDescription(productDTO.getDescription());
        product.setProductStatus(productDTO.getProductStatus());
        productRepository.persist(product);

        return product;
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.listAll();
    }

    @Override
    public List<Product> findProductByCode(String productCode) {
        if (productCode == null || productCode.isBlank()) {
            throw new BusinessException("O código do produto é obrigatório para a busca.");
        }
        return productRepository.findByCode(productCode);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) {
        validateProductDTO(productDTO);

        Product existingProduct = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        existingProduct.setProductCode(productDTO.getProductCode());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setProductStatus(productDTO.getProductStatus());

        return existingProduct;
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        if (productRepository.hasInvoiceItems(id)) {
            throw new BusinessException("Não é possível excluir um produto com movimentação.");

        }
        productRepository.delete(product);
    }

    private void validateProductDTO(ProductDTO productDTO) {
        if (productDTO.getDescription() == null || productDTO.getDescription().isBlank()) {
            throw new BusinessException("A descrição do produto é obrigatória.");

        } if (productDTO.getProductStatus() == null) {
            throw new BusinessException("O status do produto é obrigatório.");

        } if (productDTO.getProductCode() == null) {
            throw new BusinessException("O código do produto é obrigatório.");
        }
    }
}