package id.co.elevenia.paysvc.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of TrInstantPaymentSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TrInstantPaymentSearchRepositoryMockConfiguration {

    @MockBean
    private TrInstantPaymentSearchRepository mockTrInstantPaymentSearchRepository;

}
