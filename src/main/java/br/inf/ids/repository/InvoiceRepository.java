package br.inf.ids.repository;

import br.inf.ids.model.Invoice;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class InvoiceRepository implements PanacheRepository<Invoice> {

    public List<Invoice> findBySupplierId(Long supplierId) {
        return list("supplier.id", supplierId);
    }

    public Invoice findByInvoiceNumber(String invoiceNumber) {
        return find("invoiceNumber", invoiceNumber).firstResult();
    }
}