package devsinc.Instagram.clone.repository;

import devsinc.Instagram.clone.enums.AccountType;
import devsinc.Instagram.clone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Set<User> findByAccountType(AccountType accountType);

    List<User> findByUsernameContaining(String keyword);
}
