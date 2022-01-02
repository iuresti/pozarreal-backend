package org.uresti.pozarreal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment_evidences")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvidence {
    @Id
    private String id;
    private String paymentId;
    private String url;
}