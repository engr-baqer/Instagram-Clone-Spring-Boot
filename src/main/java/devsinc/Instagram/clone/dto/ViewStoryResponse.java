package devsinc.Instagram.clone.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ViewStoryResponse {
    private Long storyId;
    private String storyUrl;
    private LocalDateTime creationTime;
}
