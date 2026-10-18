package zw.ac.msu.dzimbahub.repository;
import org.springframework.data.jpa.repository.JpaRepository; import zw.ac.msu.dzimbahub.model.UserAction; import java.util.*;
public interface UserActionRepository extends JpaRepository<UserAction,Long> { List<UserAction> findByTenantEmailOrderByCreatedAtDesc(String email); List<UserAction> findByLandlordEmailOrderByCreatedAtDesc(String email); List<UserAction> findByTypeOrderByCreatedAtDesc(String type); long countByType(String type); }
