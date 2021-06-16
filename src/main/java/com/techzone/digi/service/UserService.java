package com.techzone.digi.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techzone.digi.dto.UserNewDTO;
import com.techzone.digi.entity.Address;
import com.techzone.digi.entity.Company;
import com.techzone.digi.entity.User;
import com.techzone.digi.repository.UserRepository;
import com.techzone.digi.security.UserSS;
import com.techzone.digi.service.exception.AuthorizationException;
import com.techzone.digi.service.exception.BusinessRuleException;
import com.techzone.digi.service.exception.ObjectNotFoundException;

/**
 * @author Venancio
 *
 */

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * Método responsável por retornar o usuário logado na aplicação.
	 * 
	 * @return - Instância do usuário atendendo o contrato do SpringSecurity.
	 */
	public static UserSS authenticated() {
		try {
			UserSS user = (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return user;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Médodo responsáveçl por registrar um novo cliente, um cliente possui apenas
	 * aspermissões básicas por esse motivo a flag isAdmin é setada como false.
	 * 
	 * @param client - User: Instância do cliente.
	 * @return User - Cliente salvo no banco de dados.
	 */
	public User save(User user) {
		UserSS userAuthenticated = authenticated();
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| user.getCompany().getId() != userAuthenticated.getCompany().getId())
			throw new AuthorizationException("Acesso negado.");

		user.setId(null);
		if (userExists(user.getEmail()))
			throw new BusinessRuleException("O email " + user.getEmail() + " já possui registro.");
		user.setIsAdmin(true);
		return userRepository.save(user);
	}

	public User findById(UUID id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElseThrow(() -> new ObjectNotFoundException(User.class.getName() + " not found"));
	}

	public Boolean userExists(String email) {
		User clientFound = userRepository.findByEmail(email);
		if (clientFound == null)
			return false;
		return true;
	}

	public User fromDTO(UserNewDTO userDTO) {
		Company company = null;
		if (userDTO.getCompany() != null)
			company = companyService.findByDomain(userDTO.getCompany());

		Address address = new Address();
		if (userDTO.getAddress() != null)
			address = addressService.fromDTO(userDTO.getAddress());

		User user = new User(null, userDTO.getName(), userDTO.getEmail(),
				bCryptPasswordEncoder.encode(userDTO.getPassword()), false, userDTO.getPhone(), Arrays.asList(address),
				company);
		address.setUser(user);
		return user;
	}

}
