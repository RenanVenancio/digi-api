package com.techzone.digi.dto;

import com.techzone.digi.entity.ProductCategory;

public class ProductCategoryDTO {
	private Long id;
	private String name;
	private String company;
	private Long attachment;

	public ProductCategoryDTO() {

	}

	public ProductCategoryDTO(Long id, String name, String company, Long attachment) {
		super();
		this.id = id;
		this.name = name;
		this.company = company;
		this.attachment = attachment;
	}

	public ProductCategoryDTO(ProductCategory productCategory) {
		this.id = productCategory.getId();
		this.name = productCategory.getName();
		this.company = productCategory.getCompany() != null ? productCategory.getCompany().getDomain() : null;
		this.attachment = productCategory.getAttachment() != null ? productCategory.getAttachment().getId() : null;
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

	public String getCompany() {
		return company;
	}

	public Long getAttachment() {
		return attachment;
	}

	public void setAttachment(Long attachment) {
		this.attachment = attachment;
	}

	public void setCompany(String company) {
		this.company = company;
	}

}
