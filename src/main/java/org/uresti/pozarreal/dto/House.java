package org.uresti.pozarreal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class House {
    private String id;
    private String street;
    private String streetName;
    private String number;
    private boolean chipsEnabled;
    private ArrayList<PaymentByConcept> twoMonthsPayments; //An array of 6 elements is expected (current year payments)
    private PaymentByConcept parkingPenPayment;
}
