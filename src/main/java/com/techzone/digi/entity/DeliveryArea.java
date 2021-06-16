package com.techzone.digi.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.techzone.digi.enums.UF;

@Entity
public class DeliveryArea {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;

	private String city;

	private String cityIBGEId;

	private BigDecimal deliveryValue;

	private Integer state;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "company_id")
	private Company company;

	private Date creationDate;

	private Date modifiedDate;
	
	public DeliveryArea() {
		
	}

	public DeliveryArea(Long id, String city, String cityIBGEId, BigDecimal deliveryValue, UF uf, Company company) {
		super();
		this.id = id;
		this.city = city;
		this.cityIBGEId = cityIBGEId;
		this.deliveryValue = deliveryValue;
		this.state = uf.getIBGECode();

		this.company = company;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityIBGEId() {
		return cityIBGEId;
	}

	public void setCityIBGEId(String cityIBGEId) {
		this.cityIBGEId = cityIBGEId;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public BigDecimal getDeliveryValue() {
		return deliveryValue;
	}

	public void setDeliveryValue(BigDecimal deliveryValue) {
		this.deliveryValue = deliveryValue;
	}

	public UF getState() {
		return UF.toEnum(state);
	}

	public void setState(UF state) {
		this.state = state.getIBGECode();
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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

}
