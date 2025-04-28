package br.com.vinnilmg.quarkussocial.service;

import br.com.vinnilmg.quarkussocial.domain.model.User;
import br.com.vinnilmg.quarkussocial.rest.request.CreateUserRequest;
import jakarta.ws.rs.core.Response;

public interface UserService {
    Response findAll();

    User findById(Long id);

    Response create(CreateUserRequest request);

    Response update(Long id, CreateUserRequest request);

    Response delete(Long id);
}
