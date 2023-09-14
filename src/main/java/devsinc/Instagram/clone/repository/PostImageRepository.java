package devsinc.Instagram.clone.repository;

import devsinc.Instagram.clone.model.Post;
import devsinc.Instagram.clone.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    Set<PostImage> findByPost(Post post);
}
