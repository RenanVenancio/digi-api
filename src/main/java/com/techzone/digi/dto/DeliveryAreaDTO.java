package com.techzone.digi.dto;

import java.math.BigDecimal;

import com.techzone.digi.entity.DeliveryArea;
import com.techzone.digi.enums.UF;

public class DeliveryAreaDTO {

	private Long id;
	private String city;
	private String cityIBGEId;
	private BigDecimal deliveryValue;
	private Integer state;
	private String company;

	public DeliveryAreaDTO() {

	}

	public DeliveryAreaDTO(Long id, String city, String cityIBGEId, BigDecimal deliveryValue, String state,
			String company) {
		super();
		this.id = id;
		this.city = city;
		this.cityIBGEId = cityIBGEId;
		this.deliveryValue = deliveryValue;
		this.state = UF.valueOf(state).getCode();
		this.company = company;
	}

	public DeliveryAreaDTO(DeliveryArea deliveryArea) {
		this.id = deliveryArea.getId();
		this.city = deliveryArea.getCity();
		this.cityIBGEId = deliveryArea.getCityIBGEId();
		this.deliveryValue = deliveryArea.getDeliveryValue();
		this.state = deliveryArea.getState().getCode();
		this.company = deliveryArea.getCompany().getDomain();
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

	public BigDecimal getDeliveryValue() {
		return deliveryValue;
	}

	public void setDeliveryValue(BigDecimal deliveryValue) {
		this.deliveryValue = deliveryValue;
	}

	public String getState() {
		return UF.toEnum(state).toString();
	}

	public void setState(String state) {
		this.state = UF.valueOf(state).getCode();
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

}
