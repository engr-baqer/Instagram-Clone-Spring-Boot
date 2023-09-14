package devsinc.Instagram.clone.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CommentRequest {
    @NotNull(message = "Post ID cannot be null!")
    @Min(value = 1, message = "Post id must be greater than 1!")
    private Long postId;

    @NotBlank(message = "Comment cannot be empty or null!")
    @Size(min = 1, max = 500, message = "Comment size must between 1 to 500 characters!")
    private String commentText;
}
