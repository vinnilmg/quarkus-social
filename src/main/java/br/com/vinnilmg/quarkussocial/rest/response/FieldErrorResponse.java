package br.com.vinnilmg.quarkussocial.rest.response;

public record FieldErrorResponse(
        String field,
        String message
) {
}
