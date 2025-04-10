package br.inf.ids.controller;

import br.inf.ids.dto.ProductDTO;
import br.inf.ids.exception.BusinessException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Produtos", description = "Gerenciamento de produtos")
public class ProductController {

    @Inject
    ProductService productService;

    @POST
    @Operation(summary = "Criar um novo produto", description = "Cria um novo produto no sistema.")
    @APIResponse(responseCode = "201", description = "Produto criado com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProductDTO.class)))
    @APIResponse(responseCode = "400", description = "Erro ao criar o produto")
    public Response createProduct(ProductDTO productDTO) {
        try {
            ProductDTO createdProduct = productService.createProduct(productDTO);
            return Response.status(Response.Status.CREATED).entity(createdProduct).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo ID.")
    @APIResponse(responseCode = "200", description = "Produto encontrado",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProductDTO.class)))
    @APIResponse(responseCode = "404", description = "Produto não encontrado")
    public Response getProductById(@PathParam("id")
                                   @Parameter(description = "ID do produto", required = true) Long id) {
        try {
            ProductDTO product = productService.findProductById(id);
            return Response.ok(product).build();

        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Operation(summary = "Listar todos os produtos", description = "Retorna a lista de produtos cadastrados.")
    @APIResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProductDTO.class)))
    public Response getAllProducts() {
        List<ProductDTO> products = productService.findAllProducts();
        return Response.ok(products).build();
    }

    @GET
    @Path("/buscar")
    @Operation(summary = "Buscar produtos pelo código",
            description = "Retorna a lista de produtos que possuem um código específico.")
    @APIResponse(responseCode = "200", description = "Produtos encontrados",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProductDTO.class)))
    public Response getProductByCode(@QueryParam("productCode")
                                     @Parameter(description = "Código do produto", required = true)
                                     String productCode) {
        try {
            List<ProductDTO> products = productService.findProductByCode(productCode);
            return Response.ok(products).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Atualizar um produto", description = "Atualiza os dados de um produto pelo ID.")
    @APIResponse(responseCode = "200", description = "Produto atualizado com sucesso",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = ProductDTO.class)))
    @APIResponse(responseCode = "400", description = "Erro ao atualizar o produto")
    public Response updateProduct(@PathParam("id")
                                  @Parameter(description = "ID do produto", required = true)
                                  Long id, ProductDTO productDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
            return Response.ok(updatedProduct).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Excluir um produto", description = "Remove um produto do sistema pelo ID.")
    @APIResponse(responseCode = "204", description = "Produto excluído com sucesso")
    @APIResponse(responseCode = "400", description = "Erro ao excluir o produto")
    public Response deleteProduct(@PathParam("id")
                                  @Parameter(description = "ID do produto", required = true) Long id) {
        try {
            productService.deleteProduct(id);
            return Response.noContent().build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro interno ao excluir o produto").build();
        }
    }
}
