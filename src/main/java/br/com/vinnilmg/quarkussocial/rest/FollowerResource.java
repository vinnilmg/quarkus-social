package br.com.vinnilmg.quarkussocial.rest;

import br.com.vinnilmg.quarkussocial.rest.request.FollowUserRequest;
import br.com.vinnilmg.quarkussocial.service.FollowerService;
import br.com.vinnilmg.quarkussocial.service.impl.FollowerServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/users/{userId}/followers")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class FollowerResource {
    private final FollowerService service;

    @Inject
    public FollowerResource(FollowerServiceImpl service) {
        this.service = service;
    }

    @PUT
    public Response followUser(@PathParam("userId") final Long userId, final FollowUserRequest request) {
        return service.followUser(userId, request);
    }
}
