package br.com.vinnilmg.quarkussocial.service.impl;

import br.com.vinnilmg.quarkussocial.domain.model.Post;
import br.com.vinnilmg.quarkussocial.repository.PostRepository;
import br.com.vinnilmg.quarkussocial.repository.UserRepository;
import br.com.vinnilmg.quarkussocial.rest.request.CreatePostRequest;
import br.com.vinnilmg.quarkussocial.rest.response.PostResponse;
import br.com.vinnilmg.quarkussocial.service.PostService;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static java.util.Objects.isNull;

@ApplicationScoped
public class PostServiceImpl implements PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Inject
    public PostServiceImpl(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    @Transactional
    public Response create(final Long userId, final CreatePostRequest request) {
        final var user = userRepository.findById(userId);
        if (isNull(user)) return Response.status(NOT_FOUND).build();

        final var post = new Post();
        post.setText(request.text());
        post.setUser(user);

        postRepository.persist(post);

        final var response = PostResponse.fromEntity(post);
        return Response.status(CREATED)
                .entity(response)
                .build();
    }

    @Override
    public Response findAllByUser(final Long userId) {
        final var user = userRepository.findById(userId);
        if (isNull(user)) return Response.status(NOT_FOUND).build();

        final var postsByUser = postRepository.find(
                "user",
                Sort.by("datetime", Sort.Direction.Descending),
                user
        ).list();

        final var response = postsByUser.stream()
                .map(PostResponse::fromEntity)
                .toList();

        return Response.ok(response).build();
    }
}
