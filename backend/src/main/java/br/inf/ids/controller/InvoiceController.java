package br.inf.ids.controller;

import br.inf.ids.dto.InvoiceDTO;
import br.inf.ids.model.Invoice;
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

    @POST
    public Response createInvoice(InvoiceDTO invoiceDTO) {
        if (invoiceDTO.getInvoiceNumber() == null || invoiceDTO.getInvoiceNumber().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O número da nota fiscal é obrigatório.")
                    .build();

        } if (invoiceDTO.getIssueDate() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A data de emissão é obrigatória.")
                    .build();

        } if (invoiceDTO.getTotalValue() == null || invoiceDTO.getTotalValue() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O valor total deve ser positivo.")
                    .build();

        } try {
            Invoice invoice = new Invoice();

            invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
            invoice.setIssueDate(invoiceDTO.getIssueDate());
            invoice.setAddress(invoiceDTO.getAddress());
            invoice.setTotalValue(invoiceDTO.getTotalValue());

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
                .map(invoice -> Response.ok(invoice).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Nota fiscal não encontrada.")
                        .build());
    }

    @GET
    public Response getAllInvoices() {
        List<Invoice> invoices = invoiceService.findAllInvoices();

        return Response.ok(invoices).build();
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

        } if (invoiceDTO.getTotalValue() == null || invoiceDTO.getTotalValue() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O valor total deve ser positivo.")
                    .build();

        } try {
            Invoice invoice = new Invoice();

            invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
            invoice.setIssueDate(invoiceDTO.getIssueDate());
            invoice.setAddress(invoiceDTO.getAddress());
            invoice.setTotalValue(invoiceDTO.getTotalValue());

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
