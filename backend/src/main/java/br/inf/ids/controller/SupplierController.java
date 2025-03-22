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
        Supplier supplier = new Supplier();

        supplier.setCompanyName(supplierDTO.getCompanyName());
        supplier.setsupplierEmail(supplierDTO.getsupplierEmail());
        supplier.setsupplierPhone(supplierDTO.getsupplierPhone());
        supplier.setCnpj(supplierDTO.getCnpj());
        supplier.setCompanyStatus(supplierDTO.getCompanyStatus());

        supplierService.createSupplier(supplier);

        return Response.status(Response.Status.CREATED).entity(supplier).build();
    }

    @GET
    @Path("/{id}")
    public Response getSupplierById(@PathParam("id") Long id) {
        return supplierService.findSupplierById(id)
                .map(supplier -> Response.ok(supplier).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    public Response getAllSuppliers() {
        List<Supplier> suppliers = supplierService.findAllSuppliers();

        return Response.ok(suppliers).build();
    }

    @GET
    @Path("/buscar")
    public Response getSupplierByName(@QueryParam("companyName") String companyName) {
        List<Supplier> suppliers = supplierService.findSuppliersByName(companyName);

        return Response.ok(suppliers).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateSupplier(@PathParam("id") Long id, SupplierDTO supplierDTO) {
        Supplier supplier = new Supplier();

        supplier.setCompanyName(supplierDTO.getCompanyName());
        supplier.setsupplierEmail(supplierDTO.getsupplierEmail());
        supplier.setsupplierPhone(supplierDTO.getsupplierPhone());
        supplier.setCnpj(supplierDTO.getCnpj());
        supplier.setCompanyStatus(supplierDTO.getCompanyStatus());

        Supplier updatedSupplier = supplierService.updateSupplier(id, supplier);

        return Response.ok(updatedSupplier).build();
    }


    @DELETE
    @Path("/{id}")
    public Response deleteSupplier(@PathParam("id") Long id) {
        supplierService.deleteSupplier(id);

        return Response.noContent().build();
    }
}