package br.inf.ids.controller;

import br.inf.ids.dto.ProductDTO;
import br.inf.ids.exception.BusinessException;
import br.inf.ids.exception.EntityNotFoundException;
import br.inf.ids.model.Product;
import br.inf.ids.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    ProductService productService;

    @POST
    public Response createProduct(ProductDTO productDTO) {
        if (productDTO.getDescription() == null || productDTO.getDescription().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A descrição do produto é obrigatória.")
                    .build();

        } if (productDTO.getProductStatus() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O status do produto é obrigatório.")
                    .build();

        } if (productDTO.getProductCode() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O código do produto é obrigatório.")
                    .build();

        } try {
            Product product = new Product();

            product.setProductCode(productDTO.getProductCode());
            product.setDescription(productDTO.getDescription());
            product.setProductStatus(productDTO.getProductStatus());

            productService.createProduct(product);

            return Response.status(Response.Status.CREATED).entity(product).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar o produto.")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        return productService.findProductById(id)
                .map(product -> Response.ok(product).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Produto não encontrado.")
                        .build());
    }

    @GET
    public Response getAllProducts() {
        List<Product> products = productService.findAllProducts();

        return Response.ok(products).build();
    }

    @GET
    @Path("/buscar")
    public Response getProductByCode(@QueryParam("productCode") String productCode) {
        if (productCode == null || productCode.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O código do produto é obrigatório para a busca.")
                    .build();

        }
        List<Product> products = productService.findProductByCode(productCode);

        return Response.ok(products).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, ProductDTO productDTO) {
        if (productDTO.getDescription() == null || productDTO.getDescription().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A descrição do produto é obrigatória.")
                    .build();

        } if (productDTO.getProductStatus() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O status do produto é obrigatório.")
                    .build();

        } if (productDTO.getProductCode() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O código do produto é obrigatório.")
                    .build();

        } try {
            Product product = new Product();

            product.setProductCode(productDTO.getProductCode());
            product.setDescription(productDTO.getDescription());
            product.setProductStatus(productDTO.getProductStatus());

            Product updatedProduct = productService.updateProduct(id, product);

            if (updatedProduct == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Produto não encontrado.")
                        .build();

            }
            return Response.ok(updatedProduct).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar o produto.")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        try {
            productService.deleteProduct(id);

            return Response.noContent().build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();

        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao excluir o produto.")
                    .build();
        }
    }
}
