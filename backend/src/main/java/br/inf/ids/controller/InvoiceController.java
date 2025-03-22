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
        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setIssueDate(invoiceDTO.getIssueDate());
        invoice.setAddress(invoiceDTO.getAddress());
        invoice.setTotalValue(invoiceDTO.getTotalValue());

        invoiceService.createInvoice(invoice);

        return Response.status(Response.Status.CREATED).entity(invoice).build();
    }

    @GET
    @Path("/{id}")
    public Response getInvoiceById(@PathParam("id") Long id) {
        return invoiceService.findInvoiceById(id)
                .map(invoice -> Response.ok(invoice).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response getAllInvoices() {
        List<Invoice> invoices = invoiceService.findAllInvoices();

        return Response.ok(invoices).build();
    }

    @GET
    @Path("/buscar")
    public Response searchInvoices(@QueryParam("invoiceNumber") String invoiceNumber) {
        List<Invoice> invoices = invoiceService.findInvoiceByNumber(invoiceNumber);

        return Response.ok(invoices).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateInvoice(@PathParam("id") Long id, InvoiceDTO invoiceDTO) {
        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setIssueDate(invoiceDTO.getIssueDate());
        invoice.setAddress(invoiceDTO.getAddress());
        invoice.setTotalValue(invoiceDTO.getTotalValue());

        Invoice updatedInvoice = invoiceService.updateInvoice(id, invoice);

        return Response.ok(updatedInvoice).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteInvoice(@PathParam("id") Long id) {
        invoiceService.deleteInvoice(id);

        return Response.noContent().build();
    }
}