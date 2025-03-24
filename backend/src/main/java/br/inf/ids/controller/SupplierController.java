package br.inf.ids.controller;

import br.inf.ids.dto.SupplierDTO;
import br.inf.ids.model.Supplier;
import br.inf.ids.service.SupplierService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/fornecedores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SupplierController {

    @Inject
    SupplierService supplierService;

    @POST
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
    public Response getSupplierById(@PathParam("id") Long id) {
        try {
            Supplier supplier = supplierService.findSupplierById(id);
            return Response.ok(supplier).build();

        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response getAllSuppliers() {
        List<Supplier> suppliers = supplierService.findAllSuppliers();
        return Response.ok(suppliers).build();
    }

    @GET
    @Path("/buscar")
    public Response getSupplierByName(@QueryParam("companyName") String companyName) {
        try {
            List<Supplier> suppliers = supplierService.findSuppliersByName(companyName);
            return Response.ok(suppliers).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateSupplier(@PathParam("id") Long id, SupplierDTO supplierDTO) {
        try {
            Supplier updatedSupplier = supplierService.updateSupplier(id, supplierDTO);
            return Response.ok(updatedSupplier).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSupplier(@PathParam("id") Long id) {
        try {
            supplierService.deleteSupplier(id);
            return Response.noContent().build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}