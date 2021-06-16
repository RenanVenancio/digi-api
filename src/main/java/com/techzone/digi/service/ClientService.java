package com.techzone.digi.service;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techzone.digi.dto.ClientNewDTO;
import com.techzone.digi.entity.Address;
import com.techzone.digi.entity.User;
import com.techzone.digi.repository.ClientRepository;
import com.techzone.digi.security.UserSS;
import com.techzone.digi.service.exception.AuthorizationException;
import com.techzone.digi.service.exception.BusinessRuleException;
import com.techzone.digi.service.exception.ObjectNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private AddressService addressService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * Responsável por realizar a busca de um Cliente por ID, um cliente só pode
	 * recuperar os seus próprios dados.
	 * 
	 * @param id - Long: Id do cliente
	 * @return - User: Instância do usuário persistido no banco de dados.
	 */
	public User findById(UUID id) {
		//checkPermissions(id);
		Optional<User> client = clientRepository.findById(id);
		return client.orElseThrow(() -> new ObjectNotFoundException(User.class.getName() + " not found"));
	}

	/**
	 * Retorna o usuário logado
	 * 
	 * @return User - Instância do usuário logado atualmente.
	 */
	public User loadAuthenticatedUser() {
		UserSS userAuthenticated = UserService.authenticated();
		if (userAuthenticated == null) {
			throw new ObjectNotFoundException(User.class.getName() + " not found");
		}
		Optional<User> client = clientRepository.findById(userAuthenticated.getId());
		return client.orElseThrow(() -> new ObjectNotFoundException(User.class.getName() + " not found"));
	}

	/**
	 * Médodo responsáveçl por registrar um novo cliente, um cliente possui apenas
	 * aspermissões básicas por esse motivo a flag isAdmin é setada como false.
	 * 
	 * @param client - User: Instância do cliente.
	 * @return User - Cliente salvo no banco de dados.
	 */
	public User save(User client) {
		client.setId(null);
		User clientFound = clientRepository.findByEmail(client.getEmail());
		if (!(clientFound == null))
			throw new BusinessRuleException("O email " + client.getEmail() + " já possui registro.");
		client.setIsAdmin(false);
		return clientRepository.save(client);
	}

	/**
	 * Método responsável por salvar um novo cliente e setar como usátio comum.
	 * 
	 * @param - Client: Instância com os dados do cliente.
	 * @return - Client: Cliente persistido no banco de dados.
	 */
	public User saveSimple(User client) {
		client.setId(null);
		client.setIsAdmin(false);
		return clientRepository.save(client);
	}

	/**
	 * Método responsável por realizar a atualização de um objeto já existente. Um
	 * cliente só pode alterar seu próprio cadastro
	 * 
	 * @param user - User: Instância do usuário com os dados atualizados.
	 * @return - User: Usuário salvo no banco de dados com seus dados atualizados.
	 */
	public User update(User user) {
		checkPermissions(user.getId());
		User updatedProduct = findById(user.getId());
		updateData(updatedProduct, user);
		return clientRepository.save(updatedProduct);
	}

	/**
	 * Responsável por deletar um cliente do banco de dados. Um cliente só pode
	 * deletar deu próprio cadastro
	 * 
	 * @param id - Long: Id do usuário.
	 */
	public void delete(UUID id) {
		checkPermissions(id);
		clientRepository.deleteById(id);
	}

	/**
	 * Método responsável por checar as permissões do usuário.
	 * 
	 * @param userId
	 */
	public void checkPermissions(UUID userId) {
		UserSS userAuthenticated = UserService.authenticated();
		if (userAuthenticated == null || !userAuthenticated.getId().equals(userId))
			throw new AuthorizationException("Acesso negado.");
	}

	/**
	 * Responsável por realizar a arualização dos dados de um objeto tipo User
	 * existente.
	 * 
	 * @param oldObj - User: Instância do usuário com os dados antigos.
	 * @param newObj - User: Instância do usuário com os dados atualizados.
	 */
	private void updateData(User oldObj, User newObj) {
		oldObj.setEmail(newObj.getEmail());
		oldObj.setName(newObj.getName());
		oldObj.setPassword(bCryptPasswordEncoder.encode(newObj.getPassword()));
		oldObj.setPhone(newObj.getPhone());
	}

	/**
	 * Converte um objeto DTO para um objeto User
	 * 
	 * @param clientDTO - ClientDTO: Instância do objeto DTO.
	 * @return User: Instância do cliente.
	 */
	public User fromDTO(ClientNewDTO clientDTO) {
		Address address = addressService.fromDTO(clientDTO.getAddress());
		User user = new User(null, clientDTO.getName(), clientDTO.getEmail(),
				bCryptPasswordEncoder.encode(clientDTO.getPassword()), false, clientDTO.getPhone(),
				Arrays.asList(address), null);
		address.setUser(user);
		return user;
	}
}
