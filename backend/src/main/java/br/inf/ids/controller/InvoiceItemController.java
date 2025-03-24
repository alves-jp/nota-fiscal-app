package br.inf.ids.controller;

import br.inf.ids.dto.InvoiceItemDTO;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.exception.InvalidDataException;
import br.inf.ids.exception.ItemAssignedException;
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
        try {
            InvoiceItem invoiceItem = invoiceItemService.createInvoiceItem(invoiceItemDTO);
            return Response.status(Response.Status.CREATED).entity(invoiceItem).build();

        } catch (InvalidDataException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao criar o item.")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getInvoiceItemById(@PathParam("id") Long id) {
        try {
            return invoiceItemService.findInvoiceItemById(id)
                    .map(invoiceItem -> Response.ok(invoiceItem).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao buscar o item.")
                    .build();
        }
    }

    @GET
    public Response getAllInvoiceItems() {
        try {
            List<InvoiceItem> invoiceItems = invoiceItemService.findAllInvoiceItems();
            return Response.ok(invoiceItems).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao buscar os itens.")
                    .build();
        }
    }

    @GET
    @Path("/buscar-nf/{invoiceId}")
    public Response getInvoiceItemByInvoiceId(@PathParam("invoiceId") Long invoiceId) {
        try {
            List<InvoiceItem> invoiceItems = invoiceItemService.findInvoiceItemByInvoiceId(invoiceId);
            return Response.ok(invoiceItems).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao buscar os itens.")
                    .build();
        }
    }

    @GET
    @Path("/buscar-produto/{productId}")
    public Response findInvoiceItemByProductId(@PathParam("productId") Long productId) {
        try {
            List<InvoiceItem> invoiceItems = invoiceItemService.findInvoiceItemByProductId(productId);
            return Response.ok(invoiceItems).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao buscar os itens.")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateInvoiceItem(@PathParam("id") Long id, InvoiceItemDTO invoiceItemDTO) {
        try {
            InvoiceItem updatedInvoiceItem = invoiceItemService.updateInvoiceItem(id, invoiceItemDTO);
            return Response.ok(updatedInvoiceItem).build();

        } catch (InvalidDataException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao atualizar o item.")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteInvoiceItem(@PathParam("id") Long id) {
        try {
            invoiceItemService.deleteInvoiceItem(id);
            return Response.noContent().build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao excluir o item.")
                    .build();
        }
    }
}