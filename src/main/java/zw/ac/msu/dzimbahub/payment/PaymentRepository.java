package zw.ac.msu.dzimbahub.payment;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface PaymentRepository extends JpaRepository<PaymentRecord,Long> { Optional<PaymentRecord> findByMerchantReference(String ref); List<PaymentRecord> findByTenantIdOrderByCreatedAtDesc(Long id); List<PaymentRecord> findByLandlordIdOrderByCreatedAtDesc(Long id); }
