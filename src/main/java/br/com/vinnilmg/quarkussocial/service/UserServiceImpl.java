package br.com.vinnilmg.quarkussocial.service;

import br.com.vinnilmg.quarkussocial.domain.model.User;
import br.com.vinnilmg.quarkussocial.repository.UserRepository;
import br.com.vinnilmg.quarkussocial.rest.request.CreateUserRequest;
import br.com.vinnilmg.quarkussocial.rest.response.ErrorResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static java.util.Objects.nonNull;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final Validator validator;

    @Inject
    public UserServiceImpl(UserRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public Response findAll() {
        final var users = repository.findAll()
                .list();

        return Response.ok(users).build();
    }

    @Override
    public User findById(final Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Response create(final CreateUserRequest request) {
        final var violations = validator.validate(request);

        if (!violations.isEmpty()) {
            final var error = ErrorResponse.createFromValidation(violations);
            return Response.status(BAD_REQUEST)
                    .entity(error)
                    .build();
        }

        final var user = new User();
        user.setName(request.name());
        user.setAge(request.age());

        repository.persist(user);

        return Response.ok(user).build();
    }

    @Override
    @Transactional
    public Response update(final Long id, final CreateUserRequest request) {
        final var user = findById(id);

        if (nonNull(user)) {
            user.setName(request.name());
            user.setAge(request.age());

            return Response.ok(user).build();
        }

        return Response.status(NOT_FOUND).build();
    }

    @Override
    @Transactional
    public Response delete(final Long id) {
        final var user = findById(id);

        if (nonNull(user)) {
            repository.delete(user);

            return Response.noContent().build();
        }

        return Response.status(NOT_FOUND).build();
    }
}
