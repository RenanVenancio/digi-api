package com.techzone.digi.entity;

import java.io.Serializable;
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

@Entity
public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private BigDecimal costPrice;
	private BigDecimal salePrice;
	private BigDecimal promotionalValue;
	private Date promotionValidity;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "category_id")
	private ProductCategory productCategory;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "attachment_id")
	private Attachment attachment;

	private Date creationDate;
	private Date modifiedDate;

	public Product() {

	}

	public Product(Long id, String name, String description, BigDecimal costPrice, BigDecimal salePrice,
			BigDecimal promotionalValue, Date promotionValidity, ProductCategory productCategory, Company company,
			Attachment attachment) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.costPrice = costPrice;
		this.salePrice = salePrice;
		this.promotionalValue = promotionalValue;
		this.promotionValidity = promotionValidity;
		this.company = company;
		this.productCategory = productCategory;
		this.attachment = attachment;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getPromotionalValue() {
		return promotionalValue;
	}

	public void setPromotionalValue(BigDecimal promotionalValue) {
		this.promotionalValue = promotionalValue;
	}

	public Date getPromotionValidity() {
		return promotionValidity;
	}

	public void setPromotionValidity(Date promotionValidity) {
		this.promotionValidity = promotionValidity;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public ProductCategory getProductCategoty() {
		return productCategory;
	}

	public void setProductCategoty(ProductCategory productCategoty) {
		this.productCategory = productCategoty;
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
