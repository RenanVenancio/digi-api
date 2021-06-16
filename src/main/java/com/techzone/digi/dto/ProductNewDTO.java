package com.techzone.digi.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import com.sun.istack.NotNull;

public class ProductNewDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	@NotNull
	private String name;
	private String description;
	private BigDecimal costPrice;
	private BigDecimal salePrice;
	private String company;
	private Long productCategory;
	private Long attachment;

	public ProductNewDTO() {

	}

	public ProductNewDTO(Long id, String name, String description, BigDecimal costPrice, BigDecimal salePrice,
			String company, Long productCategory, Long attachment) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.costPrice = costPrice;
		this.salePrice = salePrice;
		this.company = company;
		this.productCategory = productCategory;
		this.attachment = attachment;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Long getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(Long productCategory) {
		this.productCategory = productCategory;
	}

	public Long getAttachment() {
		return attachment;
	}

	public void setAttachment(Long attachment) {
		this.attachment = attachment;
	}

}
