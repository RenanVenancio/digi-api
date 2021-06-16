package com.techzone.digi.service;

import java.util.ArrayList;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.techzone.digi.dto.CompanyDTO;
import com.techzone.digi.entity.Address;
import com.techzone.digi.entity.Attachment;
import com.techzone.digi.entity.Company;
import com.techzone.digi.entity.User;
import com.techzone.digi.repository.CompanyRepository;
import com.techzone.digi.security.UserSS;
import com.techzone.digi.service.exception.AuthorizationException;
import com.techzone.digi.service.exception.BusinessRuleException;
import com.techzone.digi.service.exception.DataIntegrityException;
import com.techzone.digi.service.exception.ObjectNotFoundException;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private AttachmentService attachmentService;

	public Company findById(Long id) {
		Optional<Company> company = companyRepository.findById(id);
		return company.orElseThrow(() -> new ObjectNotFoundException(Company.class.getName() + " not found"));
	}

	/**
	 * Método responsável por salvar uma nova empresa no banco de dados
	 * 
	 * @param company - Instância da empresa
	 * @return - Empresa salva no banco de dados.
	 */
	@Transactional
	public Company save(Company company) {
		company.setId(null);
		if (userService.userExists(company.getUsers().get(0).getEmail()))
			throw new BusinessRuleException("O email " + company.getUsers().get(0).getEmail() + " já possui registro.");
		if (!companyRepository.findByDomain(company.getDomain()).isEmpty())
			throw new DataIntegrityException("Já existe uma empresa com o domínio " + company.getDomain());
		company.getUsers().get(0).setIsAdmin(true);
		company = companyRepository.save(company);
		return company;
	}

	/**
	 * Método responsável por realizar uma busca por uma empresa pelo nome do
	 * domínio.
	 * 
	 * @param domain - String: Nome do domínio da empresa.
	 * @return Company: Instância da empresa localizada.
	 */
	public Company findByDomain(String domain) {
		Optional<Company> company = companyRepository.findByDomain(domain);
		return company.orElseThrow(() -> new ObjectNotFoundException(Company.class.getName() + " not found"));
	}

	public Page<Company> search(String domain, String name, Integer page, Integer itensPerPage, String orderBy,
			String direction) {
		PageRequest pageRequest = PageRequest.of(page, itensPerPage, Direction.valueOf(direction), orderBy);
		if (domain != null && !domain.equals("")) {
			return companyRepository.findDistinctByDomain(domain, pageRequest);
		}
		return companyRepository.findDistinctByNameContainingIgnoreCase(name, pageRequest);
	}

	public Company update(Company company) {
		checkPermissions(company.getId());
		Company updatedcompany = findById(company.getId());
		updateData(updatedcompany, company);
		return companyRepository.save(updatedcompany);
	}

	private void updateData(Company oldObj, Company newObj) {
		oldObj.setAddresses(newObj.getAddresses());
		oldObj.setCpfCnpj(newObj.getCpfCnpj());
		oldObj.setDomain(newObj.getDomain());
		oldObj.setEmail(newObj.getEmail());
		oldObj.setIsActive(newObj.getIsActive());
		oldObj.setName(newObj.getName());
		oldObj.setPhones(newObj.getPhones());
	}

	public void checkPermissions(Long companyId) {
		UserSS userAuthenticated = UserService.authenticated();
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| userAuthenticated.getCompany().getId() != companyId)
			throw new AuthorizationException("Acesso negado.");
	}

	/**
	 * Método responsável por converter uma instância de DTO para uma instâncoa da
	 * empresa.
	 * 
	 * @param companyDTO - CompanyDTO: Objeto dto com os seus atributos.
	 * @param isUpdate   - Boolean: Caso seja uma atualização deve ser setado como
	 *                   false
	 * 
	 * @return - Company: Instância da empresa de acordo com o DTO fornecido.
	 */
	public Company fromDTO(CompanyDTO companyDTO, Boolean isUpdate) {
		User user = new User();
		Address address = new Address();
		Attachment logo = new Attachment();
		Company company = new Company();
		if (isUpdate && companyDTO.getId() == null) {
			company = findByDomain(companyDTO.getDomain());
			companyDTO.setId(company.getId());
			address.setId(company.getAddresses().get(0).getId());
		}
		if (logo != null) {
			logo = attachmentService.findById(companyDTO.getLogo());
			logo.setCompany(company);
		}
		address.setZipcode(companyDTO.getAddress().getZipcode());
		address.setStreet(companyDTO.getAddress().getStreet());
		address.setCity(companyDTO.getAddress().getCity());
		address.setComplement(companyDTO.getAddress().getComplement());
		address.setState(companyDTO.getAddress().getState());
		address.setNeighborhood(companyDTO.getAddress().getNeighborhood());
		ArrayList<Address> addr = new ArrayList<>();
		addr.add(address);
		company.setId(companyDTO.getId());
		company.setDomain(companyDTO.getDomain());
		company.setCpfCnpj(companyDTO.getCpfCnpj());
		company.setName(companyDTO.getName());
		company.setEmail(companyDTO.getEmail());
		company.setIsActive(companyDTO.getIsActive());
		company.setPhones(companyDTO.getPhones());
		company.setLogo(logo);
		company.setAddresses(addr);
		if (!isUpdate) {
			user = userService.fromDTO(companyDTO.getUser());
			user.setCompany(company);
			company.getUsers().add(user);
		}
		addr.get(0).setCompany(company);

		return company;
	}

}
