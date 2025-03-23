package br.inf.ids.controller;

import br.inf.ids.dto.SupplierDTO;
import br.inf.ids.exception.BusinessException;
import br.inf.ids.exception.EntityNotFoundException;
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
        if (supplierDTO.getCompanyName() == null || supplierDTO.getCompanyName().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A razão social do fornecedor é obrigatória.")
                    .build();

        } if (supplierDTO.getCnpj() == null || supplierDTO.getCnpj().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O CNPJ é obrigatório.")
                    .build();

        } if (supplierDTO.getCompanyStatus() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O status da empresa é obrigatório.")
                    .build();

        } if (supplierDTO.getSupplierCode() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O código do fornecedor é obrigatório.")
                    .build();

        } try {
            Supplier supplier = new Supplier();

            supplier.setSupplierCode(supplierDTO.getSupplierCode());
            supplier.setCompanyName(supplierDTO.getCompanyName());
            supplier.setsupplierEmail(supplierDTO.getsupplierEmail());
            supplier.setsupplierPhone(supplierDTO.getsupplierPhone());
            supplier.setCnpj(supplierDTO.getCnpj());
            supplier.setCompanyStatus(supplierDTO.getCompanyStatus());

            supplierService.createSupplier(supplier);

            return Response.status(Response.Status.CREATED).entity(supplier).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao criar novo fornecedor.")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getSupplierById(@PathParam("id") Long id) {
        return supplierService.findSupplierById(id)
                .map(supplier -> Response.ok(supplier).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity("Fornecedor não encontrado.")
                        .build());
    }

    @GET
    public Response getAllSuppliers() {
        List<Supplier> suppliers = supplierService.findAllSuppliers();

        return Response.ok(suppliers).build();
    }

    @GET
    @Path("/buscar")
    public Response getSupplierByName(@QueryParam("companyName") String companyName) {
        if (companyName == null || companyName.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A razão social do fornecedor é obrigatória para a busca.")
                    .build();

        }
        List<Supplier> suppliers = supplierService.findSuppliersByName(companyName);

        return Response.ok(suppliers).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateSupplier(@PathParam("id") Long id, SupplierDTO supplierDTO) {
        if (supplierDTO.getCompanyName() == null || supplierDTO.getCompanyName().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A razão social do fornecedor é obrigatória.")
                    .build();

        } if (supplierDTO.getCnpj() == null || supplierDTO.getCnpj().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O CNPJ do fornecedor é obrigatório.")
                    .build();

        } if (supplierDTO.getCompanyStatus() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O status da empresa é obrigatório.")
                    .build();

        } if (supplierDTO.getSupplierCode() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("O código do fornecedor é obrigatório.")
                    .build();

        } try {
            Supplier supplier = new Supplier();

            supplier.setSupplierCode(supplierDTO.getSupplierCode());
            supplier.setCompanyName(supplierDTO.getCompanyName());
            supplier.setsupplierEmail(supplierDTO.getsupplierEmail());
            supplier.setsupplierPhone(supplierDTO.getsupplierPhone());
            supplier.setCnpj(supplierDTO.getCnpj());
            supplier.setCompanyStatus(supplierDTO.getCompanyStatus());

            Supplier updatedSupplier = supplierService.updateSupplier(id, supplier);

            if (updatedSupplier == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Fornecedor não encontrado.")
                        .build();

            }
            return Response.ok(updatedSupplier).build();

        } catch (BusinessException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao atualizar o fornecedor.")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSupplier(@PathParam("id") Long id) {
        try {
            supplierService.deleteSupplier(id);

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
                    .entity("Erro ao excluir o fornecedor.")
                    .build();
        }
    }
}
