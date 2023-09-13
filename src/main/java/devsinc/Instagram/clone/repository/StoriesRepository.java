package devsinc.Instagram.clone.repository;

import devsinc.Instagram.clone.model.Stories;
import devsinc.Instagram.clone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface StoriesRepository extends JpaRepository<Stories, Long> {
    List<Stories> findByCreationTimeBefore(LocalDateTime time);
    List<Stories> findByUser(User user);
}
