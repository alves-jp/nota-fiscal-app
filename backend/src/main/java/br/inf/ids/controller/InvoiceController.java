package br.inf.ids.controller;

import br.inf.ids.dto.InvoiceDTO;
import br.inf.ids.dto.InvoiceResponseDTO;
import br.inf.ids.model.Invoice;
import br.inf.ids.model.Supplier;
import br.inf.ids.repository.SupplierRepository;
import br.inf.ids.service.InvoiceService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/notas-fiscais")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvoiceController {

    @Inject
    InvoiceService invoiceService;

    @Inject
    SupplierRepository supplierRepository;

    @POST
    public Response createInvoice(InvoiceDTO invoiceDTO) {
        try {
            Supplier supplier = supplierRepository.findById(invoiceDTO.getSupplierId());

            if (supplier == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Fornecedor com ID " + invoiceDTO.getSupplierId() + " não encontrado.")
                        .build();

            }
            Invoice invoice = new Invoice();

            invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
            invoice.setIssueDate(invoiceDTO.getIssueDate());
            invoice.setAddress(invoiceDTO.getAddress());
            invoice.setSupplier(supplier);

            invoiceService.createInvoice(invoice);

            return Response.status(Response.Status.CREATED).entity(invoice).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar a nota fiscal.")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getInvoiceById(@PathParam("id") Long id) {
        return invoiceService.findInvoiceById(id)
                .map(invoice -> {
                    double totalValue = invoice.getItems().stream()
                            .mapToDouble(item -> item.getUnitValue() * item.getQuantity())
                            .sum();

                    InvoiceResponseDTO responseDTO = new InvoiceResponseDTO();

                    responseDTO.setId(invoice.getId());
                    responseDTO.setInvoiceNumber(invoice.getInvoiceNumber());
                    responseDTO.setIssueDate(invoice.getIssueDate());
                    responseDTO.setSupplier(invoice.getSupplier());
                    responseDTO.setAddress(invoice.getAddress());
                    responseDTO.setItems(invoice.getItems());
                    responseDTO.setTotalValue(totalValue);

                    return Response.ok(responseDTO).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Nota fiscal não encontrada.")
                        .build());
    }

    @GET
    public Response getAllInvoices() {
        List<Invoice> invoices = invoiceService.findAllInvoices();

        List<InvoiceResponseDTO> responseDTOs = invoices.stream()
                .map(invoice -> {
                    double totalValue = invoice.getItems().stream()
                            .mapToDouble(item -> item.getUnitValue() * item.getQuantity())
                            .sum();

                    InvoiceResponseDTO responseDTO = new InvoiceResponseDTO();

                    responseDTO.setId(invoice.getId());
                    responseDTO.setInvoiceNumber(invoice.getInvoiceNumber());
                    responseDTO.setIssueDate(invoice.getIssueDate());
                    responseDTO.setSupplier(invoice.getSupplier());
                    responseDTO.setAddress(invoice.getAddress());
                    responseDTO.setItems(invoice.getItems());
                    responseDTO.setTotalValue(totalValue);

                    return responseDTO;
                })
                .toList();

        return Response.ok(responseDTOs).build();
    }

    @GET
    @Path("/buscar")
    public Response getInvoiceByNumber(@QueryParam("invoiceNumber") String invoiceNumber) {
        if (invoiceNumber == null || invoiceNumber.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O número da nota fiscal é obrigatório para a busca.")
                    .build();

        }
        List<Invoice> invoices = invoiceService.findInvoiceByNumber(invoiceNumber);

        return Response.ok(invoices).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateInvoice(@PathParam("id") Long id, InvoiceDTO invoiceDTO) {
        if (invoiceDTO.getInvoiceNumber() == null || invoiceDTO.getInvoiceNumber().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O número da nota fiscal é obrigatório.")
                    .build();

        } if (invoiceDTO.getIssueDate() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A data de emissão é obrigatória.")
                    .build();

        } try {
            Invoice invoice = new Invoice();

            invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
            invoice.setIssueDate(invoiceDTO.getIssueDate());
            invoice.setAddress(invoiceDTO.getAddress());

            Invoice updatedInvoice = invoiceService.updateInvoice(id, invoice);

            if (updatedInvoice == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nota fiscal não encontrada.")
                        .build();
            }
            return Response.ok(updatedInvoice).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar a nota fiscal.")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteInvoice(@PathParam("id") Long id) {
        try {
            if (invoiceService.findInvoiceById(id).isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nota fiscal não encontrada.")
                        .build();

            }
            invoiceService.deleteInvoice(id);

            return Response.noContent().build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao excluir a nota fiscal.")
                    .build();
        }
    }
}