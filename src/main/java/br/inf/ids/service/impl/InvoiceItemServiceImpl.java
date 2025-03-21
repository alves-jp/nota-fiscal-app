package br.inf.ids.service.impl;

import br.inf.ids.model.InvoiceItem;
import br.inf.ids.repository.InvoiceItemRepository;
import br.inf.ids.service.InvoiceItemService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InvoiceItemServiceImpl implements InvoiceItemService {

    @Inject
    InvoiceItemRepository invoiceItemRepository;

    @Override
    @Transactional
    public InvoiceItem createInvoiceItem(InvoiceItem invoiceItem) {
        invoiceItemRepository.persist(invoiceItem);
        return invoiceItem;
    }

    @Override
    public Optional<InvoiceItem> findInvoiceItemById(Long id) {
        return invoiceItemRepository.findByIdOptional(id);
    }

    @Override
    public List<InvoiceItem> findAllInvoiceItems() {
        return invoiceItemRepository.listAll();
    }

    @Override
    @Transactional
    public InvoiceItem updateInvoiceItem(Long id, InvoiceItem invoiceItem) {
        InvoiceItem existingInvoiceItem = invoiceItemRepository.findById(id);

        if (existingInvoiceItem != null) {
            existingInvoiceItem.setInvoice(invoiceItem.getInvoice());
            existingInvoiceItem.setProduct(invoiceItem.getProduct());
            existingInvoiceItem.setUnitValue(invoiceItem.getUnitValue());
            existingInvoiceItem.setQuantity(invoiceItem.getQuantity());
            existingInvoiceItem.setTotalItemValue(invoiceItem.getTotalItemValue());

        }
        return existingInvoiceItem;
    }

    @Override
    @Transactional
    public void deleteInvoiceItem(Long id) {
        InvoiceItem invoiceItem = invoiceItemRepository.findById(id);

        if (invoiceItem != null) {
            invoiceItemRepository.delete(invoiceItem);
        }
    }

    @Override
    public List<InvoiceItem> findInvoiceItemsByInvoiceId(Long invoiceId) {
        return invoiceItemRepository.findByInvoiceId(invoiceId);
    }

    @Override
    public List<InvoiceItem> findInvoiceItemsByProductId(Long productId) {
        return invoiceItemRepository.findByProductId(productId);
    }
}