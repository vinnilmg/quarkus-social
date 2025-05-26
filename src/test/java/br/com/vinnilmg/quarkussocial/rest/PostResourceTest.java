package br.com.vinnilmg.quarkussocial.rest;

import br.com.vinnilmg.quarkussocial.domain.model.Follower;
import br.com.vinnilmg.quarkussocial.domain.model.Post;
import br.com.vinnilmg.quarkussocial.domain.model.User;
import br.com.vinnilmg.quarkussocial.repository.FollowerRepository;
import br.com.vinnilmg.quarkussocial.repository.PostRepository;
import br.com.vinnilmg.quarkussocial.repository.UserRepository;
import br.com.vinnilmg.quarkussocial.rest.request.CreatePostRequest;
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
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.CREATED;
import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.OK;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
class PostResourceTest {

    @Inject
    private UserRepository userRepository;
    @Inject
    private FollowerRepository followerRepository;
    @Inject
    private PostRepository postRepository;

    private Long userId;
    private Long otherUserId;
    private Long followerId;

    @BeforeEach
    @Transactional
    void setup() {
        final var user = new User();
        user.setName("Fulano");
        user.setAge(18);
        userRepository.persist(user);
        this.userId = user.getId();

        final var post = new Post();
        post.setText("Some text");
        post.setUser(user);
        postRepository.persist(post);

        final var otherUser = new User();
        otherUser.setName("Beltrano");
        otherUser.setAge(30);
        userRepository.persist(otherUser);
        this.otherUserId = otherUser.getId();

        final var userFollower = new User();
        userFollower.setName("Sicrano");
        userFollower.setAge(50);
        userRepository.persist(userFollower);
        this.followerId = userFollower.getId();

        final var follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);
    }

    @Test
    @DisplayName("should create a post for a user")
    void createPostTest() {
        final var request = new CreatePostRequest("Some text");

        given()
                .contentType(JSON)
                .body(request)
                .pathParam("userId", userId)
                .when()
                .post()
                .then()
                .statusCode(CREATED.getStatusCode());
    }

    @Test
    @DisplayName("should return 404 when trying to make a post for an inexistent user")
    void postForAnInexistentUserText() {
        final var request = new CreatePostRequest("Some text");
        final var inexistentUserId = 1000;

        given()
                .contentType(JSON)
                .body(request)
                .pathParam("userId", inexistentUserId)
                .when()
                .post()
                .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should return 404 when user does not exist")
    void listPostUserNotFoundTest() {
        final var inexistentUserId = 1000;
        final var followerId = 1;

        given()
                .pathParam("userId", inexistentUserId)
                .header("followerId", followerId)
                .when()
                .get()
                .then()
                .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should return 400 when followerId header is not present")
    void listPostFollowerHeaderNotSendTest() {
        given()
                .pathParam("userId", userId)
                .when()
                .get()
                .then()
                .statusCode(BAD_REQUEST.getStatusCode())
                .body(Matchers.is("You forgot the header followerId"));
    }

    @Test
    @DisplayName("should return 404 when follower does not exist")
    void listPostFollowerNotFoundTest() {
        final var inexistentFollowerId = 1000;

        given()
                .pathParam("userId", userId)
                .header("followerId", inexistentFollowerId)
                .when()
                .get()
                .then()
                .statusCode(BAD_REQUEST.getStatusCode())
                .body(Matchers.is("Follower is not found"));
    }

    @Test
    @DisplayName("should return 403 when follower does not follow user")
    void listPostNotAFollowerTest() {
        given()
                .pathParam("userId", userId)
                .header("followerId", otherUserId)
                .when()
                .get()
                .then()
                .statusCode(FORBIDDEN.getStatusCode())
                .body(Matchers.is("You don't follow this user"));
    }

    @Test
    @DisplayName("should return posts")
    void listPostsTest() {
        given()
                .pathParam("userId", userId)
                .header("followerId", followerId)
                .when()
                .get()
                .then()
                .statusCode(OK.getStatusCode())
                .body("size()", Matchers.is(1));
    }
}
