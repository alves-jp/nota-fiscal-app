package br.inf.ids.service.impl;

import br.inf.ids.dto.InvoiceItemDTO;
import br.inf.ids.exception.InvalidDataException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.model.Invoice;
import br.inf.ids.model.InvoiceItem;
import br.inf.ids.model.Product;
import br.inf.ids.repository.InvoiceRepository;
import br.inf.ids.repository.InvoiceItemRepository;
import br.inf.ids.repository.ProductRepository;
import br.inf.ids.service.InvoiceItemService;
import br.inf.ids.service.InvoiceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InvoiceItemServiceImpl implements InvoiceItemService {

    @Inject
    InvoiceItemRepository invoiceItemRepository;

    @Inject
    InvoiceService invoiceService;

    @Inject
    InvoiceRepository invoiceRepository;

    @Inject
    ProductRepository productRepository;

    @Override
    @Transactional
    public InvoiceItem createInvoiceItem(InvoiceItemDTO invoiceItemDTO) throws InvalidDataException {
        Invoice invoice = invoiceRepository.findById(invoiceItemDTO.getInvoiceId());

        if (invoice == null) {
            throw new InvalidDataException("Nota fiscal com ID " + invoiceItemDTO.getInvoiceId() + " não encontrada.");
        }

        Product product = productRepository.findById(invoiceItemDTO.getProductId());

        if (product == null) {
            throw new InvalidDataException("Produto com ID " + invoiceItemDTO.getProductId() + " não encontrado.");

        } if (invoiceItemDTO.getUnitValue() == null || invoiceItemDTO.getUnitValue() <= 0) {
            throw new InvalidDataException("O valor unitário deve ser maior que 0.00.");

        } if (invoiceItemDTO.getQuantity() == null || invoiceItemDTO.getQuantity() <= 0) {
            throw new InvalidDataException("A quantidade de produtos deve ser maior que zero.");
        }

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setInvoice(invoice);
        invoiceItem.setProduct(product);
        invoiceItem.setUnitValue(invoiceItemDTO.getUnitValue());
        invoiceItem.setQuantity(invoiceItemDTO.getQuantity());
        invoiceItemRepository.persist(invoiceItem);

        if (invoice.getItems() == null) {
            invoice.setItems(new ArrayList<>());
        }

        invoice.getItems().add(invoiceItem);
        invoiceService.updateTotalValue(invoice);

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
    public List<InvoiceItem> findInvoiceItemByInvoiceId(Long invoiceId) {
        return invoiceItemRepository.findByInvoiceId(invoiceId);
    }

    @Override
    public List<InvoiceItem> findInvoiceItemByProductId(Long productId) {
        return invoiceItemRepository.findByProductId(productId);
    }

    @Override
    @Transactional
    public InvoiceItem updateInvoiceItem(Long id, InvoiceItemDTO invoiceItemDTO) throws InvalidDataException, EntityNotFoundException {
        if (invoiceItemDTO.getUnitValue() == null || invoiceItemDTO.getUnitValue() <= 0) {
            throw new InvalidDataException("O valor unitário deve ser maior que 0.00.");

        } if (invoiceItemDTO.getQuantity() == null || invoiceItemDTO.getQuantity() <= 0) {
            throw new InvalidDataException("A quantidade de produtos deve ser maior que zero.");
        }

        InvoiceItem existingInvoiceItem = invoiceItemRepository.findById(id);

        if (existingInvoiceItem == null) {
            throw new EntityNotFoundException("Item não encontrado.");
        }

        existingInvoiceItem.setId(invoiceItemDTO.getId());
        existingInvoiceItem.setUnitValue(invoiceItemDTO.getUnitValue());
        existingInvoiceItem.setQuantity(invoiceItemDTO.getQuantity());
        Invoice invoice = existingInvoiceItem.getInvoice();
        invoiceService.updateTotalValue(invoice);

        return existingInvoiceItem;
    }

    @Override
    @Transactional
    public void deleteInvoiceItem(Long id) throws EntityNotFoundException {
        InvoiceItem invoiceItem = invoiceItemRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado."));

        Invoice invoice = invoiceItem.getInvoice();

        invoiceItemRepository.delete(invoiceItem);

        if (invoice != null) {
            invoice.getItems().remove(invoiceItem);
            invoiceService.updateTotalValue(invoice);
        }
    }
}