package br.com.vinnilmg.quarkussocial.repository;

import br.com.vinnilmg.quarkussocial.domain.model.Follower;
import br.com.vinnilmg.quarkussocial.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean follows(final User follower, final User user) {
        final var params = Parameters
                .with("follower", follower)
                .and("user", user)
                .map();

        final var query = find("follower = :follower and user = :user", params);

        return query.firstResultOptional()
                .isPresent();
    }

    public List<Follower> findByUser(final Long userId) {
        return find("user.id", userId)
                .list();
    }
}
