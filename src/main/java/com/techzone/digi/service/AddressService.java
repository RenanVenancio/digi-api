package com.techzone.digi.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techzone.digi.dto.AddressDTO;
import com.techzone.digi.entity.Address;
import com.techzone.digi.entity.DeliveryArea;
import com.techzone.digi.entity.User;
import com.techzone.digi.repository.AddressRepository;
import com.techzone.digi.security.UserSS;
import com.techzone.digi.service.exception.AuthorizationException;
import com.techzone.digi.service.exception.ObjectNotFoundException;

@Service
public class AddressService {

	@Autowired
	private DeliveryAreaService deliveryAreaService;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private UserService userService;

	public Address save(Address obj) {
		UserSS userAuthenticated = UserService.authenticated();
		User user = userService.findById(userAuthenticated.getId());
		obj.setUser(user);
		return addressRepository.save(obj);
	}

	/**
	 * Lista todos os enderelos do usuario logado
	 * 
	 * @return
	 */
	public List<Address> findAll() {
		UserSS userAuthenticated = UserService.authenticated();
		return addressRepository.findAllByUserId(userAuthenticated.getId());
	}

	public Address findById(Long id) {
		Optional<Address> address = addressRepository.findById(id);
		return address.orElseThrow(() -> new ObjectNotFoundException(Address.class.getName() + " not found"));
	}

	/**
	 * Deleta o endereço do usuário logado
	 * 
	 * @param id - Long: Id do endereço
	 */
	public void deleteById(Long id) {
		UserSS userAuthenticated = UserService.authenticated();
		Address address = findById(id);
		if (address.getUser() != null) {
			if (address.getUser().getId() != userAuthenticated.getId()) {
				throw new AuthorizationException("Acesso negado.");
			}
		}
		addressRepository.delete(address);
	}

	public Address update(Address object) {
		UserSS userAuthenticated = UserService.authenticated();
		if (object.getUser() != null) {
			if (object.getUser().getId() != userAuthenticated.getId()) {
				throw new AuthorizationException("Acesso negado.");
			}
		}
		/*
		 * if (object.getCompany() != null) { if (object.getCompany().getId() !=
		 * userAuthenticated.getId()) { throw new
		 * AuthorizationException("Acesso negado."); } }
		 */
		Address newObject = findById(object.getId());
		updateData(newObject, object);
		return addressRepository.save(newObject);
	}

	private void updateData(Address oldObj, Address newObj) {
		oldObj.setCity(newObj.getCity());
		oldObj.setComplement(newObj.getComplement());
		oldObj.setDeliveryArea(newObj.getDeliveryArea());
		oldObj.setModifiedDate(new Date());
		oldObj.setNeighborhood(newObj.getNeighborhood());
		oldObj.setState(newObj.getState());
		oldObj.setStreet(newObj.getStreet());
		oldObj.setZipcode(newObj.getZipcode());
	}

	public Address fromDTO(AddressDTO addressDTO) {
		DeliveryArea deliveryArea = null;
		if (addressDTO.getDeliveryArea() != null) {
			deliveryArea = deliveryAreaService.findById(addressDTO.getDeliveryArea());
		}
		Address address = new Address();
		address.setId(addressDTO.getId() != null ? addressDTO.getId() : null);
		address.setStreet(addressDTO.getStreet());
		address.setNeighborhood(addressDTO.getNeighborhood());
		address.setZipcode(addressDTO.getZipcode());
		address.setCity(addressDTO.getCity() != null ? addressDTO.getCity() : deliveryArea.getCity());
		address.setState(addressDTO.getState() != null ? addressDTO.getState() : deliveryArea.getState());
		address.setDeliveryArea(deliveryArea);

		return address;
	}
}
