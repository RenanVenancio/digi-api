package com.techzone.digi.dto;

import java.util.UUID;

import com.techzone.digi.entity.User;

public class UserDTO {
	private UUID id;
	private String name;
	private String email;
	private String phone;
	private String company;
	private AddressDTO address;
	private Boolean isAdmin;

	public UserDTO() {

	}

	public UserDTO(UUID id, String name, String email, String phone, String company, AddressDTO address,
			Boolean isAdmin) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.company = company;
		this.address = address;
		this.isAdmin = isAdmin;
	}

	public UserDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.phone = user.getPhone();
		this.company = user.getCompany() != null ? user.getCompany().getDomain() : null;
		this.address = user.getAddress().size() > 0 ? new AddressDTO(user.getAddress().get(0)) : null;
		this.isAdmin = user.getIsAdmin();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
