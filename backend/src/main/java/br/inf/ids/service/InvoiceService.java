package br.inf.ids.service;

import br.inf.ids.dto.InvoiceDTO;
import br.inf.ids.dto.InvoiceResponseDTO;
import br.inf.ids.exception.InvalidDataException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.model.Invoice;
import java.util.List;

public interface InvoiceService {

    Invoice createInvoice(InvoiceDTO invoiceDTO) throws InvalidDataException;

    InvoiceResponseDTO getInvoiceById(Long id) throws EntityNotFoundException;

    List<InvoiceResponseDTO> findAllInvoices();

    List<Invoice> findInvoiceByNumber(String invoiceNumber) throws InvalidDataException;

    Invoice updateInvoice(Long id, InvoiceDTO invoiceDTO) throws InvalidDataException, EntityNotFoundException;

    void deleteInvoice(Long id) throws EntityNotFoundException;

    Double calculateTotalValue(Invoice invoice);

    void updateTotalValue(Invoice invoice);
}