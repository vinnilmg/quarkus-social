package br.com.vinnilmg.quarkussocial.service;

import br.com.vinnilmg.quarkussocial.rest.request.FollowUserRequest;
import jakarta.ws.rs.core.Response;

public interface FollowerService {
    Response followUser(Long userId, FollowUserRequest request);

    Response listFollowersByUser(Long userId);

    Response unfollowUser(Long userId, Long followerId);
}
