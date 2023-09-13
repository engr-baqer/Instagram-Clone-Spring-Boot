package devsinc.Instagram.clone.repository;

import devsinc.Instagram.clone.model.Like;
import devsinc.Instagram.clone.model.Post;
import devsinc.Instagram.clone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.user = :user AND l.post = :post")
    boolean existsByUserAndPost(@Param("user") User user, @Param("post") Post post);
}
