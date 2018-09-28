package id.co.elevenia.paysvc.web.rest;

import com.codahale.metrics.annotation.Timed;
import id.co.elevenia.paysvc.domain.TrInstantPayment;
import id.co.elevenia.paysvc.service.TrInstantPaymentService;
import id.co.elevenia.paysvc.web.rest.errors.BadRequestAlertException;
import id.co.elevenia.paysvc.web.rest.util.HeaderUtil;
import id.co.elevenia.paysvc.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TrInstantPayment.
 */

//test
//test again
@RestController
@RequestMapping("/api")
public class TrInstantPaymentResource {

    private final Logger log = LoggerFactory.getLogger(TrInstantPaymentResource.class);

    private static final String ENTITY_NAME = "trInstantPayment";

    private final TrInstantPaymentService trInstantPaymentService;

    public TrInstantPaymentResource(TrInstantPaymentService trInstantPaymentService) {
        this.trInstantPaymentService = trInstantPaymentService;
    }

    /**
     * POST  /tr-instant-payments : Create a new trInstantPayment.
     *
     * @param trInstantPayment the trInstantPayment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new trInstantPayment, or with status 400 (Bad Request) if the trInstantPayment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tr-instant-payments")
    @Timed
    public ResponseEntity<TrInstantPayment> createTrInstantPayment(@Valid @RequestBody TrInstantPayment trInstantPayment) throws URISyntaxException {
        log.debug("REST request to save TrInstantPayment : {}", trInstantPayment);
        if (trInstantPayment.getId() != null) {
            throw new BadRequestAlertException("A new trInstantPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrInstantPayment result = trInstantPaymentService.save(trInstantPayment);
        return ResponseEntity.created(new URI("/api/tr-instant-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tr-instant-payments : Updates an existing trInstantPayment.
     *
     * @param trInstantPayment the trInstantPayment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated trInstantPayment,
     * or with status 400 (Bad Request) if the trInstantPayment is not valid,
     * or with status 500 (Internal Server Error) if the trInstantPayment couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tr-instant-payments")
    @Timed
    public ResponseEntity<TrInstantPayment> updateTrInstantPayment(@Valid @RequestBody TrInstantPayment trInstantPayment) throws URISyntaxException {
        log.debug("REST request to update TrInstantPayment : {}", trInstantPayment);
        if (trInstantPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TrInstantPayment result = trInstantPaymentService.save(trInstantPayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, trInstantPayment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tr-instant-payments : get all the trInstantPayments.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trInstantPayments in body
     */
    @GetMapping("/tr-instant-payments")
    @Timed
    public ResponseEntity<List<TrInstantPayment>> getAllTrInstantPayments(Pageable pageable) {
        log.debug("REST request to get a page of TrInstantPayments");
        Page<TrInstantPayment> page = trInstantPaymentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tr-instant-payments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tr-instant-payments/:id : get the "id" trInstantPayment.
     *
     * @param id the id of the trInstantPayment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the trInstantPayment, or with status 404 (Not Found)
     */
    @GetMapping("/tr-instant-payments/{id}")
    @Timed
    public ResponseEntity<TrInstantPayment> getTrInstantPayment(@PathVariable Long id) {
        log.debug("REST request to get TrInstantPayment : {}", id);
        Optional<TrInstantPayment> trInstantPayment = trInstantPaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trInstantPayment);
    }

    /**
     * DELETE  /tr-instant-payments/:id : delete the "id" trInstantPayment.
     *
     * @param id the id of the trInstantPayment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tr-instant-payments/{id}")
    @Timed
    public ResponseEntity<Void> deleteTrInstantPayment(@PathVariable Long id) {
        log.debug("REST request to delete TrInstantPayment : {}", id);
        trInstantPaymentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/tr-instant-payments?query=:query : search for the trInstantPayment corresponding
     * to the query.
     *
     * @param query the query of the trInstantPayment search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/tr-instant-payments")
    @Timed
    public ResponseEntity<List<TrInstantPayment>> searchTrInstantPayments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TrInstantPayments for query {}", query);
        Page<TrInstantPayment> page = trInstantPaymentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/tr-instant-payments");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
