package com.techzone.digi.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.techzone.digi.entity.Product;

public class ProductDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private BigDecimal costPrice;
	private BigDecimal salePrice;
	private BigDecimal promotionalValue;
	private Date promotionValidity;

	private String company;
	private ProductCategoryDTO productCategory;
	private Long attachment;

	public ProductDTO() {

	}

	public ProductDTO(Long id, String name, String description, BigDecimal costPrice, BigDecimal salePrice,
			BigDecimal promotionalValue, Date promotionValidity, String company, ProductCategoryDTO productCategory,
			Long attachment) {
		super();
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

	public ProductDTO(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.costPrice = product.getCostPrice();
		this.salePrice = product.getSalePrice();
		this.promotionalValue = product.getPromotionalValue() != null ? product.getPromotionalValue() : null;
		this.promotionValidity = product.getPromotionValidity() != null ? product.getPromotionValidity() : null;
		this.company = product.getCompany() == null ? null : product.getCompany().getDomain();
		this.productCategory = new ProductCategoryDTO(product.getProductCategoty());
		this.attachment = product.getAttachment() != null ? product.getAttachment().getId() : null;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Long getAttachment() {
		return attachment;
	}

	public void setAttachment(Long attachment) {
		this.attachment = attachment;
	}

	public ProductCategoryDTO getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategoryDTO productCategory) {
		this.productCategory = productCategory;
	}

}
