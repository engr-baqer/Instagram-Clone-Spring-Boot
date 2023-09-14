package devsinc.Instagram.clone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "stories")
public class Stories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private Long storyId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "public_id")
    private String publicId;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;


    /*********** Mappings ************/

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
}
