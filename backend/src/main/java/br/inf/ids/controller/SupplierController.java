package br.inf.ids.controller;

import br.inf.ids.dto.SupplierDTO;
import br.inf.ids.model.Supplier;
import br.inf.ids.service.SupplierService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Path("/fornecedores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Fornecedores", description = "Gerenciamento de fornecedores")
public class SupplierController {

    @Inject
    SupplierService supplierService;

    @POST
    @Operation(summary = "Criar um fornecedor", description = "Cria um novo fornecedor no sistema.")
    @APIResponse(responseCode = "201", description = "Fornecedor criado com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Supplier.class)))
    @APIResponse(responseCode = "400", description = "Erro ao criar o fornecedor")
    public Response createSupplier(SupplierDTO supplierDTO) {
        try {
            Supplier supplier = supplierService.createSupplier(supplierDTO);
            return Response.status(Response.Status.CREATED).entity(supplier).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar fornecedor por ID", description = "Retorna um fornecedor específico pelo ID.")
    @APIResponse(responseCode = "200", description = "Fornecedor encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Supplier.class)))
    @APIResponse(responseCode = "404", description = "Fornecedor não encontrado")
    public Response getSupplierById(@PathParam("id")
                                    @Parameter(description = "ID do fornecedor", required = true) Long id) {
        try {
            Supplier supplier = supplierService.findSupplierById(id);
            return Response.ok(supplier).build();

        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Listar todos os fornecedores",
            description = "Retorna a lista de fornecedores cadastrados.")
    @APIResponse(responseCode = "200", description = "Lista de fornecedores retornada com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Supplier.class)))
    public Response getAllSuppliers() {
        List<Supplier> suppliers = supplierService.findAllSuppliers();
        return Response.ok(suppliers).build();
    }

    @GET
    @Path("/buscar")
    @Operation(summary = "Buscar fornecedores pelo nome",
            description = "Retorna a lista de fornecedores que possuem um nome específico.")
    @APIResponse(responseCode = "200", description = "Fornecedores encontrados",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Supplier.class)))
    public Response getSupplierByName(@QueryParam("companyName")
                                      @Parameter(description = "Nome da empresa", required = true)
                                          String companyName) {
        try {
            List<Supplier> suppliers = supplierService.findSuppliersByName(companyName);
            return Response.ok(suppliers).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar um fornecedor", description = "Atualiza os dados de um fornecedor pelo ID.")
    @APIResponse(responseCode = "200", description = "Fornecedor atualizado com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Supplier.class)))
    @APIResponse(responseCode = "400", description = "Erro ao atualizar o fornecedor")
    public Response updateSupplier(@PathParam("id")
                                   @Parameter(description = "ID do fornecedor", required = true)
                                       Long id, SupplierDTO supplierDTO) {
        try {
            Supplier updatedSupplier = supplierService.updateSupplier(id, supplierDTO);
            return Response.ok(updatedSupplier).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir um fornecedor", description = "Remove um fornecedor do sistema pelo ID.")
    @APIResponse(responseCode = "204", description = "Fornecedor excluído com sucesso")
    @APIResponse(responseCode = "400", description = "Erro ao excluir o fornecedor")
    public Response deleteSupplier(@PathParam("id")
                                   @Parameter(description = "ID do fornecedor", required = true) Long id) {
        try {
            supplierService.deleteSupplier(id);
            return Response.noContent().build();
            
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
