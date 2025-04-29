package br.com.vinnilmg.quarkussocial.rest;

import br.com.vinnilmg.quarkussocial.rest.request.CreatePostRequest;
import br.com.vinnilmg.quarkussocial.service.PostService;
import br.com.vinnilmg.quarkussocial.service.impl.PostServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/users/{userId}/posts")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class PostResource {
    private final PostService service;

    @Inject
    public PostResource(PostServiceImpl service) {
        this.service = service;
    }

    @POST
    public Response savePost(@PathParam("userId") final Long userId, final CreatePostRequest request) {
        return service.create(userId, request);
    }

    @GET
    public Response listPosts(@PathParam("userId") final Long userId) {
        return service.findAllByUser(userId);
    }
}
