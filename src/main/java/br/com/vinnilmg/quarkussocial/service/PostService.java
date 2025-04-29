package br.com.vinnilmg.quarkussocial.service;

import br.com.vinnilmg.quarkussocial.rest.request.CreatePostRequest;
import jakarta.ws.rs.core.Response;

public interface PostService {
    Response create(Long userId, CreatePostRequest request);

    Response findAllByUser(Long userId);
}
