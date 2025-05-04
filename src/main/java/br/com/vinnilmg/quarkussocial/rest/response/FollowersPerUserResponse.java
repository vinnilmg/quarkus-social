package br.com.vinnilmg.quarkussocial.rest.response;

import br.com.vinnilmg.quarkussocial.domain.model.Follower;

import java.util.List;

public record FollowersPerUserResponse(
        Integer followerCount,
        List<FollowerResponse> content
) {
    public static FollowersPerUserResponse fromEntities(final List<Follower> followers) {
        final var followerQuantity = followers.size();
        final var followersResponse = followers.stream()
                .map(FollowerResponse::fromEntity)
                .toList();

        return new FollowersPerUserResponse(followerQuantity, followersResponse);
    }
}
