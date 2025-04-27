package br.com.vinnilmg.quarkussocial.rest.request;

public record CreateUserRequest(
        String name,
        Integer age
) {
}
