package com.techzone.digi.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.techzone.digi.enums.OrderStatus;
import com.techzone.digi.enums.PaymentMethod;

@Entity
public class OrderSale implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String observation;

	@ManyToOne
	@JoinColumn(name = "client_id")
	private User client;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "address_id")
	private Address address;

	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItem> itens = new ArrayList<>();

	private BigDecimal discount;

	private BigDecimal freightCost;

	private Boolean delivery;

	private Integer paymentMethod;

	private BigDecimal changeValue;

	private Integer status;

	private Date creationDate;

	private Date modifiedDate;

	public OrderSale() {

	}

	public OrderSale(Long id, String observation, User client, Address address, Company company, List<OrderItem> itens,
			BigDecimal discount, BigDecimal freightCost, Boolean delivery, PaymentMethod paymentMethod,
			BigDecimal changeValue, OrderStatus status) {
		super();
		this.id = id;
		this.observation = observation;
		this.client = client;
		this.address = address;
		this.company = company;
		this.itens = itens;
		this.discount = discount;
		this.freightCost = freightCost;
		this.delivery = delivery;
		this.paymentMethod = paymentMethod.getCod();
		this.changeValue = changeValue;
		this.status = status.getCod();
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

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public User getClient() {
		return client;
	}

	public void setClient(User client) {
		this.client = client;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public List<OrderItem> getItens() {
		return itens;
	}

	public void setItens(List<OrderItem> itens) {
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

	public PaymentMethod getPaymentMethod() {
		return PaymentMethod.toEnum(paymentMethod);
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod.getCod();
	}

	public BigDecimal getChangeValue() {
		return changeValue;
	}

	public void setChangeValue(BigDecimal changeValue) {
		this.changeValue = changeValue;
	}

	public OrderStatus getStatus() {
		return OrderStatus.toEnum(status);
	}

	public void setStatus(OrderStatus status) {
		this.status = status.getCod();
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

	public BigDecimal getTotal() {
		BigDecimal total = new BigDecimal("0.00");
		for (OrderItem orderItem : itens) {
			total = total.add(orderItem.getSubtotal());
		}
		total = total.subtract(discount);
		total = total.add(freightCost);

		return total;
	}

}
