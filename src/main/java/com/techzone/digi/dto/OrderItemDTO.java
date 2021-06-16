package com.techzone.digi.dto;

import java.math.BigDecimal;

import com.techzone.digi.entity.OrderItem;

public class OrderItemDTO {

	private Long order;
	private Long product;
	private BigDecimal quantity;
	private BigDecimal salePrice;
	private String observation;

	public OrderItemDTO() {

	}

	public OrderItemDTO(Long order, Long product, BigDecimal quantity, BigDecimal saleprice, String observation) {
		super();
		this.order = order;
		this.product = product;
		this.quantity = quantity;
		this.salePrice = saleprice;
		this.observation = observation;
	}

	public OrderItemDTO(OrderItem orderItem) {
		this.order = orderItem.getOrder().getId();
		this.product = orderItem.getProduct().getId();
		this.quantity = orderItem.getQuantity();
		this.salePrice = orderItem.getSalePrice();
		this.observation = orderItem.getObservation();
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public Long getProduct() {
		return product;
	}

	public void setProduct(Long product) {
		this.product = product;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

}
