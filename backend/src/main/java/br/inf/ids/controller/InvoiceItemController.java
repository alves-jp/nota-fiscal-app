package br.inf.ids.controller;

import br.inf.ids.dto.InvoiceItemDTO;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.exception.InvalidDataException;
import br.inf.ids.model.InvoiceItem;
import br.inf.ids.service.InvoiceItemService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.Operation;

@Path("/itens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Itens da Nota Fiscal", description = "Gerenciamento de itens inseridos em notas fiscais")
public class InvoiceItemController {

    @Inject
    InvoiceItemService invoiceItemService;

    @POST
    @Operation(summary = "Cria um novo item", description = "Adiciona um item a uma nota fiscal existente.")
    @APIResponses({
            @APIResponse(responseCode = "201", description = "Item criado com sucesso",
                    content = @Content(schema = @Schema(implementation = InvoiceItem.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response createInvoiceItem(InvoiceItemDTO invoiceItemDTO) {
        try {
            InvoiceItem invoiceItem = invoiceItemService.createInvoiceItem(invoiceItemDTO);
            return Response.status(Response.Status.CREATED).entity(invoiceItem).build();

        } catch (InvalidDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao criar o item.").build();
        }
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtém um item pelo ID", description = "Retorna os detalhes de um item com base no seu ID.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Item encontrado",
                    content = @Content(schema = @Schema(implementation = InvoiceItem.class))),
            @APIResponse(responseCode = "404", description = "Item não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response getInvoiceItemById(@PathParam("id") @Parameter(description = "ID do item") Long id) {
        try {
            return invoiceItemService.findInvoiceItemById(id)
                    .map(invoiceItem -> Response.ok(invoiceItem).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).build());

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao buscar o item.").build();
        }
    }

    @GET
    @Operation(summary = "Lista todos os itens inseridos em notas fiscais",
            description = "Retorna uma lista de todos os itens cadastrados.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Lista de itens retornada com sucesso",
                    content = @Content(schema = @Schema(implementation = InvoiceItem.class))),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response getAllInvoiceItems() {
        try {
            List<InvoiceItem> invoiceItems = invoiceItemService.findAllInvoiceItems();
            return Response.ok(invoiceItems).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao buscar os itens.").build();
        }
    }

    @GET
    @Path("/buscar-nf/{invoiceId}")
    @Operation(summary = "Obtém itens pelo ID de uma nota fiscal",
            description = "Retorna todos os itens pertencentes a uma nota fiscal específica.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Itens encontrados",
                    content = @Content(schema = @Schema(implementation = InvoiceItem.class))),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response getInvoiceItemByInvoiceId(@PathParam("invoiceId")
                                                  @Parameter(description = "ID da nota fiscal") Long invoiceId) {
        try {
            List<InvoiceItem> invoiceItems = invoiceItemService.findInvoiceItemByInvoiceId(invoiceId);
            return Response.ok(invoiceItems).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao buscar os itens.").build();
        }
    }

    @GET
    @Path("/buscar-produto/{productId}")
    @Operation(summary = "Obtém itens pelo ID do produto",
            description = "Retorna todos os itens relacionados a um produto específico.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Itens encontrados",
                    content = @Content(schema = @Schema(implementation = InvoiceItem.class))),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response findInvoiceItemByProductId(@PathParam("productId")
                                                   @Parameter(description = "ID do produto") Long productId) {
        try {
            List<InvoiceItem> invoiceItems = invoiceItemService.findInvoiceItemByProductId(productId);
            return Response.ok(invoiceItems).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao buscar os itens.").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualiza um item", description = "Modifica os dados de um item específico.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "Item atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = InvoiceItem.class))),
            @APIResponse(responseCode = "400", description = "Dados inválidos"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response updateInvoiceItem(@PathParam("id")
                                          @Parameter(description = "ID do item")
                                          Long id, InvoiceItemDTO invoiceItemDTO) {
        try {
            InvoiceItem updatedInvoiceItem = invoiceItemService.updateInvoiceItem(id, invoiceItemDTO);
            return Response.ok(updatedInvoiceItem).build();

        } catch (InvalidDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao atualizar o item.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Exclui um item", description = "Remove um item específico do banco de dados.")
    @APIResponses({
            @APIResponse(responseCode = "204", description = "Item excluído com sucesso"),
            @APIResponse(responseCode = "404", description = "Item não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public Response deleteInvoiceItem(@PathParam("id") @Parameter(description = "ID do item") Long id) {
        try {
            invoiceItemService.deleteInvoiceItem(id);
            return Response.noContent().build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Ocorreu um erro ao excluir o item.").build();
        }
    }
}
