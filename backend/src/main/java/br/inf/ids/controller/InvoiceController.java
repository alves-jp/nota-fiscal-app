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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import java.util.List;

@Path("/notas-fiscais")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Notas Fiscais", description = "Gerenciamento de notas fiscais")
public class InvoiceController {

    @Inject
    InvoiceService invoiceService;

    @POST
    @Operation(summary = "Criar uma nova nota fiscal",
            description = "Cria uma nova nota fiscal e retorna os detalhes.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Nota fiscal criada com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response createInvoice(InvoiceDTO invoiceDTO) {
        try {
            Invoice invoice = invoiceService.createInvoice(invoiceDTO);
            return Response.status(Response.Status.CREATED).entity(invoice).build();

        } catch (InvalidDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar nota fiscal.").build();
        }
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar nota fiscal por ID",
            description = "Retorna os detalhes de uma nota fiscal pelo seu ID.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Nota fiscal encontrada"),
            @APIResponse(responseCode = "404", description = "Nota fiscal não encontrada"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response getInvoiceById(@PathParam("id") @Parameter(description = "ID da nota fiscal") Long id) {
        try {
            InvoiceResponseDTO responseDTO = invoiceService.getInvoiceById(id);
            return Response.ok(responseDTO).build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar nota fiscal.").build();
        }
    }

    @GET
    @Operation(summary = "Listar todas as notas fiscais",
            description = "Retorna uma lista de todas as notas fiscais cadastradas.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Lista de notas fiscais"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response getAllInvoices() {
        try {
            List<InvoiceResponseDTO> responseDTOs = invoiceService.findAllInvoices();
            return Response.ok(responseDTOs).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar notas fiscais.").build();
        }
    }

    @GET
    @Path("/buscar")
    @Operation(summary = "Buscar notas fiscais por número",
            description = "Retorna uma lista de notas fiscais pelo número da nota.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Notas fiscais encontradas"),
            @APIResponse(responseCode = "400", description = "Parâmetro inválido"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response getInvoiceByNumber(@QueryParam("invoiceNumber")
                                           @Parameter(description = "Número da nota fiscal") String invoiceNumber) {
        try {
            List<Invoice> invoices = invoiceService.findInvoiceByNumber(invoiceNumber);
            return Response.ok(invoices).build();

        } catch (InvalidDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar nota fiscal.").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar nota fiscal", description = "Atualiza os dados de uma nota fiscal existente.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Nota fiscal atualizada com sucesso"),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "404", description = "Nota fiscal não encontrada"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response updateInvoice(@PathParam("id")
                                      @Parameter(description = "ID da nota fiscal") Long id, InvoiceDTO invoiceDTO) {
        try {
            Invoice updatedInvoice = invoiceService.updateInvoice(id, invoiceDTO);
            return Response.ok(updatedInvoice).build();

        } catch (InvalidDataException | EntityNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar nota fiscal.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir nota fiscal", description = "Remove uma nota fiscal pelo ID.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Nota fiscal excluída com sucesso"),
            @APIResponse(responseCode = "404", description = "Nota fiscal não encontrada"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response deleteInvoice(@PathParam("id") @Parameter(description = "ID da nota fiscal") Long id) {
        try {
            invoiceService.deleteInvoice(id);
            return Response.noContent().build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao excluir nota fiscal.").build();
        }
    }
}
