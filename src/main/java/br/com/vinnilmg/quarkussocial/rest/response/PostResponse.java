package br.com.vinnilmg.quarkussocial.rest.response;

import br.com.vinnilmg.quarkussocial.domain.model.Post;

import java.time.LocalDateTime;

public record PostResponse(
        String text,
        LocalDateTime datetime
) {
    public static PostResponse fromEntity(final Post post) {
        return new PostResponse(post.getText(), post.getDatetime());
    }
}
