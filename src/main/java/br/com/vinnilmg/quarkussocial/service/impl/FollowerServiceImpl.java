package br.com.vinnilmg.quarkussocial.service.impl;

import br.com.vinnilmg.quarkussocial.domain.model.Follower;
import br.com.vinnilmg.quarkussocial.repository.FollowerRepository;
import br.com.vinnilmg.quarkussocial.repository.UserRepository;
import br.com.vinnilmg.quarkussocial.rest.request.FollowUserRequest;
import br.com.vinnilmg.quarkussocial.rest.response.FollowersPerUserResponse;
import br.com.vinnilmg.quarkussocial.service.FollowerService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import static java.util.Objects.isNull;

@ApplicationScoped
public class FollowerServiceImpl implements FollowerService {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    @Inject
    public FollowerServiceImpl(UserRepository userRepository, FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.followerRepository = followerRepository;
    }

    @Override
    @Transactional
    public Response followUser(final Long userId, final FollowUserRequest request) {
        final var user = userRepository.findById(userId);
        final var follower = userRepository.findById(request.followerId());

        if (isNull(user) || isNull(follower)) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (user.getId().equals(follower.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("You can't follow yourself")
                    .build();
        }

        if (!followerRepository.follows(follower, user)) {
            final var followerEntity = new Follower();
            followerEntity.setUser(user);
            followerEntity.setFollower(follower);

            followerRepository.persist(followerEntity);
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response listFollowersByUser(final Long userId) {
        final var user = userRepository.findById(userId);
        if (isNull(user)) return Response.status(Response.Status.NOT_FOUND).build();

        final var followers = followerRepository.findByUser(userId);
        final var response = FollowersPerUserResponse.fromEntities(followers);
        return Response.ok(response).build();
    }

    @Override
    @Transactional
    public Response unfollowUser(final Long userId, final Long followerId) {
        final var user = userRepository.findById(userId);
        if (isNull(user)) return Response.status(Response.Status.NOT_FOUND).build();

        followerRepository.deleteByFollowerAndUser(followerId, userId);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
