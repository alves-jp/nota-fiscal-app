package br.inf.ids.service;

import br.inf.ids.model.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {

    Invoice createInvoice(Invoice invoice);

    Optional<Invoice> findInvoiceById(Long id);

    List<Invoice> findAllInvoices();

    Invoice updateInvoice(Long id, Invoice invoice);

    void deleteInvoice(Long id);

    List<Invoice> searchInvoices(String invoiceNumber);
}