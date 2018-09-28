package id.co.elevenia.paysvc.repository.search;

import id.co.elevenia.paysvc.domain.TrInstantPayment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TrInstantPayment entity.
 */
public interface TrInstantPaymentSearchRepository extends ElasticsearchRepository<TrInstantPayment, Long> {
}
