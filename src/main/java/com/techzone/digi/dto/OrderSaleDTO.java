package com.techzone.digi.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.techzone.digi.entity.OrderItem;
import com.techzone.digi.entity.OrderSale;
import com.techzone.digi.enums.OrderStatus;

public class OrderSaleDTO {
	private Long id;
	private String observation;
	private UserDTO client;
	private AddressDTO address;
	private List<OrderItemDTO> itens = new ArrayList<>();
	private BigDecimal discount;
	private BigDecimal freightCost;
	private Boolean delivery;
	private Integer paymentMethod;
	private BigDecimal changeValue;
	private Integer status;
	private String companyDomain;

	public OrderSaleDTO() {

	}

	public OrderSaleDTO(Long id, String observation, UserDTO client, AddressDTO address, List<OrderItemDTO> itens,
			BigDecimal discount, BigDecimal freightCost, Boolean delivery, Integer paymentMethod, BigDecimal changeValue,
			OrderStatus status, String companyDomain) {
		super();
		this.id = id;
		this.observation = observation;
		this.client = client;
		this.address = address;
		this.itens = itens;
		this.discount = discount;
		this.freightCost = freightCost;
		this.delivery = delivery;
		this.paymentMethod = paymentMethod;
		this.changeValue = changeValue;
		this.status = status.getCod();
		this.companyDomain = companyDomain;
	}

	public OrderSaleDTO(OrderSale orderSale) {
		this.id = orderSale.getId();
		this.observation = orderSale.getObservation();
		this.client = new UserDTO(orderSale.getClient());
		this.address = new AddressDTO(orderSale.getAddress());
		this.discount = orderSale.getDiscount();
		this.freightCost = orderSale.getFreightCost();
		this.delivery = orderSale.getDelivery();
		this.paymentMethod = orderSale.getPaymentMethod().getCod();
		this.changeValue = orderSale.getChangeValue();
		this.companyDomain = orderSale.getCompany().getDomain();
		this.status = orderSale.getStatus().getCod();
		for (OrderItem orderItem : orderSale.getItens()) {
			this.itens.add(new OrderItemDTO(orderItem));
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public UserDTO getClient() {
		return client;
	}

	public void setClient(UserDTO client) {
		this.client = client;
	}

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	public List<OrderItemDTO> getItens() {
		return itens;
	}

	public void setItens(List<OrderItemDTO> itens) {
		this.itens = itens;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getFreightCost() {
		return freightCost;
	}

	public void setFreightCost(BigDecimal freightCost) {
		this.freightCost = freightCost;
	}

	public Boolean getDelivery() {
		return delivery;
	}

	public void setDelivery(Boolean delivery) {
		this.delivery = delivery;
	}

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getChangeValue() {
		return changeValue;
	}

	public void setChangeValue(BigDecimal change) {
		this.changeValue = change;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCompanyDomain() {
		return companyDomain;
	}

	public void setCompanyDomain(String companyDomain) {
		this.companyDomain = companyDomain;
	}

	public BigDecimal getTotal() {
		BigDecimal total = new BigDecimal("0.00");
		for (OrderItemDTO orderItem : itens) {
			total.add(orderItem.getQuantity().multiply(orderItem.getSalePrice()));
		}
		total.subtract(discount);
		total.add(freightCost);
		return total;
	}

}
