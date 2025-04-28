package br.com.vinnilmg.quarkussocial.service;

import br.com.vinnilmg.quarkussocial.domain.model.User;
import br.com.vinnilmg.quarkussocial.repository.UserRepository;
import br.com.vinnilmg.quarkussocial.rest.request.CreateUserRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

import static java.util.Objects.nonNull;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Inject
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll() {
        return repository.findAll()
                .list();
    }

    @Override
    public User findById(final Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public User create(final CreateUserRequest request) {
        final var user = new User();
        user.setName(request.name());
        user.setAge(request.age());

        repository.persist(user);

        return user;
    }

    @Override
    @Transactional
    public User update(final Long id, final CreateUserRequest request) {
        final var user = findById(id);

        if (nonNull(user)) {
            user.setName(request.name());
            user.setAge(request.age());

            return user;
        }

        return null;
    }

    @Override
    @Transactional
    public boolean delete(final Long id) {
        final var user = findById(id);

        if (nonNull(user)) {
            repository.delete(user);
            return true;
        }

        return false;
    }
}
