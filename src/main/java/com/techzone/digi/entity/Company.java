package com.techzone.digi.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String domain;
	private String cpfCnpj;
	private String name;
	private String email;
	private Boolean isActive;

	@ElementCollection
	@CollectionTable(name = "PHONES")
	Set<String> phones = new HashSet<>();

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
	private List<Address> addresses = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
	private List<Product> products = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
	private List<OrderSale> orders = new ArrayList<>();

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
	private List<DeliveryArea> deliveryAreas = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
	private List<User> users = new ArrayList<>();

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "attachment_id")
	private Attachment logo;

	private Date creationDate;
	private Date modifiedDate;

	public Company() {

	}

	public Company(Long id, String domain, String cpfCnpj, String name, String email, Boolean isActive,
			Set<String> phones, List<Address> addresses, Date creationDate, Date modifiedDate) {
		super();
		this.id = id;
		this.domain = domain;
		this.cpfCnpj = cpfCnpj;
		this.name = name;
		this.email = email;
		this.isActive = isActive;
		this.phones = phones;
		this.addresses = addresses;
		this.creationDate = creationDate;
		this.modifiedDate = modifiedDate;
	}

	public Company(Long id, String domain, String cpfCnpj, String name, String email, Boolean isActive,
			Set<String> phones, List<Address> addresses) {
		super();
		this.id = id;
		this.domain = domain;
		this.cpfCnpj = cpfCnpj;
		this.name = name;
		this.email = email;
		this.isActive = isActive;
		this.phones = phones;
		this.addresses = addresses;

	}

	@PrePersist
	public void prePersist() {
		creationDate = new Date();
	}

	@PreUpdate
	public void preUpdate() {
		modifiedDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<OrderSale> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderSale> orders) {
		this.orders = orders;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public List<DeliveryArea> getDeliveryAreas() {
		return deliveryAreas;
	}

	public void setDeliveryAreas(List<DeliveryArea> deliveryAreas) {
		this.deliveryAreas = deliveryAreas;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Attachment getLogo() {
		return logo;
	}

	public void setLogo(Attachment logo) {
		this.logo = logo;
	}

}
