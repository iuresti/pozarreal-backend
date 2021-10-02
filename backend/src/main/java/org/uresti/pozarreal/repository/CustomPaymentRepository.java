package org.uresti.pozarreal.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.uresti.pozarreal.dto.PaymentFilter;
import org.uresti.pozarreal.dto.PaymentView;

import java.util.List;

@Repository
public class CustomPaymentRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CustomPaymentRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<PaymentView> executeQuery(PaymentFilter paymentFilter) {
        String query = "SELECT p.id, s.id streetId, s.name streetName, h.id houseId, h.number houseNumber, p.payment_date, " +
                " p.registration_date, u.name userName, p.amount, pc.id paymentConceptId, pc.label paymentConcept, psc.id paymentSubConceptId, psc.label paymentSubConcept, p.payment_mode paymentMode, p.notes " +
                " FROM payments p INNER JOIN houses h ON p.house_id = h.id " +
                "  INNER JOIN streets s ON h.street = s.id " +
                "  INNER JOIN payment_concepts pc ON p.payment_concept_id = pc.id" +
                "  LEFT JOIN payment_sub_concepts psc ON p.payment_sub_concept_id = psc.id" +
                "  INNER JOIN users u ON p.user_id = u.id ";
        StringBuilder whereCondition = new StringBuilder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        BeanPropertyRowMapper<PaymentView> paymentViewMapper = new BeanPropertyRowMapper<>(PaymentView.class);

        if (StringUtils.hasLength(paymentFilter.getPaymentMode())) {
            appendParam(whereCondition, " p.payment_mode = :paymentMode");
            mapSqlParameterSource.addValue("paymentMode", paymentFilter.getPaymentMode());
        }

        if (!CollectionUtils.isEmpty(paymentFilter.getConcepts())) {
            appendParam(whereCondition, " pc.id IN (:concepts)");
            mapSqlParameterSource.addValue("concepts", paymentFilter.getConcepts());
        }

        if (paymentFilter.getStartDate() != null && paymentFilter.getEndDate() != null) {
            appendParam(whereCondition, " p.payment_date BETWEEN :paymentDateStart AND :paymentDateEnd");
            mapSqlParameterSource.addValue("paymentDateStart", paymentFilter.getStartDate());
            mapSqlParameterSource.addValue("paymentDateEnd", paymentFilter.getEndDate());
        } else if (paymentFilter.getStartDate() != null) {
            appendParam(whereCondition, " p.payment_date >= :paymentDateStart ");
            mapSqlParameterSource.addValue("paymentDateStart", paymentFilter.getStartDate());
        } else if (paymentFilter.getEndDate() != null) {
            appendParam(whereCondition, " p.payment_date <= :paymentDateEnd ");
            mapSqlParameterSource.addValue("paymentDateEnd", paymentFilter.getEndDate());
        }

        if (StringUtils.hasLength(paymentFilter.getHouse())) {
            appendParam(whereCondition, " h.id = :houseId");
            mapSqlParameterSource.addValue("houseId", paymentFilter.getHouse());
        }

        if (StringUtils.hasLength(paymentFilter.getPaymentMode())) {
            appendParam(whereCondition, " h.id = :paymentMode");
            mapSqlParameterSource.addValue("paymentMode", paymentFilter.getPaymentMode());
        }

        if (StringUtils.hasLength(paymentFilter.getStreet())) {
            appendParam(whereCondition, " s.id = :streetId");
            mapSqlParameterSource.addValue("streetId", paymentFilter.getStreet());
        }

        return namedParameterJdbcTemplate.query(query + whereCondition + " ORDER BY p.payment_date", mapSqlParameterSource, paymentViewMapper);
    }

    private void appendParam(StringBuilder query, String paramString) {
        if (query.length() == 0) {
            query.append(" WHERE ").append(paramString);
        } else {
            query.append(" AND ").append(paramString);
        }
    }
}
