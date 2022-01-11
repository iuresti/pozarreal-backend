package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.config.FeeConfig;
import org.uresti.pozarreal.config.PozarrealConfig;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.House;
import org.uresti.pozarreal.dto.LoggedUser;
import org.uresti.pozarreal.dto.PaymentByConcept;
import org.uresti.pozarreal.dto.StreetInfo;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.model.Payment;
import org.uresti.pozarreal.model.PaymentConcept;
import org.uresti.pozarreal.model.Representative;
import org.uresti.pozarreal.model.Street;
import org.uresti.pozarreal.repository.*;
import org.uresti.pozarreal.service.StreetsService;
import org.uresti.pozarreal.service.mappers.HousesMapper;
import org.uresti.pozarreal.service.mappers.RepresentativeMapper;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.uresti.pozarreal.model.PaymentSubConcept.*;

@Service
public class StreetServiceImpl implements StreetsService {

    private final StreetRepository streetRepository;
    private final RepresentativeRepository representativeRepository;
    private final HousesRepository housesRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final PozarrealConfig pozarrealConfig;
    private final SessionHelper sessionHelper;

    public StreetServiceImpl(StreetRepository streetRepository,
                             RepresentativeRepository representativeRepository,
                             HousesRepository housesRepository,
                             PaymentRepository paymentRepository,
                             UserRepository userRepository,
                             PozarrealConfig pozarrealConfig,
                             SessionHelper sessionHelper) {
        this.streetRepository = streetRepository;
        this.representativeRepository = representativeRepository;
        this.housesRepository = housesRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.pozarrealConfig = pozarrealConfig;
        this.sessionHelper = sessionHelper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Street> getStreets(LoggedUser user) {

        if (sessionHelper.hasRole(user, Role.ROLE_REPRESENTATIVE) && !sessionHelper.hasRole(user, Role.ROLE_ADMIN)) {
            Representative representative = representativeRepository.findById(user.getUserId()).orElseThrow();

            return Collections.singletonList(streetRepository.findById(representative.getStreet())
                    .orElseThrow(() -> new BadRequestDataException("Wrong street configured for representative " + representative.getUserId(), "INVALID_STREET")));
        }

        return streetRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public StreetInfo getStreetInfo(String streetId, LoggedUser user, Integer startYear) {

        if (sessionHelper.hasRole(user, Role.ROLE_REPRESENTATIVE) && !sessionHelper.hasRole(user, Role.ROLE_ADMIN)) {
            Representative representative = representativeRepository.findById(user.getUserId()).orElseThrow();

            if (!representative.getStreet().equals(streetId)) {
                throw new BadRequestDataException("Invalid street for representative query", "INVALID_STREET");
            }
        }

        Street street = streetRepository.findById(streetId).orElseThrow();
        StreetInfo streetInfo = new StreetInfo();

        LocalDate startOfYear = LocalDate.now().withDayOfYear(1);

        LocalDate endOfYear = LocalDate.now().withDayOfYear(1);

        if(startYear != null) {
            startOfYear = startOfYear.withYear(startYear);
            endOfYear = endOfYear.withYear(startYear + 1);
        }

        Map<String, List<Payment>> streetPaymentsByHouse = paymentRepository
                .findAllByStreetAndPaymentDateBetween(streetId, startOfYear, endOfYear)
                .stream().collect(Collectors.groupingBy(Payment::getHouseId));

        streetInfo.setId(streetId);
        streetInfo.setName(street.getName());
        streetInfo.setRepresentative(getRepresentative(street));
        streetInfo.setHouses(housesRepository.findAllByStreetOrderByNumber(streetId).stream().map(HousesMapper::entityToDto)
                .peek(house -> setYearPayments(house, streetPaymentsByHouse.getOrDefault(house.getId(), Collections.emptyList())))
                .peek(this::setParkingPenPayment)
                .collect(Collectors.toList()));

        return streetInfo;
    }

    private org.uresti.pozarreal.dto.Representative getRepresentative(Street street) {
        Optional<Representative> representativeModel = representativeRepository.findByStreet(street.getId());

        if (representativeModel.isEmpty()) {
            return null;
        }

        org.uresti.pozarreal.dto.Representative representative = RepresentativeMapper.entityToDto(representativeModel.get());

        representative.setStreet(street.getId());
        representative.setStreetName(street.getName());
        representative.setName(userRepository.findById(representative.getUserId()).orElseThrow().getName());

        if (representative.getHouse() != null) {
            housesRepository.findById(representative.getHouse()).ifPresent(house -> representative.setHouseNumber(house.getNumber()));
        }

        return representative;
    }

    private void setParkingPenPayment(House house) {
        List<Payment> payments = paymentRepository.findAllByHouseIdAndPaymentConceptId(house.getId(), PaymentConcept.PARKING_PEN);
        PaymentByConcept paymentByConcept = new PaymentByConcept();
        FeeConfig feeConfig = pozarrealConfig.getFees();

        paymentByConcept.setAmount(payments.stream().map(Payment::getAmount).reduce(0.0, Double::sum));
        paymentByConcept.setComplete(paymentByConcept.getAmount() >= feeConfig.getParkingPenFee());
        paymentByConcept.setValidated(paymentByConcept.isValidated());

        house.setParkingPenPayment(paymentByConcept);
    }

    private void setYearPayments(org.uresti.pozarreal.dto.House house, List<Payment> payments) {
        String[] twoMonthsPaymentIds = {
                MAINTENANCE_BIM_1,
                MAINTENANCE_BIM_2,
                MAINTENANCE_BIM_3,
                MAINTENANCE_BIM_4,
                MAINTENANCE_BIM_5,
                MAINTENANCE_BIM_6
        };

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
            if (MAINTENANCE_ANNUITY.equals(payment.getPaymentSubConceptId())) {
                for (PaymentByConcept paymentByConcept : paymentInfo) {
                    paymentByConcept.setId(payment.getId());
                    paymentByConcept.setComplete(true);
                    paymentByConcept.setAmount(payment.getAmount());
                    paymentByConcept.setValidated(payment.isValidated());
                    paymentByConcept.setConflict(payment.isConflict());
                }
                break;
            } else {
                for (int i = 0; i < twoMonthsPaymentIds.length; i++) {
                    if (twoMonthsPaymentIds[i].equals(payment.getPaymentSubConceptId())) {
                        paymentInfo.get(i).setId(payment.getId());
                        paymentInfo.get(i).setAmount(paymentInfo.get(i).getAmount() + payment.getAmount());
                        paymentInfo.get(i).setComplete(paymentInfo.get(i).getAmount() >= feeConfig.getBiMonthlyMaintenanceFee());
                        paymentInfo.get(i).setValidated(payment.isValidated());
                        paymentInfo.get(i).setConflict(payment.isConflict());
                        break;
                    }
                }
            }
        }
    }
}
