package devsinc.Instagram.clone.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "comment_time")
    private LocalDateTime creationTime;

    @Column(name = "comment_text")
    private String commentText;

    /*********** Mappings ************/

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne()
    @JsonIgnore
    @JoinColumn(name = "post_id")
    private Post post;
}
