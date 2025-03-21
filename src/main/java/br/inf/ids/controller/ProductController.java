package br.inf.ids.controller;

import br.inf.ids.dto.ProductDTO;
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
        Product product = new Product();

        product.setDescription(productDTO.getDescription());
        product.setProductStatus(productDTO.getProductStatus());

        productService.createProduct(product);

        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {

        return productService.findProductById(id)
                .map(product -> Response.ok(product).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response getAllProducts() {
        List<Product> products = productService.findAllProducts();

        return Response.ok(products).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, ProductDTO productDTO) {
        Product product = new Product();

        product.setDescription(productDTO.getDescription());
        product.setProductStatus(productDTO.getProductStatus());

        Product updatedProduct = productService.updateProduct(id, product);

        return Response.ok(updatedProduct).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        productService.deleteProduct(id);

        return Response.noContent().build();
    }

    @GET
    @Path("/search")
    public Response searchProducts(@QueryParam("description") String description) {
        List<Product> products = productService.searchProducts(description);

        return Response.ok(products).build();
    }
}