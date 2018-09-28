package id.co.elevenia.paysvc.service;

import id.co.elevenia.paysvc.domain.TrInstantPayment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing TrInstantPayment.
 */
public interface TrInstantPaymentService {

    /**
     * Save a trInstantPayment.
     *
     * @param trInstantPayment the entity to save
     * @return the persisted entity
     */
    TrInstantPayment save(TrInstantPayment trInstantPayment);

    /**
     * Get all the trInstantPayments.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TrInstantPayment> findAll(Pageable pageable);


    /**
     * Get the "id" trInstantPayment.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TrInstantPayment> findOne(Long id);

    /**
     * Delete the "id" trInstantPayment.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the trInstantPayment corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TrInstantPayment> search(String query, Pageable pageable);
}
