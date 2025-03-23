package br.inf.ids.config;

import br.inf.ids.model.*;
import br.inf.ids.model.enums.CompanyStatus;
import br.inf.ids.model.enums.ProductStatus;
import br.inf.ids.repository.InvoiceItemRepository;
import br.inf.ids.repository.InvoiceRepository;
import br.inf.ids.repository.ProductRepository;
import br.inf.ids.repository.SupplierRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DataInitializer {

    @Inject
    SupplierRepository supplierRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    InvoiceRepository invoiceRepository;

    @Inject
    InvoiceItemRepository invoiceItemRepository;

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        if (supplierRepository.count() == 0 && productRepository.count() == 0) {
            Supplier supplier1 = new Supplier();
            supplier1.setSupplierCode("F-2025-00001");
            supplier1.setCompanyName("Fornecedor ABC");
            supplier1.setsupplierEmail("fornecedor.abc@mail.com");
            supplier1.setsupplierPhone("11999999999");
            supplier1.setCnpj("12345678000199");
            supplier1.setCompanyStatus(CompanyStatus.ACTIVE);
            supplierRepository.persist(supplier1);

            Supplier supplier2 = new Supplier();
            supplier2.setSupplierCode("F-2025-00002");
            supplier2.setCompanyName("123 Empreitadas");
            supplier2.setsupplierEmail("123empreitadas@mail.com");
            supplier2.setsupplierPhone("21988888888");
            supplier2.setCnpj("98765432000199");
            supplier2.setCompanyStatus(CompanyStatus.ACTIVE);
            supplierRepository.persist(supplier2);


            Product product1 = new Product();
            product1.setProductCode("PROD-10000");
            product1.setDescription("Notebook Dell Inspiron");
            product1.setProductStatus(ProductStatus.ACTIVE);
            productRepository.persist(product1);

            Product product2 = new Product();
            product2.setProductCode("PROD-20000");
            product2.setDescription("Monitor LG 24 Polegadas");
            product2.setProductStatus(ProductStatus.ACTIVE);
            productRepository.persist(product2);


            Invoice invoice1 = new Invoice();
            invoice1.setInvoiceNumber("NF-123.456");
            invoice1.setIssueDate(LocalDateTime.now());
            invoice1.setSupplier(supplier1);
            invoice1.setAddress("Av. Brasil, 123");
            invoiceRepository.persist(invoice1);


            InvoiceItem item1 = new InvoiceItem();
            item1.setInvoice(invoice1);
            item1.setProduct(product1);
            item1.setUnitValue(500.00);
            item1.setQuantity(3);
            invoiceItemRepository.persist(item1);


            List<InvoiceItem> items = new ArrayList<>();
            items.add(item1);
            invoice1.setItems(items);

            invoiceRepository.persist(invoice1);

            System.out.println("Dados iniciais cadastrados com sucesso!");
        }
    }
}
