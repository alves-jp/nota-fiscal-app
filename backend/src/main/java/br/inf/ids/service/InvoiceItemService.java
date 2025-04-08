package br.inf.ids.service;

import br.inf.ids.dto.InvoiceItemDTO;
import br.inf.ids.exception.InvalidDataException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.model.InvoiceItem;
import java.util.List;
import java.util.Optional;

public interface InvoiceItemService {

    InvoiceItem createInvoiceItem(InvoiceItemDTO invoiceItemDTO) throws InvalidDataException;

    Optional<InvoiceItem> findInvoiceItemById(Long id);

    List<InvoiceItem> findAllInvoiceItems();

    List<InvoiceItem> findInvoiceItemByInvoiceId(Long invoiceId);

    List<InvoiceItem> findInvoiceItemByProductId(Long productId);

    InvoiceItem updateInvoiceItem(Long id, InvoiceItemDTO invoiceItemDTO) throws InvalidDataException, EntityNotFoundException;

    void deleteInvoiceItem(Long id) throws EntityNotFoundException;
}