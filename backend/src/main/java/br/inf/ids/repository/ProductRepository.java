package br.inf.ids.repository;

import br.inf.ids.model.Product;
import br.inf.ids.model.enums.ProductStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Status;

import java.util.List;
import static java.util.Collections.list;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public List<Product> findByProductStatus(ProductStatus productStatus) {
        return list("productStatus", productStatus);
    }

    public List<Product> findByDescription(String description) {
        return list("LOWER(description) LIKE LOWER(?1)", "%" + description + "%");
    }

    public boolean hasInvoiceItems(Long productId) {
        return count("product.id", productId) > 0;
    }
}