package br.inf.ids.service;

import br.inf.ids.model.InvoiceItem;

import java.util.List;
import java.util.Optional;

public interface InvoiceItemService {

    InvoiceItem createInvoiceItem(InvoiceItem invoiceItem);

    Optional<InvoiceItem> findInvoiceItemById(Long id);

    List<InvoiceItem> findAllInvoiceItems();

    List<InvoiceItem> findInvoiceItemsByInvoiceId(Long invoiceId);

    List<InvoiceItem> findInvoiceItemsByProductId(Long productId);

    InvoiceItem updateInvoiceItem(Long id, InvoiceItem invoiceItem);

    void deleteInvoiceItem(Long id);
}