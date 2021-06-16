package com.techzone.digi.dto;

import java.util.HashSet;
import java.util.Set;

import com.techzone.digi.entity.Company;

public class CompanyDTO {
	private Long id;
	private String domain;
	private String cpfCnpj;
	private String name;
	private String email;
	private Boolean isActive;
	private AddressDTO address;
	private UserNewDTO user;
	private Long logo;
	Set<String> phones = new HashSet<>();

	public CompanyDTO() {

	}

	public CompanyDTO(Long id, String domain, String cpfCnpj, String name, String email, Boolean isActive,
			AddressDTO address, UserNewDTO user, Set<String> phones, Long logo) {
		super();
		this.id = id;
		this.domain = domain;
		this.cpfCnpj = cpfCnpj;
		this.name = name;
		this.email = email;
		this.isActive = isActive;
		this.address = address;
		this.user = user;
		this.phones = phones;
		this.logo = logo;
	}

	public CompanyDTO(Company company) {
		this.id = company.getId();
		this.domain = company.getDomain();
		this.cpfCnpj = company.getCpfCnpj();
		this.name = company.getName();
		this.email = company.getEmail();
		this.isActive = company.getIsActive();
		this.address = !company.getAddresses().isEmpty() ? new AddressDTO(company.getAddresses().get(0)) : null;
		this.phones = company.getPhones();
		this.logo = company.getLogo() != null ? company.getLogo().getId() : null;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Set<String> getPhones() {
		return phones;
	}

	public void setPhones(Set<String> phones) {
		this.phones = phones;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	public UserNewDTO getUser() {
		return user;
	}

	public void setUser(UserNewDTO user) {
		this.user = user;
	}

	public Long getLogo() {
		return logo;
	}

	public void setLogo(Long logo) {
		this.logo = logo;
	}

}
