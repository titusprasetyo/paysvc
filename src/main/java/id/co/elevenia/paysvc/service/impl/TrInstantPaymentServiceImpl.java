package id.co.elevenia.paysvc.service.impl;

import id.co.elevenia.paysvc.service.TrInstantPaymentService;
import id.co.elevenia.paysvc.domain.TrInstantPayment;
import id.co.elevenia.paysvc.repository.TrInstantPaymentRepository;
import id.co.elevenia.paysvc.repository.search.TrInstantPaymentSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing TrInstantPayment.
 */
@Service
@Transactional
public class TrInstantPaymentServiceImpl implements TrInstantPaymentService {

    private final Logger log = LoggerFactory.getLogger(TrInstantPaymentServiceImpl.class);

    private final TrInstantPaymentRepository trInstantPaymentRepository;

    private final TrInstantPaymentSearchRepository trInstantPaymentSearchRepository;

    public TrInstantPaymentServiceImpl(TrInstantPaymentRepository trInstantPaymentRepository, TrInstantPaymentSearchRepository trInstantPaymentSearchRepository) {
        this.trInstantPaymentRepository = trInstantPaymentRepository;
        this.trInstantPaymentSearchRepository = trInstantPaymentSearchRepository;
    }

    /**
     * Save a trInstantPayment.
     *
     * @param trInstantPayment the entity to save
     * @return the persisted entity
     */
    @Override
    public TrInstantPayment save(TrInstantPayment trInstantPayment) {
        log.debug("Request to save TrInstantPayment : {}", trInstantPayment);        TrInstantPayment result = trInstantPaymentRepository.save(trInstantPayment);
        trInstantPaymentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the trInstantPayments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TrInstantPayment> findAll(Pageable pageable) {
        log.debug("Request to get all TrInstantPayments");
        return trInstantPaymentRepository.findAll(pageable);
    }


    /**
     * Get one trInstantPayment by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TrInstantPayment> findOne(Long id) {
        log.debug("Request to get TrInstantPayment : {}", id);
        return trInstantPaymentRepository.findById(id);
    }

    /**
     * Delete the trInstantPayment by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TrInstantPayment : {}", id);
        trInstantPaymentRepository.deleteById(id);
        trInstantPaymentSearchRepository.deleteById(id);
    }

    /**
     * Search for the trInstantPayment corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TrInstantPayment> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of TrInstantPayments for query {}", query);
        return trInstantPaymentSearchRepository.search(queryStringQuery(query), pageable);    }
}
