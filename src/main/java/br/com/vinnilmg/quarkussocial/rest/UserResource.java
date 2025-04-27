package br.com.vinnilmg.quarkussocial.rest;

import br.com.vinnilmg.quarkussocial.domain.model.User;
import br.com.vinnilmg.quarkussocial.rest.request.CreateUserRequest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/users")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class UserResource {

    @POST
    @Transactional
    public Response createUser(final CreateUserRequest request) {
        final var user = new User();
        user.setName(request.name());
        user.setAge(request.age());
        user.persist();

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers() {
        final var users = User.findAll();
        return Response.ok(users.list()).build();
    }
}
