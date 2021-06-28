package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.config.FeeConfig;
import org.uresti.pozarreal.config.PozarrealConfig;
import org.uresti.pozarreal.config.RoleConstants;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.House;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.PaymentByConcept;
import org.uresti.pozarreal.dto.StreetInfo;
import org.uresti.pozarreal.exception.PozarrealSystemException;
import org.uresti.pozarreal.model.*;
import org.uresti.pozarreal.repository.*;
import org.uresti.pozarreal.service.StreetsService;
import org.uresti.pozarreal.service.mappers.HousesMapper;
import org.uresti.pozarreal.service.mappers.RepresentativeMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.uresti.pozarreal.model.PaymentConcept.MAINTENANCE;
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
    private final SessionHelper sessionHelper;

    public StreetServiceImpl(StreetRepository streetRepository,
                             RepresentativeRepository representativeRepository,
                             HousesRepository housesRepository,
                             PaymentRepository paymentRepository,
                             PaymentSubConceptsRepository paymentSubConceptsRepository,
                             PaymentConceptsRepository paymentConceptsRepository,
                             PozarrealConfig pozarrealConfig, SessionHelper sessionHelper) {
        this.streetRepository = streetRepository;
        this.representativeRepository = representativeRepository;
        this.housesRepository = housesRepository;
        this.paymentRepository = paymentRepository;
        this.paymentSubConceptsRepository = paymentSubConceptsRepository;
        this.paymentConceptsRepository = paymentConceptsRepository;
        this.pozarrealConfig = pozarrealConfig;
        this.sessionHelper = sessionHelper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Street> getStreets(LoggedUser user) {

        if (sessionHelper.hasRole(user, RoleConstants.ROLE_REPRESENTATIVE) && !sessionHelper.hasRole(user, RoleConstants.ROLE_ADMIN)) {
            Representative representative = representativeRepository.findByUserId(user.getUserId());

            return Collections.singletonList(streetRepository.findById(representative.getStreet())
                    .orElseThrow(() -> new PozarrealSystemException("Wrong street configured for representative " + representative.getId())));
        }

        return streetRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public StreetInfo getStreetInfo(String streetId, LoggedUser user) {

        if (sessionHelper.hasRole(user, RoleConstants.ROLE_REPRESENTATIVE) && !sessionHelper.hasRole(user, RoleConstants.ROLE_ADMIN)) {
            Representative representative = representativeRepository.findByUserId(user.getUserId());

            if (!representative.getStreet().equals(streetId)) {
                throw new PozarrealSystemException("Invalid street for representative query");
            }
        }

        Street street = streetRepository.findById(streetId).orElseThrow();
        StreetInfo streetInfo = new StreetInfo();

        PaymentConcept paymentConcept = paymentConceptsRepository.findByLabel(MAINTENANCE);
        Map<String, String> paymentSubConcepts = paymentSubConceptsRepository.findAllByConceptId(paymentConcept.getId())
                .stream().collect(Collectors.toMap(PaymentSubConcept::getLabel, PaymentSubConcept::getId));

        LocalDate startOfYear = LocalDate.now().withDayOfYear(1);

        Map<String, List<Payment>> streetPaymentsByHouse = paymentRepository.findAllByStreetAndPaymentDateIsGreaterThanEqual(streetId, startOfYear)
                .stream().collect(Collectors.groupingBy(Payment::getHouseId));

        streetInfo.setId(streetId);
        streetInfo.setName(street.getName());
        streetInfo.setRepresentative(RepresentativeMapper.entityToDto(representativeRepository.findRepresentativeByStreet(streetId)));
        streetInfo.setHouses(housesRepository.findAllByStreetOrderByNumber(streetId).stream().map(HousesMapper::entityToDto)
                .peek(house -> setYearPayments(house, paymentSubConcepts, streetPaymentsByHouse.getOrDefault(house.getId(), Collections.emptyList())))
                .peek(this::setParkingPenPayment)
                .collect(Collectors.toList()));

        return streetInfo;
    }

    private void setParkingPenPayment(House house) {
        List<Payment> payments = paymentRepository.findAllByHouseIdAndPaymentConcept(house.getId(), PaymentConcept.PARKING_PEN);
        PaymentByConcept paymentByConcept = new PaymentByConcept();
        FeeConfig feeConfig = pozarrealConfig.getFees();

        paymentByConcept.setAmount(payments.stream().map(Payment::getAmount).reduce(0.0, Double::sum));
        paymentByConcept.setComplete(paymentByConcept.getAmount() >= feeConfig.getParkingPenFee());

        house.setParkingPenPayment(paymentByConcept);
    }

    private void setYearPayments(org.uresti.pozarreal.dto.House house, Map<String, String> paymentSubConcepts, List<Payment> payments) {
        String[] twoMonthsPaymentIds = {
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_1),
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_2),
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_3),
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_4),
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_5),
                paymentSubConcepts.get(MAINTENANCE_TWO_MONTHS_6)
        };
        String annuityId = paymentSubConcepts.get(MAINTENANCE_ANNUITY);

        ArrayList<PaymentByConcept> paymentInfo = new ArrayList<>();

        paymentInfo.add(new PaymentByConcept());
        paymentInfo.add(new PaymentByConcept());
        paymentInfo.add(new PaymentByConcept());
        paymentInfo.add(new PaymentByConcept());
        paymentInfo.add(new PaymentByConcept());
        paymentInfo.add(new PaymentByConcept());

        house.setTwoMonthsPayments(paymentInfo);

        FeeConfig feeConfig = pozarrealConfig.getFees();

        for (Payment payment : payments) {
            if (annuityId.equals(payment.getPaymentSubConceptId())) {
                for (PaymentByConcept paymentByConcept : paymentInfo) {
                    paymentByConcept.setComplete(true);
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
