package br.com.vinnilmg.quarkussocial.rest;

import br.com.vinnilmg.quarkussocial.domain.model.Follower;
import br.com.vinnilmg.quarkussocial.domain.model.User;
import br.com.vinnilmg.quarkussocial.repository.FollowerRepository;
import br.com.vinnilmg.quarkussocial.repository.UserRepository;
import br.com.vinnilmg.quarkussocial.rest.request.FollowUserRequest;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static jakarta.ws.rs.core.Response.Status.CONFLICT;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    private UserRepository userRepository;
    @Inject
    private FollowerRepository followerRepository;

    private Long userId;
    private Long followerId;

    @BeforeEach
    @Transactional
    void setup() {
        final var user = new User();
        user.setName("Fulano");
        user.setAge(18);
        userRepository.persist(user);
        this.userId = user.getId();

        final var follower = new User();
        follower.setName("Cicrano");
        follower.setAge(45);
        userRepository.persist(follower);
        this.followerId = follower.getId();

        final var followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setUser(user);
        followerRepository.persist(followerEntity);
    }

    @Test
    @DisplayName("should return 409 when followerId is equal to userId")
    void saveUserAsFollowerTest() {
        final var request = new FollowUserRequest(userId);

        given()
                .contentType(JSON)
                .body(request)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(CONFLICT.getStatusCode())
                .body(Matchers.is("You can't follow yourself"));
    }

    @Test
    @DisplayName("should return 404 on follow user when user does not exists")
    void userNotFoundWhenTryingToFollowTest() {
        final var request = new FollowUserRequest(userId);
        final var inexistentUserId = 1000;

        given()
                .contentType(JSON)
                .body(request)
                .pathParam("userId", inexistentUserId)
                .when()
                .put()
                .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should follow an user")
    void followUserTest() {
        final var request = new FollowUserRequest(followerId);

        given()
                .contentType(JSON)
                .body(request)
                .pathParam("userId", userId)
                .when()
                .put()
                .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("should return 404 on list user followers when user does not exists")
    void userNotFoundWhenListingFollowersTest() {
        final var inexistentUserId = 1000;

        given()
                .contentType(JSON)
                .pathParam("userId", inexistentUserId)
                .when()
                .get()
                .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should list a user followers")
    void listFollowersTest() {
        final var response = given()
                .contentType(JSON)
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .extract()
                .response();

        assertEquals(OK.getStatusCode(), response.getStatusCode());

        final var followerCount = response.jsonPath().get("followerCount");
        final var followerContent = response.jsonPath().getList("content");

        assertEquals(1, followerCount);
        assertEquals(1, followerContent.size());
    }

    @Test
    @DisplayName("should return 404 on unfollow user when user does not exists")
    void userNotFoundWhenUnfollowUserTest() {
        final var inexistentUserId = 1000;

        given()
                .contentType(JSON)
                .pathParam("userId", inexistentUserId)
                .queryParam("followerId", followerId)
                .when()
                .delete()
                .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should unfollow an user")
    void unfollowUserTest() {
        given()
                .pathParam("userId", userId)
                .queryParam("followerId", followerId)
                .when()
                .delete()
                .then()
                .statusCode(NO_CONTENT.getStatusCode());
    }
}
