package devsinc.Instagram.clone.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Table(name = "follow")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    /*********** Mappings ************/

    // This user is one which will receive the follow request
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    // These are the users will follow the others
    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;
}
