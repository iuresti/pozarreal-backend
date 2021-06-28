package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.config.FeeConfig;
import org.uresti.pozarreal.config.PozarrealConfig;
import org.uresti.pozarreal.dto.StreetInfo;
import org.uresti.pozarreal.dto.TwoMonthsPayment;
import org.uresti.pozarreal.model.Payment;
import org.uresti.pozarreal.model.PaymentConcept;
import org.uresti.pozarreal.model.PaymentSubConcept;
import org.uresti.pozarreal.model.Street;
import org.uresti.pozarreal.repository.*;
import org.uresti.pozarreal.service.StreetsService;
import org.uresti.pozarreal.service.mappers.HousesMapper;
import org.uresti.pozarreal.service.mappers.RepresentativeMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.uresti.pozarreal.model.PaymentConcept.MAINTENENCE;
import static org.uresti.pozarreal.model.PaymentSubConcept.*;

@Service
public class StreetServiceImpl implements StreetsService {

    private final StreetRepository streetRepository;

    private final RepresentativeRepository representativeRepository;

    private final HousesRepository housesRepository;

    private final PaymentRepository paymentRepository;

    private final PaymentSubConceptsRepository paymentSubConceptsRepository;

    private final PaymentConceptsRepository paymentConceptsRepository;

    private final PozarrealConfig pozarrealConfig;

    public StreetServiceImpl(StreetRepository streetRepository,
                             RepresentativeRepository representativeRepository,
                             HousesRepository housesRepository,
                             PaymentRepository paymentRepository,
                             PaymentSubConceptsRepository paymentSubConceptsRepository,
                             PaymentConceptsRepository paymentConceptsRepository,
                             PozarrealConfig pozarrealConfig) {
        this.streetRepository = streetRepository;
        this.representativeRepository = representativeRepository;
        this.housesRepository = housesRepository;
        this.paymentRepository = paymentRepository;
        this.paymentSubConceptsRepository = paymentSubConceptsRepository;
        this.paymentConceptsRepository = paymentConceptsRepository;
        this.pozarrealConfig = pozarrealConfig;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Street> getStreets() {
        return streetRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public StreetInfo getStreetInfo(String streetId) {

        Street street = streetRepository.findById(streetId).orElseThrow();
        StreetInfo streetInfo = new StreetInfo();

        PaymentConcept paymentConcept = paymentConceptsRepository.findByLabel(MAINTENENCE);
        Map<String, String> paymentSubConcepts = paymentSubConceptsRepository.findAllByConceptId(paymentConcept.getId())
                .stream().collect(Collectors.toMap(PaymentSubConcept::getLabel, PaymentSubConcept::getId));

        streetInfo.setId(streetId);
        streetInfo.setName(street.getName());
        streetInfo.setRepresentative(RepresentativeMapper.entityToDto(representativeRepository.findRepresentativeByStreet(streetId)));
        streetInfo.setHouses(housesRepository.findAllByStreetOrderByNumber(streetId).stream().map(HousesMapper::entityToDto).
                peek(house -> setYearPayments(house, paymentSubConcepts)).collect(Collectors.toList()));

        return streetInfo;
    }

    private void setYearPayments(org.uresti.pozarreal.dto.House house, Map<String, String> paymentSubConcepts) {
        LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
        List<Payment> payments = paymentRepository.findAllByHouseIdAndPaymentDateIsGreaterThanEqual(house.getId(), startOfYear);
        String[] twoMonthsPaymentIds = {
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_1),
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_2),
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_3),
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_4),
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_5),
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_6)
        };
        String annuityId = paymentSubConcepts.get(MAINTENANCE_ANNUITY);

        ArrayList<TwoMonthsPayment> paymentInfo = new ArrayList<>();

        paymentInfo.add(new TwoMonthsPayment());
        paymentInfo.add(new TwoMonthsPayment());
        paymentInfo.add(new TwoMonthsPayment());
        paymentInfo.add(new TwoMonthsPayment());
        paymentInfo.add(new TwoMonthsPayment());
        paymentInfo.add(new TwoMonthsPayment());

        house.setTwoMonthsPayments(paymentInfo);

        FeeConfig feeConfig = pozarrealConfig.getFees();

        for (Payment payment : payments) {
            if (annuityId.equals(payment.getPaymentSubConceptId())) {
                for (TwoMonthsPayment twoMonthsPayment : paymentInfo) {
                    twoMonthsPayment.setComplete(true);
                }
                break;
            } else {
                for (int i = 0; i < twoMonthsPaymentIds.length; i++) {
                    if (twoMonthsPaymentIds[i].equals(payment.getPaymentSubConceptId())) {
                        paymentInfo.get(i).setAmount(payment.getAmount());
                        paymentInfo.get(i).setComplete(payment.getAmount() >= feeConfig.getBiMonthlyMaintenanceFee());
                        break;
                    }
                }
            }
        }
    }
}
