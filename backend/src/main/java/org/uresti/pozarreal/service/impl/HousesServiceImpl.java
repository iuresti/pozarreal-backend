package org.uresti.pozarreal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.uresti.pozarreal.config.PozarrealConfig;
import org.uresti.pozarreal.config.Role;
import org.uresti.pozarreal.controllers.SessionHelper;
import org.uresti.pozarreal.dto.*;
import org.uresti.pozarreal.exception.BadRequestDataException;
import org.uresti.pozarreal.model.House;
import org.uresti.pozarreal.model.Representative;
import org.uresti.pozarreal.model.Street;
import org.uresti.pozarreal.repository.*;
import org.uresti.pozarreal.service.HousesService;
import org.uresti.pozarreal.service.mappers.HousesByUserMapper;
import org.uresti.pozarreal.service.mappers.HousesMapper;
import org.uresti.pozarreal.tools.PaymentTools;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HousesServiceImpl implements HousesService {

	private final HousesRepository housesRepository;
	private final StreetRepository streetRepository;
	private final RepresentativeRepository representativeRepository;
	private final SessionHelper sessionHelper;
	private final HousesByUserRepository housesByUserRepository;
	private final PaymentRepository paymentRepository;

	public HousesServiceImpl(HousesRepository housesRepository,
	                         StreetRepository streetRepository,
	                         RepresentativeRepository representativeRepository,
	                         SessionHelper sessionHelper,
	                         HousesByUserRepository housesByUserRepository,
	                         PaymentRepository paymentRepository) {
		this.housesRepository = housesRepository;
		this.streetRepository = streetRepository;
		this.representativeRepository = representativeRepository;
		this.sessionHelper = sessionHelper;
		this.housesByUserRepository = housesByUserRepository;
		this.paymentRepository = paymentRepository;
	}

	@Override
	@Transactional
	public void toggleChipStatusRequest(String houseId, boolean enable) {
		House house = housesRepository.findById(houseId).orElseThrow();

		house.setChipsEnabled(enable);

		housesRepository.save(house);
	}

	@Override
	@Transactional(readOnly = true)
	public List<org.uresti.pozarreal.dto.House> getHousesByStreet(String streetId, LoggedUser user) {

		if (sessionHelper.hasRole(user, Role.ROLE_REPRESENTATIVE) && !sessionHelper.hasRole(user, Role.ROLE_ADMIN)) {
			Representative representative = representativeRepository.findById(user.getUserId()).orElseThrow();

			if (!representative.getStreet().equals(streetId)) {
				throw new BadRequestDataException("Invalid street for representative query", "INVALID_STREET");
			}
		}

		return housesRepository.findAllByStreetOrderByNumber(streetId).stream()
				.map(HousesMapper::entityToDto)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<HouseByUser> getHousesByUser(String userId) {
		return housesByUserRepository.findAllByUserId(userId).stream()
				.map(HousesByUserMapper::entityToDto)
				.collect(Collectors.toList());
	}

	@Override
	public HouseInfo getHouseInfo(String houseId) {

		House house = housesRepository.findById(houseId).orElseThrow();
		Street street = streetRepository.findById(house.getStreet()).orElseThrow();

		return HouseInfo.builder()
				.id(house.getId())
				.number(house.getNumber())
				.street(house.getStreet())
				.streetName(street.getName())
				.chipsEnabled(house.isChipsEnabled())
				.notes(house.getNotes())
				.build();
	}

	@Override
	public void saveNotes(String houseId, String notes) {
		House house = housesRepository.findById(houseId).orElseThrow();

		house.setNotes(notes);

		housesRepository.save(house);
	}

	@Override
	public void deleteHouseByUser(String id) {
		housesByUserRepository.deleteById(id);
	}

	@Override
	public HouseByUser saveHouseByUser(HouseByUser houseByUser) {

		if (houseByUser.getHouseId() == null) {
			throw new BadRequestDataException("it's no possible save null house", "WRONG_HOUSE_ID");
		}

		housesByUserRepository.findAllByUserId(houseByUser.getUserId())
				.forEach(house -> {
					if (house.getHouseId().equals(houseByUser.getHouseId())) {
						throw new BadRequestDataException("already you are owner of this house", "INVALID_SAVE_HOUSE");
					}
				});

		if (houseByUser.getId() == null) {
			houseByUser.setId(UUID.randomUUID().toString());
		}

		houseByUser.setMainHouse(false);
		houseByUser.setValidated(false);

		return HousesByUserMapper.entityToDto(housesByUserRepository.save(HousesByUserMapper.dtoToEntity(houseByUser)));
	}

	@Override
	public ArrayList<PaymentByConcept> getPaymentsHouse(String houseId) {
		LocalDate startOfYear = LocalDate.now().withYear(LocalDate.now().getYear()).withDayOfYear(1);

		LocalDate endOfYear = LocalDate.now().withYear(LocalDate.now().getYear() + 1).withDayOfYear(1);

		List<org.uresti.pozarreal.model.Payment> paymentsByHouse = paymentRepository
				.findAllByHouseIdAndPaymentDateBetween(houseId, startOfYear, endOfYear);


		org.uresti.pozarreal.dto.House house = org.uresti.pozarreal.dto.House.builder().build();

		 PaymentTools.setYearPayments(new PozarrealConfig() ,house, paymentsByHouse);

		return house.getTwoMonthsPayments();
	}

	@Override
	public List<HouseByUser> getHousesByUser(Principal principal) {
		return getHousesByUser(sessionHelper.getLoggedUser(principal).getUserId());
	}
}
