package devsinc.Instagram.clone.repository;

import devsinc.Instagram.clone.model.Follow;
import devsinc.Instagram.clone.model.FollowRequests;
import devsinc.Instagram.clone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRequestsRepository extends JpaRepository<FollowRequests, Long> {
    @Query("SELECT f FROM FollowRequests f WHERE f.follower = :follower AND f.following = :following")
    FollowRequests findByFollowerAndFollowing(@Param("follower") User follower, @Param("following") User following);
}
