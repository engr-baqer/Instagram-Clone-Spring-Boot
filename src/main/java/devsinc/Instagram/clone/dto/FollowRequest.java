package devsinc.Instagram.clone.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FollowRequest {

    @NotNull(message = "Please enter your follower id!")
    @Min(value = 1, message = "Id must be greater than 0")
    private Long followerId;

    @NotNull(message = "Please enter your following id!")
    @Min(value = 1, message = "Id must be greater than 0")
    private Long followingId;
}
