package br.com.vinnilmg.quarkussocial.service;

import br.com.vinnilmg.quarkussocial.domain.model.User;
import br.com.vinnilmg.quarkussocial.rest.request.CreateUserRequest;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findById(Long id);

    User create(CreateUserRequest request);

    User update(Long id, CreateUserRequest request);

    boolean delete(Long id);
}
