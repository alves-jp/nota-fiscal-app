package br.inf.ids.service.impl;

import br.inf.ids.model.Invoice;
import br.inf.ids.repository.InvoiceRepository;
import br.inf.ids.service.InvoiceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InvoiceServiceImpl implements InvoiceService {

    @Inject
    InvoiceRepository invoiceRepository;

    @Override
    @Transactional
    public Invoice createInvoice(Invoice invoice) {
        invoiceRepository.persist(invoice);

        return invoice;
    }

    @Override
    public Optional<Invoice> findInvoiceById(Long id) {
        return invoiceRepository.findByIdOptional(id);
    }

    @Override
    public List<Invoice> findAllInvoices() {
        return invoiceRepository.listAll();
    }

    @Override
    public List<Invoice> findInvoiceByNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }

    @Override
    @Transactional
    public Invoice updateInvoice(Long id, Invoice invoice) {
        Invoice existingInvoice = invoiceRepository.findById(id);

        if (existingInvoice != null) {
            existingInvoice.setInvoiceNumber(invoice.getInvoiceNumber());
            existingInvoice.setIssueDate(invoice.getIssueDate());
            existingInvoice.setSupplier(invoice.getSupplier());
            existingInvoice.setAddress(invoice.getAddress());
            existingInvoice.setTotalValue(invoice.getTotalValue());

        }
        return existingInvoice;
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id);

        if (invoice != null) {
            invoiceRepository.delete(invoice);
        }
    }
}