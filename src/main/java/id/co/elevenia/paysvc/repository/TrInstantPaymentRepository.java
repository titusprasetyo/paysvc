package id.co.elevenia.paysvc.repository;

import id.co.elevenia.paysvc.domain.TrInstantPayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TrInstantPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrInstantPaymentRepository extends JpaRepository<TrInstantPayment, Long> {

}
