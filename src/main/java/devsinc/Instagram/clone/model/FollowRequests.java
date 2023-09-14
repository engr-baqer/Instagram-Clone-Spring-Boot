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
@Table(name = "follow_requests")
public class FollowRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "request_date")
    private String requestDate;

    /*********** Mappings ************/

    // This mapping indicates the users which will receive the follow requests
    @ManyToOne
    @JoinColumn(name = "follower")
    private User follower;

    // This mapping indicates the users which will send the follow requests
    @ManyToOne
    @JoinColumn(name = "following")
    private User following;
}
