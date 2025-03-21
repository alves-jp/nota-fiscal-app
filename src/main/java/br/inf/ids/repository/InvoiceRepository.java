package br.inf.ids.repository;

import br.inf.ids.model.Invoice;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class InvoiceRepository implements PanacheRepository<Invoice> {

    public List<Invoice> findByInvoiceNumber(String invoiceNumber) {
        return list("invoiceNumber", invoiceNumber);
    }

    public List<Invoice> findBySupplierId(Long supplierId) {
        return list("supplier.id", supplierId);
    }

    public List<Invoice> findByIssueDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return list("issueDate between ?1 and ?2", startDate, endDate);
    }
}