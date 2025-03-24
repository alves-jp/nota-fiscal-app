package br.inf.ids.controller;

import br.inf.ids.dto.InvoiceDTO;
import br.inf.ids.dto.InvoiceResponseDTO;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.exception.InvalidDataException;
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
        try {
            Invoice invoice = invoiceService.createInvoice(invoiceDTO);
            return Response.status(Response.Status.CREATED).entity(invoice).build();

        } catch (InvalidDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao criar nota fiscal.").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getInvoiceById(@PathParam("id") Long id) {
        try {
            InvoiceResponseDTO responseDTO = invoiceService.getInvoiceById(id);
            return Response.ok(responseDTO).build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar nota fiscal.").build();
        }
    }

    @GET
    public Response getAllInvoices() {
        try {
            List<InvoiceResponseDTO> responseDTOs = invoiceService.findAllInvoices();
            return Response.ok(responseDTOs).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar notas fiscais.").build();
        }
    }

    @GET
    @Path("/buscar")
    public Response getInvoiceByNumber(@QueryParam("invoiceNumber") String invoiceNumber) {
        try {
            List<Invoice> invoices = invoiceService.findInvoiceByNumber(invoiceNumber);
            return Response.ok(invoices).build();

        } catch (InvalidDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar nota fiscal.").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateInvoice(@PathParam("id") Long id, InvoiceDTO invoiceDTO) {
        try {
            Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceDTO);
            return Response.ok(updatedInvoice).build();

        } catch (InvalidDataException | EntityNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao atualizar nota fiscal.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteInvoice(@PathParam("id") Long id) {
        try {
            invoiceService.deleteInvoice(id);
            return Response.noContent().build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao excluir nota fiscal.").build();
        }
    }
}