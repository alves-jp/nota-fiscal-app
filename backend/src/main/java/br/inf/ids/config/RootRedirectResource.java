package br.inf.ids.config;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class RootRedirectResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response redirectToDevUI() {
        return Response.status(Response.Status.FOUND)
                .location(java.net.URI.create("/q/dev-ui/welcome"))
                .build();
    }
}
