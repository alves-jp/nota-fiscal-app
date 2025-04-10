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
import java.util.stream.Collectors;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {


    @Inject
    ProductRepository productRepository;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        validateProductDTO(productDTO);

        Product product = new Product();
        product.setProductCode(productDTO.getProductCode());
        product.setDescription(productDTO.getDescription());
        product.setProductStatus(productDTO.getProductStatus());

        List<Product> existingProducts = productRepository.findByCode(product.getProductCode());
        if (existingProducts != null && !existingProducts.isEmpty()) {
            throw new BusinessException("Já existe um produto cadastrado com o código informado.");
        }

        productRepository.persist(product);

        return convertToDTO(product);
    }

    @Override
    public ProductDTO findProductById(Long id) {
        Product product = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        return convertToDTO(product);
    }

    @Override
    public List<ProductDTO> findAllProducts() {
        List<Product> products = productRepository.listAll();
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> findProductByCode(String productCode) {
        if (productCode == null || productCode.isBlank()) {
            throw new BusinessException("O código do produto é obrigatório para a busca.");

        }
        List<Product> products = productRepository.findByCode(productCode);
        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        validateProductDTO(productDTO);

        Product existingProduct = productRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

        existingProduct.setProductCode(productDTO.getProductCode());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setProductStatus(productDTO.getProductStatus());
        productRepository.persist(existingProduct);

        return convertToDTO(existingProduct);
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

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(product.getId(),
                product.getProductCode(),
                product.getDescription(),
                product.getProductStatus());
    }
}
