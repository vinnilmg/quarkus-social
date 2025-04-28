package br.com.vinnilmg.quarkussocial.rest.response;

import jakarta.validation.ConstraintViolation;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Set;

public record ErrorResponse(
        String message,
        List<FieldErrorResponse> errors
) {
    private static final String VALIDATION_ERROR = "Validation Error";
    public static final int UNPROCESSABLE_ENTITY_STATUS_CODE = 422;

    public static <T> ErrorResponse createFromValidation(
            final Set<ConstraintViolation<T>> violations
    ) {
        final var errors = violations.stream()
                .map(cv -> new FieldErrorResponse(cv.getPropertyPath().toString(), cv.getMessage()))
                .toList();

        return new ErrorResponse(VALIDATION_ERROR, errors);
    }

    public Response withStatusCode(final int code) {
        return Response.status(code)
                .entity(this)
                .build();
    }
}
