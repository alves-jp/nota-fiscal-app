package br.inf.ids.repository;

import br.inf.ids.model.Product;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {

    public List<Product> findByCode(String productCode) {
        return list("LOWER(productCode) LIKE LOWER(?1)", "%" + productCode + "%");
    }

    public boolean hasInvoiceItems(Long productId) {
        return count("SELECT COUNT(i) FROM InvoiceItem i WHERE i.product.id = ?1", productId) > 0;
    }
}