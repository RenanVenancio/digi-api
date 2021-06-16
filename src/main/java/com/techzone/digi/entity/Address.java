package com.techzone.digi.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import com.techzone.digi.enums.UF;

@Entity
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String street;
	private String neighborhood;
	private String complement;
	private String zipcode;
	private String city;
	private Integer state;
	private Date creationDate;
	private Date modifiedDate;
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "delivery_area_id")
	private DeliveryArea deliveryArea;
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "company_id")
	private Company company;

	@JsonIgnore
	@OneToMany(mappedBy = "address", cascade = CascadeType.ALL)
	private List<OrderSale> orderSale;

	public Address() {

	}

	public Address(Long id, String street, String neighborhood, String complement, String zipcode, String city,
			UF state, DeliveryArea deliveryArea) {
		super();
		this.id = id;
		this.street = street;
		this.neighborhood = neighborhood;
		this.complement = complement;
		this.zipcode = zipcode;
		this.city = city;
		this.state = state.getIBGECode();
		this.deliveryArea = deliveryArea;
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

	public void setId(Long id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public UF getState() {
		return UF.toEnum(state);
	}

	public void setState(UF state) {
		this.state = state.getIBGECode();
	}

	public DeliveryArea getDeliveryArea() {
		return deliveryArea;
	}

	public void setDeliveryArea(DeliveryArea deliveryArea) {
		this.deliveryArea = deliveryArea;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreationDate() {
		return creationDate;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<OrderSale> getOrderSale() {
		return orderSale;
	}

	public void setOrderSale(List<OrderSale> orderSale) {
		this.orderSale = orderSale;
	}

}
