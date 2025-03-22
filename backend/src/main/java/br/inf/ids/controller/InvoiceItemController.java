package br.inf.ids.controller;

import br.inf.ids.dto.InvoiceItemDTO;
import br.inf.ids.model.InvoiceItem;
import br.inf.ids.service.InvoiceItemService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/itens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvoiceItemController {

    @Inject
    InvoiceItemService invoiceItemService;

    @POST
    public Response createInvoiceItem(InvoiceItemDTO invoiceItemDTO) {
        if (invoiceItemDTO.getUnitValue() == null || invoiceItemDTO.getUnitValue() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O valor unitário deve ser positivo.")
                    .build();

        } if (invoiceItemDTO.getQuantity() == null || invoiceItemDTO.getQuantity() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A quantidade deve ser maior que zero.")
                    .build();

        } try {
            InvoiceItem invoiceItem = new InvoiceItem();

            invoiceItem.setUnitValue(invoiceItemDTO.getUnitValue());
            invoiceItem.setQuantity(invoiceItemDTO.getQuantity());

            invoiceItemService.createInvoiceItem(invoiceItem);

            return Response.status(Response.Status.CREATED).entity(invoiceItem).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao criar o item de fatura.")
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
                    .entity("Ocorreu um erro ao buscar o item de fatura.")
                    .build();
        }
    }

    @GET
    public Response getAllInvoiceItems() {
        try {
            List<InvoiceItem> invoiceItems = invoiceItemService.findAllInvoiceItems();

            if (invoiceItems.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Nenhum item de fatura encontrado.")
                        .build();

            }
            return Response.ok(invoiceItems).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao buscar os itens de fatura.")
                    .build();
        }
    }

    @GET
    @Path("/buscar-nf/{invoiceId}")
    public Response getInvoiceItemByInvoiceId(@PathParam("invoiceId") Long invoiceId) {
        List<InvoiceItem> invoiceItems = invoiceItemService.findInvoiceItemByInvoiceId(invoiceId);

        if (invoiceItems.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nenhum item de fatura encontrado para a fatura com ID " + invoiceId)
                    .build();

        }
        return Response.ok(invoiceItems).build();
    }

    @GET
    @Path("/buscar-produto/{productId}")
    public Response findInvoiceItemByProductId(@PathParam("productId") Long productId) {
        List<InvoiceItem> invoiceItems = invoiceItemService.findInvoiceItemByProductId(productId);

        if (invoiceItems.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nenhum item de fatura encontrado para o produto com ID " + productId)
                    .build();

        }
        return Response.ok(invoiceItems).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateInvoiceItem(@PathParam("id") Long id, InvoiceItemDTO invoiceItemDTO) {
        if (invoiceItemDTO.getUnitValue() == null || invoiceItemDTO.getUnitValue() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O valor unitário deve ser positivo.")
                    .build();

        } if (invoiceItemDTO.getQuantity() == null || invoiceItemDTO.getQuantity() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A quantidade deve ser maior que zero.")
                    .build();

        } try {
            InvoiceItem invoiceItem = new InvoiceItem();

            invoiceItem.setUnitValue(invoiceItemDTO.getUnitValue());
            invoiceItem.setQuantity(invoiceItemDTO.getQuantity());

            InvoiceItem updatedInvoiceItem = invoiceItemService.updateInvoiceItem(id, invoiceItem);

            if (updatedInvoiceItem == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Item da fatura não encontrado.")
                        .build();

            }
            return Response.ok(updatedInvoiceItem).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao atualizar o item da fatura.")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteInvoiceItem(@PathParam("id") Long id) {
        try {
            Optional<InvoiceItem> invoiceItem = invoiceItemService.findInvoiceItemById(id);

            if (invoiceItem.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Item da fatura não encontrado.")
                        .build();

            }
            invoiceItemService.deleteInvoiceItem(id);

            return Response.noContent().build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao excluir o item da fatura.")
                    .build();
        }
    }
}