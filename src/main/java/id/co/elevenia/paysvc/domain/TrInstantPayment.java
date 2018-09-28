package id.co.elevenia.paysvc.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TrInstantPayment.
 */
@Entity
@Table(name = "tr_instant_payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "trinstantpayment")
public class TrInstantPayment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "ord_no", nullable = false)
    private Long ordNo;

    @Column(name = "amount")
    private Long amount;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrdNo() {
        return ordNo;
    }

    public TrInstantPayment ordNo(Long ordNo) {
        this.ordNo = ordNo;
        return this;
    }

    public void setOrdNo(Long ordNo) {
        this.ordNo = ordNo;
    }

    public Long getAmount() {
        return amount;
    }

    public TrInstantPayment amount(Long amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TrInstantPayment trInstantPayment = (TrInstantPayment) o;
        if (trInstantPayment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), trInstantPayment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TrInstantPayment{" +
            "id=" + getId() +
            ", ordNo=" + getOrdNo() +
            ", amount=" + getAmount() +
            "}";
    }
}
