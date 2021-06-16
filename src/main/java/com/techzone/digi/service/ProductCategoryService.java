package com.techzone.digi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techzone.digi.dto.ProductCategoryDTO;
import com.techzone.digi.entity.Attachment;
import com.techzone.digi.entity.Company;
import com.techzone.digi.entity.ProductCategory;
import com.techzone.digi.repository.ProductCategoryRepository;
import com.techzone.digi.security.UserSS;
import com.techzone.digi.service.exception.AuthorizationException;
import com.techzone.digi.service.exception.ObjectNotFoundException;

@Service
public class ProductCategoryService {

	@Autowired
	private ProductCategoryRepository productCategoryRepository;
	
	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private CompanyService companyService;

	public ProductCategory save(ProductCategory productCategory) {
		UserSS userAuthenticated = UserService.authenticated();
		if (userAuthenticated == null || !userAuthenticated.isAdmin()
				|| userAuthenticated.getCompany().getId() != productCategory.getCompany().getId())
			throw new AuthorizationException("Acesso negado.");
		return productCategoryRepository.save(productCategory);
	}

	public ProductCategory findById(Long id) {
		Optional<ProductCategory> category = productCategoryRepository.findById(id);
		return category.orElseThrow(() -> new ObjectNotFoundException(ProductCategory.class.getName() + " not found"));
	}

	public ProductCategory findByIdAndCompanyDomain(Long id, String companyDomain) {
		Optional<ProductCategory> productCategory = productCategoryRepository.findByIdAndCompanyDomain(id,
				companyDomain);
		return productCategory
				.orElseThrow(() -> new ObjectNotFoundException(ProductCategory.class.getName() + " not found"));
	}

	public List<ProductCategory> findAllByCompanyDomain(String companyDomain) {
		return productCategoryRepository.findByCompanyDomain(companyDomain);
	}

	public ProductCategory fromDTO(ProductCategoryDTO dto) {
		Attachment attachment = null;
		Company company = null;
		if (!dto.getCompany().equals(null)) {
			company = companyService.findByDomain(dto.getCompany());
		}
		if (!dto.getAttachment().equals(null)) {
			attachment = attachmentService.findById(dto.getAttachment());
		}
		ProductCategory productCategory = new ProductCategory();
		productCategory.setId(dto.getId());
		productCategory.setName(dto.getName());
		productCategory.setCompany(company);
		productCategory.setAttachment(attachment);;
		return productCategory;
	}

}
