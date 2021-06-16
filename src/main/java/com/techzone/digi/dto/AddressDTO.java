package com.techzone.digi.dto;

import com.techzone.digi.entity.Address;
import com.techzone.digi.enums.UF;

public class AddressDTO {
	private Long id;
	private String street;
	private String neighborhood;
	private String complement;
	private String zipcode;
	private String city;
	private Integer state;
	private Long deliveryArea;

	public AddressDTO() {

	}

	public AddressDTO(Long id, String street, String neighborhood, String complement, String zipcode, String city,
			UF state, Long deliveryArea) {
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

	public AddressDTO(Address address) {
		this.id = address.getId();
		this.street = address.getStreet();
		this.neighborhood = address.getNeighborhood();
		this.complement = address.getComplement();
		this.zipcode = address.getZipcode();
		this.city = address.getCity();
		this.state = address.getState() != null ? address.getState().getIBGECode() : null;
		this.deliveryArea = address.getDeliveryArea() != null ? address.getDeliveryArea().getId() : null;
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

	public Long getDeliveryArea() {
		return deliveryArea;
	}

	public void setDeliveryArea(Long deliveryArea) {
		this.deliveryArea = deliveryArea;
	}

}
