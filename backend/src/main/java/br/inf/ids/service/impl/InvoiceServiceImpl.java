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
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        ensureInvoiceNumberIsUnique(invoiceDTO.getInvoiceNumber());

        Supplier supplier = findSupplierById(invoiceDTO.getSupplierId());
        LocalDateTime issueDate = normalizeIssueDate(invoiceDTO.getIssueDate());
        invoiceDTO.setIssueDate(issueDate);

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setIssueDate(issueDate);
        invoice.setAddress(invoiceDTO.getAddress());
        invoice.setSupplier(supplier);
        invoice.setTotalValue(0.00);
        invoiceRepository.persist(invoice);

        return invoice;
    }

    @Override
    public InvoiceResponseDTO getInvoiceById(Long id) throws EntityNotFoundException {
        Invoice invoice = findInvoiceById(id);
        return mapToDTO(invoice);
    }

    @Override
    public List<InvoiceResponseDTO> findAllInvoices() {
        return invoiceRepository.listAll().stream()
                .map(this::mapToDTO)
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

        Invoice existingInvoice = findInvoiceById(id);

        if (!existingInvoice.getSupplier().getId().equals(invoiceDTO.getSupplierId())) {
            throw new InvalidDataException("Não é permitido alterar o fornecedor de uma nota fiscal existente.");
        }

        existingInvoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        existingInvoice.setIssueDate(invoiceDTO.getIssueDate());
        existingInvoice.setAddress(invoiceDTO.getAddress());

        return existingInvoice;
    }

    @Override
    @Transactional
    public void deleteInvoice(Long id) throws EntityNotFoundException {
        Invoice invoice = findInvoiceById(id);
        invoiceRepository.delete(invoice);
    }

    @Override
    public Double calculateTotalValue(Invoice invoice) {
        if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
            return 0.00;
        }

        return invoice.getItems().stream()
                .mapToDouble(item -> item.getUnitValue() * item.getQuantity())
                .sum();
    }

    @Override
    @Transactional
    public void updateTotalValue(Invoice invoice) {
        double total = calculateTotalValue(invoice);
        invoice.setTotalValue(total);
        invoiceRepository.persist(invoice);
    }

    private Invoice findInvoiceById(Long id) throws EntityNotFoundException {
        return invoiceRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota fiscal não encontrada."));
    }

    private Supplier findSupplierById(Long id) throws InvalidDataException {
        Supplier supplier = supplierRepository.findById(id);

        if (supplier == null) {
            throw new InvalidDataException("Fornecedor com ID " + id + " não encontrado.");
        }

        return supplier;
    }

    private void ensureInvoiceNumberIsUnique(String invoiceNumber) throws InvalidDataException {
        List<Invoice> existing = invoiceRepository.findByInvoiceNumber(invoiceNumber);

        if (existing != null && !existing.isEmpty()) {
            throw new InvalidDataException("Já existe uma nota fiscal cadastrada com o número informado.");
        }
    }

    private LocalDateTime normalizeIssueDate(LocalDateTime input) {
        if (input == null) return LocalDateTime.now();

        if (input.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return LocalDateTime.of(input.toLocalDate(), LocalTime.now());
        }

        return input;
    }

    private void validateInvoiceDTO(InvoiceDTO dto) throws InvalidDataException {
        if (dto.getInvoiceNumber() == null || dto.getInvoiceNumber().isBlank()) {
            throw new InvalidDataException("O número da nota fiscal é obrigatório.");
        }

        if (dto.getSupplierId() == null) {
            throw new InvalidDataException("É obrigatório selecionar um fornecedor.");
        }
    }

    private InvoiceResponseDTO mapToDTO(Invoice invoice) {
        return new InvoiceResponseDTO(
                invoice.getId(),
                invoice.getInvoiceNumber(),
                invoice.getIssueDate(),
                invoice.getSupplier(),
                invoice.getAddress(),
                invoice.getItems(),
                invoice.getTotalValue()
        );
    }
}
