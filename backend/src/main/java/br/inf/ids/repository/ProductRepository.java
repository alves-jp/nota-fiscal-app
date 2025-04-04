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

    public List<Product> findByCode(String productCode) {
        return list("LOWER(productCode) LIKE LOWER(?1)", "%" + productCode + "%");
    }

    public boolean hasInvoiceItems(Long productId) {
        return count("SELECT COUNT(i) FROM InvoiceItem i WHERE i.product.id = ?1", productId) > 0;
    }
}