package devsinc.Instagram.clone.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ViewAllPostImagesResponse {
    private Long imageId;
    private Long postId;
    private String imageUrl;
    private String publicId;
}
