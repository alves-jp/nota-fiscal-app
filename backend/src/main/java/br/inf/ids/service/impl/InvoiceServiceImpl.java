package br.inf.ids.service.impl;

import br.inf.ids.dto.InvoiceDTO;
import br.inf.ids.dto.InvoiceResponseDTO;
import br.inf.ids.exception.InvalidDataException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.model.Invoice;
import br.inf.ids.model.Supplier;
import br.inf.ids.repository.InvoiceRepository;
import br.inf.ids.repository.SupplierRepository;
import br.inf.ids.service.InvoiceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class InvoiceServiceImpl implements InvoiceService {

    @Inject
    InvoiceRepository invoiceRepository;

    @Inject
    SupplierRepository supplierRepository;

    @Override
    @Transactional
    public Invoice createInvoice(InvoiceDTO invoiceDTO) throws InvalidDataException {
        validateInvoiceDTO(invoiceDTO);

        Supplier supplier = supplierRepository.findById(invoiceDTO.getSupplierId());

        if (supplier == null) {
            throw new InvalidDataException("Fornecedor com ID " + invoiceDTO.getSupplierId() + " não encontrado.");

        }
        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setIssueDate(invoiceDTO.getIssueDate());
        invoice.setAddress(invoiceDTO.getAddress());
        invoice.setSupplier(supplier);

        if (invoice.getItems() != null && !invoice.getItems().isEmpty()) {
            Double totalValue = calculateTotalValue(invoice);
            invoice.setTotalValue(totalValue);

        } else {
            invoice.setTotalValue(0.0);

        }
        invoiceRepository.persist(invoice);
        return invoice;
    }

    @Override
    public InvoiceResponseDTO getInvoiceById(Long id) throws EntityNotFoundException {
        Invoice invoice = invoiceRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota fiscal não encontrada."));

        return createInvoiceResponseDTO(invoice);
    }

    @Override
    public List<InvoiceResponseDTO> findAllInvoices() {
        return invoiceRepository.listAll().stream()
                .map(this::createInvoiceResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Invoice> findInvoiceByNumber(String invoiceNumber) throws InvalidDataException {
        if (invoiceNumber == null || invoiceNumber.isBlank()) {
            throw new InvalidDataException("O número da nota fiscal é obrigatório para a busca.");
        }
        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }

    @Override
    @Transactional
    public Invoice updateInvoice(Long id, InvoiceDTO invoiceDTO) throws InvalidDataException, EntityNotFoundException {
        validateInvoiceDTO(invoiceDTO);

        Invoice existingInvoice = invoiceRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota fiscal não encontrada."));

        existingInvoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        existingInvoice.setIssueDate(invoiceDTO.getIssueDate());
        existingInvoice.setAddress(invoiceDTO.getAddress());

        return existingInvoice;
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) throws EntityNotFoundException {
        Invoice invoice = invoiceRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota fiscal não encontrada."));

        invoiceRepository.delete(invoice);
    }

    @Override
    public Double calculateTotalValue(Invoice invoice) {
        if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
            return 0.0;
        }

        return invoice.getItems().stream()
                .mapToDouble(item -> item.getUnitValue() * item.getQuantity())
                .sum();
    }

    private void validateInvoiceDTO(InvoiceDTO invoiceDTO) throws InvalidDataException {
        if (invoiceDTO.getInvoiceNumber() == null || invoiceDTO.getInvoiceNumber().isBlank()) {
            throw new InvalidDataException("O número da nota fiscal é obrigatório.");

        } if (invoiceDTO.getIssueDate() == null) {
            throw new InvalidDataException("A data de emissão é obrigatória.");

        } if (invoiceDTO.getSupplierId() == null) {
            throw new InvalidDataException("O fornecedor é obrigatório.");
        }
    }

    private InvoiceResponseDTO createInvoiceResponseDTO(Invoice invoice) {
        InvoiceResponseDTO responseDTO = new InvoiceResponseDTO();

        responseDTO.setId(invoice.getId());
        responseDTO.setInvoiceNumber(invoice.getInvoiceNumber());
        responseDTO.setIssueDate(invoice.getIssueDate());
        responseDTO.setSupplier(invoice.getSupplier());
        responseDTO.setAddress(invoice.getAddress());
        responseDTO.setItems(invoice.getItems());

        Double totalValue = calculateTotalValue(invoice);
        responseDTO.setTotalValue(totalValue);

        invoice.setTotalValue(totalValue);
        invoiceRepository.persist(invoice);

        return responseDTO;
    }

    @Override
    @Transactional
    public void updateTotalValue(Invoice invoice) {
        if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
            invoice.setTotalValue(0.0);

        } else {
            Double totalValue = invoice.getItems().stream()
                    .mapToDouble(item -> item.getUnitValue() * item.getQuantity())
                    .sum();
            invoice.setTotalValue(totalValue);

        }
        invoiceRepository.persist(invoice);
    }
}