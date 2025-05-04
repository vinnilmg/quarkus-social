package br.com.vinnilmg.quarkussocial.rest.response;

import br.com.vinnilmg.quarkussocial.domain.model.Follower;

public record FollowerResponse(Long id, String name) {
    public static FollowerResponse fromEntity(final Follower follower) {
        return new FollowerResponse(follower.getId(), follower.getFollower().getName());
    }
}
