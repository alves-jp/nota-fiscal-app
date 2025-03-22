package br.inf.ids.repository;

import br.inf.ids.model.InvoiceItem;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class InvoiceItemRepository implements PanacheRepository<InvoiceItem> {

    public List<InvoiceItem> findByInvoiceId(Long invoiceId) {
        return list("invoice.id", invoiceId);
    }

    public List<InvoiceItem> findByProductId(Long productId) {
        return list("product.id", productId);
    }
}
