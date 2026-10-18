package zw.ac.msu.dzimbahub.auth;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AppUserRepository extends JpaRepository<AppUser,Long> { Optional<AppUser> findByEmailIgnoreCase(String email); boolean existsByEmailIgnoreCase(String email); List<AppUser> findByStatusOrderByCreatedAtAsc(AccountStatus status); long countByStatus(AccountStatus status); }
