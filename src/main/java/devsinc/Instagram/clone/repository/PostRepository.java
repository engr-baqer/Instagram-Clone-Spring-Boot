package devsinc.Instagram.clone.repository;

import devsinc.Instagram.clone.model.Post;
import devsinc.Instagram.clone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Set<Post> findByUser(User user);
}
