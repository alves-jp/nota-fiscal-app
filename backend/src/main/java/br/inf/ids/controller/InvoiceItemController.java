package br.inf.ids.controller;

import br.inf.ids.dto.InvoiceItemDTO;
import br.inf.ids.model.InvoiceItem;
import br.inf.ids.service.InvoiceItemService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/itens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvoiceItemController {

    @Inject
    InvoiceItemService invoiceItemService;

    @POST
    public Response createInvoiceItem(InvoiceItemDTO invoiceItemDTO) {
        InvoiceItem invoiceItem = new InvoiceItem();

        invoiceItem.setUnitValue(invoiceItemDTO.getUnitValue());
        invoiceItem.setQuantity(invoiceItemDTO.getQuantity());

        invoiceItemService.createInvoiceItem(invoiceItem);

        return Response.status(Response.Status.CREATED).entity(invoiceItem).build();
    }

    @GET
    @Path("/{id}")
    public Response getInvoiceItemById(@PathParam("id") Long id) {
        return invoiceItemService.findInvoiceItemById(id)
                .map(invoiceItem -> Response.ok(invoiceItem).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response getAllInvoiceItems() {
        List<InvoiceItem> invoiceItems = invoiceItemService.findAllInvoiceItems();

        return Response.ok(invoiceItems).build();
    }

    @GET
    @Path("/buscar-nf/{invoiceId}")
    public Response getInvoiceItemsByInvoiceId(@PathParam("invoiceId") Long invoiceId) {
        List<InvoiceItem> invoiceItems = invoiceItemService.findInvoiceItemsByInvoiceId(invoiceId);

        return Response.ok(invoiceItems).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateInvoiceItem(@PathParam("id") Long id, InvoiceItemDTO invoiceItemDTO) {
        InvoiceItem invoiceItem = new InvoiceItem();

        invoiceItem.setUnitValue(invoiceItemDTO.getUnitValue());
        invoiceItem.setQuantity(invoiceItemDTO.getQuantity());

        InvoiceItem updatedInvoiceItem = invoiceItemService.updateInvoiceItem(id, invoiceItem);

        return Response.ok(updatedInvoiceItem).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteInvoiceItem(@PathParam("id") Long id) {
        invoiceItemService.deleteInvoiceItem(id);

        return Response.noContent().build();
    }
}