package br.com.vinnilmg.quarkussocial.rest;

import br.com.vinnilmg.quarkussocial.rest.request.CreateUserRequest;
import br.com.vinnilmg.quarkussocial.service.UserService;
import br.com.vinnilmg.quarkussocial.service.UserServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/users")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class UserResource {
    private final UserService service;

    @Inject
    public UserResource(UserServiceImpl service) {
        this.service = service;
    }

    @POST
    public Response createUser(final CreateUserRequest request) {
        return service.create(request);
    }

    @GET
    public Response listAllUsers() {
        return service.findAll();
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") final Long userId) {
        return service.delete(userId);
    }

    @PUT
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") final Long userId, final CreateUserRequest request) {
        return service.update(userId, request);
    }
}
