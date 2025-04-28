package br.com.vinnilmg.quarkussocial.rest;

import br.com.vinnilmg.quarkussocial.domain.model.User;
import br.com.vinnilmg.quarkussocial.repository.UserRepository;
import br.com.vinnilmg.quarkussocial.rest.request.CreateUserRequest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
import static java.util.Objects.nonNull;

@Path("/users")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class UserResource {
    private final UserRepository repository;

    @Inject
    public UserResource(UserRepository repository) {
        this.repository = repository;
    }

    @POST
    @Transactional
    public Response createUser(final CreateUserRequest request) {
        final var user = new User();
        user.setName(request.name());
        user.setAge(request.age());

        repository.persist(user);

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers() {
        final var users = repository.findAll();
        return Response.ok(users.list()).build();
    }

    @DELETE
    @Path("/{userId}")
    @Transactional
    public Response deleteUser(@PathParam("userId") final Long userId) {
        final User user = repository.findById(userId);

        if (nonNull(user)) {
            repository.delete(user);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{userId}")
    @Transactional
    public Response updateUser(@PathParam("userId") final Long userId, final CreateUserRequest request) {
        final User user = repository.findById(userId);

        if (nonNull(user)) {
            user.setName(request.name());
            user.setAge(request.age());

            return Response.ok(user).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
