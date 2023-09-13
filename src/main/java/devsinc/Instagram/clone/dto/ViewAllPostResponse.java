package devsinc.Instagram.clone.dto;

import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ViewAllPostResponse {
    private Long postId;
    private String postDescription;
    private String creationDate;
    private Long userId;
    private Set<ViewAllPostImagesResponse> postImages;
}
